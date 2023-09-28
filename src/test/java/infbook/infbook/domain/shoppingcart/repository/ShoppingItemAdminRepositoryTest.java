package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.abstractUtils.RepostoryUtil;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;
import infbook.infbook.utils.TestConfig;
import infbook.infbook.utils.models;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Import({TestConfig.class})
@Transactional
@DataJpaTest
class ShoppingItemAdminRepositoryTest extends RepostoryUtil {


    @DisplayName("장바구니에 담긴 상품의 개수를 회원번호로 조회할 수 있다.")
    @Test
    void shoppingCartCountTest() {
        int i = shoppingItemRepository.countShoppingItemByMember(savedMember.getId());

        assertThat(i).isEqualTo(1);

    }

    @DisplayName("회원엔티티의 id로 쇼핑카트에 담긴 리스트를 찾을 수 있다.")
    @Test
    void shoppingItemLisTest() {

        List<CartItemDto> result = shoppingItemRepository.findShoppingItemByMemberId(savedMember.getId());
        assertThat(result).hasSize(1);
        assertThat(result).extracting(CartItemDto::getName).contains(savedItem.getName());
        assertThat(result.get(0).getQuantity()).isEqualTo(models.SHOPPINGTEM_QUANTITY);
    }
}