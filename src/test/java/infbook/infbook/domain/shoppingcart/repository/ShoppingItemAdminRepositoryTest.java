package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.abstractTest.RepostoryTest;
import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;
import infbook.infbook.utils.models;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static infbook.infbook.utils.models.*;
import static infbook.infbook.utils.models.MEMBER_USERLEVEL;
import static org.assertj.core.api.Assertions.*;


class ShoppingItemAdminRepositoryTest extends RepostoryTest {


    @DisplayName("장바구니에 담긴 상품의 개수를 회원번호로 조회할 수 있다.")
    @Test
    void shoppingCartCountTest() {
        Member savedMember2 = Member.builder()
                .name(MEMBER_NAME)
                .accountId(MEMBER_ACCOUNTID)
                .password(MEMBER_PASSWORD)
                .birthDate(MEMBER_BIRTHDATE)
                .email(MEMBER_EMAIL)
                .telephone(MEMBER_TELEPHONE)
                .address(ADDRESS)
                .userLevel(MEMBER_USERLEVEL)
                .build();

        memberRepository.save(savedMember2);
        shoppingCartRepository.save(ShoppingCart.builder().member(savedMember2).build());

        int i = shoppingItemRepository.countShoppingItemByMember(savedMember2.getId());

        assertThat(i).isEqualTo(0);

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