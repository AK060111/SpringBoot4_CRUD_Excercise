package vn.edu.ute.admin.service;
import vn.edu.ute.admin.entity.Category;
import vn.edu.ute.admin.dto.CategoryForm;
import vn.edu.ute.admin.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
@Service @Transactional(readOnly=true) public class CategoryService {
    private final CategoryRepository repository;
    public CategoryService(CategoryRepository repository) {
        this.repository=repository;
    }
    public Page<Category> search(String keyword,int page) {
        return PageSupport.search(page,p->repository.findByNameContainingIgnoreCase(keyword,p));
    }
    public Category get(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
    @Transactional public void save(Long id,CategoryForm form) {
        Category c=id==null?new Category():get(id);
        c.setName(form.getName());
        repository.saveAndFlush(c);
    }
    @Transactional public void delete(Long id) {
        repository.delete(get(id));
        repository.flush();
    }
}
