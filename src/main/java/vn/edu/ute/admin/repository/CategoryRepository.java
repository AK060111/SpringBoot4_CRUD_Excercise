package vn.edu.ute.admin.repository;
import vn.edu.ute.admin.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
