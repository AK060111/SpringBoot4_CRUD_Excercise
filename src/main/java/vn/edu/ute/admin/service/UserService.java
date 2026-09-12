package vn.edu.ute.admin.service;
import vn.edu.ute.admin.entity.User;
import vn.edu.ute.admin.dto.UserForm;
import vn.edu.ute.admin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
@Service @Transactional(readOnly=true) public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    public UserService(UserRepository repository,PasswordEncoder encoder) {
        this.repository=repository;
        this.encoder=encoder;
    }
    public Page<User> search(String keyword,int page) {
        return PageSupport.search(page,p->repository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullnameContainingIgnoreCase(keyword,keyword,keyword,p));
    }
    public User get(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
    public boolean usernameTaken(String value,Long id) {
        return repository.existsByUsernameIgnoreCaseAndIdNot(value,id==null?-1L:id);
    }
    public boolean emailTaken(String value,Long id) {
        return repository.existsByEmailIgnoreCaseAndIdNot(value,id==null?-1L:id);
    }
    @Transactional public void save(Long id,UserForm form) {
        User u=id==null?new User():get(id);
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());
        u.setFullname(form.getFullname());
        u.setRole(form.getRole());
        if(form.getPassword()!=null&&!form.getPassword().isEmpty())u.setPassword(encoder.encode(form.getPassword()));
        repository.saveAndFlush(u);
    }
    @Transactional public void delete(Long id) {
        repository.delete(get(id));
        repository.flush();
    }
}
