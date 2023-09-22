package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    @Query("select sc from ShoppingCart sc where sc.member.id = :memberId")
    Optional<ShoppingCart> findByMemberId(@Param(value = "memberId") Long memberId);
}
