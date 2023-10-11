package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID>,CustomShoppingItemRepository{

    @Modifying
    @Query("delete from ShoppingItem si where si.shoppingCart.id = " +
            "(select sc.id from ShoppingCart sc where sc.member.id =:memberId)")
    void deleteAllInShoppingCart(@Param(value = "memberId") Long memberId);

    @Query("select count(si.id) from ShoppingItem si join ShoppingCart sc on si.shoppingCart.id = sc.id where sc.member.id =:memberId")
    int countShoppingItemByMember(@Param(value = "memberId") Long memberId);

//    @Query("select si from ShoppingItem si where si.shoppingCart.id =: shoppingCartId and si.item.id =: itemId")
//    Optional<ShoppingItem> findShoppItemByShoppingCartAndItem(@Param(value = "shoppingCartId") Long shoppingCartId,
//                                                              @Param(value = "itemId") Long itemId);

    Optional<ShoppingItem> findShoppingItemByShoppingCartAndItemId(ShoppingCart shoppingCart, Long itemId);

    @Query("select si from ShoppingItem si join fetch si.item join ShoppingCart sc where sc.member.id =:memberId")
    List<ShoppingItem> findShoppingItemListByMemberId(@Param(value = "memberId") Long memberId);
}
