package infbook.infbook.domain.category.repository;

import infbook.infbook.domain.category.domain.SubCategory;
import infbook.infbook.domain.category.domain.SubCategoryItem;
import infbook.infbook.domain.category.domain.SubCategoryItemPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    SubCategory findByName(String name);
}
