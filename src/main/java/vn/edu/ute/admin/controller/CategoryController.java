package vn.edu.ute.admin.controller;
import vn.edu.ute.admin.dto.CategoryForm;
import vn.edu.ute.admin.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataAccessException;
@Controller @RequestMapping("/admin/categories") public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service) {
        this.service=service;
    }
    @GetMapping public String list(@RequestParam(defaultValue="") String keyword,@RequestParam(defaultValue="0") int page,Model model) {
        keyword=keyword.strip();
        model.addAttribute("page",service.search(keyword,page));
        model.addAttribute("keyword",keyword);
        return "admin/categories/list";
    }
    @GetMapping("/add") public String add(Model model) {
        model.addAttribute("form",new CategoryForm());
        return "admin/categories/form";
    }
    @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model model) {
        var item=service.get(id);
        var form=new CategoryForm();
        form.setName(item.getName());
        model.addAttribute("form",form);
        model.addAttribute("id",id);
        return "admin/categories/form";
    }
    @PostMapping("/add") public String create(@Valid @ModelAttribute("form") CategoryForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        return save(null,form,errors,model,flash);
    }
    @PostMapping("/edit/{id}") public String update(@PathVariable Long id,@Valid @ModelAttribute("form") CategoryForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        service.get(id);
        return save(id,form,errors,model,flash);
    }
    private String save(Long id,CategoryForm form,BindingResult errors,Model model,RedirectAttributes flash) {
        model.addAttribute("id",id);
        if(errors.hasErrors())return "admin/categories/form";
        try {
            service.save(id,form);
        }
        catch(DataAccessException e) {
            errors.reject("database","Không thể lưu. Kiểm tra dữ liệu trùng hoặc kết nối cơ sở dữ liệu.");
            return "admin/categories/form";
        }
        flash.addFlashAttribute("success","Đã lưu thành công");
        return "redirect:/admin/categories";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes flash) {
        try {
            service.delete(id);
            flash.addFlashAttribute("success","Đã xóa thành công");
        }
        catch(DataAccessException e) {
            flash.addFlashAttribute("error","Không thể xóa do ràng buộc dữ liệu hoặc lỗi kết nối.");
        }
        return "redirect:/admin/categories";
    }
}
