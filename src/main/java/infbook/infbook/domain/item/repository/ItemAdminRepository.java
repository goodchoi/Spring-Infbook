package infbook.infbook.domain.item.repository;

import infbook.infbook.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemAdminRepository extends JpaRepository<Item, Long>{
    @Query("select i from Item i join fetch i.category c join fetch i.subCategories where i.id=:itemId")
    Optional<Item> findFetchItem(@Param("itemId") Long itemId);
}
