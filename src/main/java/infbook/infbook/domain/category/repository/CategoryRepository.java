package infbook.infbook.domain.category.repository;

import infbook.infbook.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);

    @Query("select c from Category c join fetch c.children cd")
    List<Category> findFetchAll();
}
