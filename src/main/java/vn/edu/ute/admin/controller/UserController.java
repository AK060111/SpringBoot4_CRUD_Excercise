package vn.edu.ute.admin.controller;
import vn.edu.ute.admin.dto.UserForm;
import vn.edu.ute.admin.service.UserService;
import vn.edu.ute.admin.entity.Role;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataAccessException;
@Controller @RequestMapping("/admin/users") public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service=service;
    }
    @ModelAttribute("roles") public Role[] roles() {
        return Role.values();
    }
    @GetMapping public String list(@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="0") int page,Model model) {
        keyword=keyword.strip();
        model.addAttribute("page",service.search(keyword,page));
        model.addAttribute("keyword",keyword);
        return "admin/users/list";
    }
    @GetMapping("/add") public String add(Model model) {
        model.addAttribute("form",new UserForm());
        return "admin/users/form";
    }
    @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model model) {
        var item=service.get(id);
        var form=new UserForm();
        form.setUsername(item.getUsername());
        form.setEmail(item.getEmail());
        form.setFullname(item.getFullname());
        form.setRole(item.getRole());
        model.addAttribute("form",form);
        model.addAttribute("id",id);
        return "admin/users/form";
    }
    @PostMapping("/add") public String create(@Valid @ModelAttribute("form") UserForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        return save(null,form,errors,model,flash);
    }
    @PostMapping("/edit/{id}") public String update(@PathVariable Long id,@Valid @ModelAttribute("form") UserForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        service.get(id);
        return save(id,form,errors,model,flash);
    }
    private String save(Long id,UserForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        model.addAttribute("id",id);
        String password=form.getPassword();
        if(id==null&&(password==null||password.isBlank()))errors.rejectValue("password","required","Mật khẩu bắt buộc khi thêm");
        if(password!=null&&!password.isEmpty()&&(password.isBlank()||password.length()<8||password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72))    errors.rejectValue("password","length","Mật khẩu từ 8 ký tự, tối đa 72 byte UTF-8 và không chỉ gồm khoảng trắng");
        if(!errors.hasFieldErrors("username")&&service.usernameTaken(form.getUsername(),id))errors.rejectValue("username","duplicate","Username đã tồn tại");
        if(!errors.hasFieldErrors("email")&&service.emailTaken(form.getEmail(),id))errors.rejectValue("email","duplicate","Email đã tồn tại");
        if(errors.hasErrors())return "admin/users/form";
        try {
            service.save(id,form);
        }
        catch(DataAccessException e) {
            errors.reject("database","Không thể lưu. Kiểm tra dữ liệu trùng hoặc kết nối cơ sở dữ liệu.");
            return "admin/users/form";
        }
        flash.addFlashAttribute("success","Đã lưu thành công");
        return "redirect:/admin/users";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes flash) {
        try {
            service.delete(id);
            flash.addFlashAttribute("success","Đã xóa thành công");
        }
        catch(DataAccessException e) {
            flash.addFlashAttribute("error","Không thể xóa do ràng buộc dữ liệu hoặc lỗi kết nối.");
        }
        return "redirect:/admin/users";
    }
}
