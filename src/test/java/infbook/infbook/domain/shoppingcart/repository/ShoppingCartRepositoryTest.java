package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.abstractUtils.RepostoryUtil;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class ShoppingCartRepositoryTest extends RepostoryUtil {


//    @DisplayName("장바구니는 회원당 하나를 가지며, 회원으로 조회가능하다.")
//    @Test
//    void findShoppingCart() {
//      //  ShoppingCart findshoppingCart = shoppingCartRepository.findByMemberId(savedMember);
//       // assertThat(findshoppingCart.getMember().getName()).isEqualTo(savedMember.getName());
//    }

}