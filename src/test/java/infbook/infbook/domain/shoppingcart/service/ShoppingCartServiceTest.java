package infbook.infbook.domain.shoppingcart.service;

import infbook.infbook.abstractTest.ServiceTest;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import infbook.infbook.domain.shoppingcart.dto.CartPostRequest;
import infbook.infbook.domain.shoppingcart.dto.CartPutRequest;
import infbook.infbook.exception.ajax.AjaxCartNotFoundException;
import infbook.infbook.exception.ajax.AjaxItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

class ShoppingCartServiceTest extends ServiceTest {

    @InjectMocks
    ShoppingCartService shoppingCartService;

    private CartPostRequest cartPostRequest;
    private CartPutRequest cartPutRequest;

    @BeforeEach
    void setUpFakeCartPostRequest() {
        cartPostRequest = new CartPostRequest(1L, 1L, 10);
        cartPutRequest = new CartPutRequest(UUID.randomUUID(), 10);
    }

    @DisplayName("장바구니에 존재하지않는 상품을 추가할 시 AjaxItemNotFoundException발생")
    @Test
    void shoppingCartAddItem_fail_상품존재하지않음() {
        //given
        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingCartService.addShoppingCartItem(cartPostRequest))
                .isInstanceOf(AjaxItemNotFoundException.class);

    }

    @DisplayName("재고수량을 만족하지 않을시 '3'을 반환한다.")
    @Test
    void shoppingCartAddItem_Fail_재고수량을만족하지않음() {

        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.of(item));
        given(item.getStockQuantity()).willReturn(cartPostRequest.getRequestQuantity() - 1);
        //when
        String result = shoppingCartService.addShoppingCartItem(cartPostRequest);

        //then
        assertThat(result).isEqualTo("3");

    }

    @DisplayName("추가하고자 하는 장바구니가 존재하지 않을시 AjaxCartNotFoundException발생 ")
    @Test
    void shoppingCartAddItem_fail_장바구니존재하지않음() {
        //given
        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.of(item));
        given(item.getStockQuantity()).willReturn(cartPostRequest.getRequestQuantity() + 1);
        given(shoppingCartRepository.findByMemberId(any(Long.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingCartService.addShoppingCartItem(cartPostRequest))
                .isInstanceOf(AjaxCartNotFoundException.class);
    }

    @DisplayName("상품과 쇼핑카트가 존재하고 재고수량을 만족할시 장바구니에 처음담는 상품이면 장바구니에 상품을 추가한다.")
    @Test
    void shoppingCartAddItem_success_장바구니에존재하지않는상품_상품추가() {

        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.of(item));
        given(item.getStockQuantity()).willReturn(cartPostRequest.getRequestQuantity() + 1);
        given(shoppingCartRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(shoppingCart));
        given(shoppingItemRepository.findShoppingItemByShoppingCartAndItemId(any(ShoppingCart.class), any(Long.class)))
                .willReturn(Optional.empty());

        //when
        String result = shoppingCartService.addShoppingCartItem(cartPostRequest);

        //then
        then(shoppingItemRepository).should().save(any(ShoppingItem.class));
        assertThat(result).isEqualTo("1");
    }

    @DisplayName("장바구니에 이미 존재하는 상품이면 추가수량 요청과 현재수량을 더한값이 상품의 재고수량 보다 작으면 '3'을 반환한다.")
    @Test
    void shoppingCartAddItem_success_장바구니에이존재하는상품_재고부족() {

        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.of(item));
        given(item.getStockQuantity()).willReturn(cartPostRequest.getRequestQuantity() + 1);
        given(shoppingCartRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(shoppingCart));
        given(shoppingItemRepository.findShoppingItemByShoppingCartAndItemId(any(ShoppingCart.class), any(Long.class)))
                .willReturn(Optional.of(shoppingItem));
        given(shoppingItem.getQuantity()).willReturn(10);

        //when
        String result = shoppingCartService.addShoppingCartItem(cartPostRequest);

        //then
        assertThat(result).isEqualTo("3");
    }

    @DisplayName("장바구니에 이미 존재하는 상품이며 수량을 만족하면 현재 담긴 상품의 수량을 변경한다.")
    @Test
    void shoppingCartAddItem_success_장바구니에이미존재하는상품_수량변경() {

        given(itemAdminRepository.findById(any(Long.class))).willReturn(Optional.of(item));
        given(item.getStockQuantity()).willReturn(cartPostRequest.getRequestQuantity() + 10);
        given(shoppingCartRepository.findByMemberId(any(Long.class))).willReturn(Optional.of(shoppingCart));
        given(shoppingItemRepository.findShoppingItemByShoppingCartAndItemId(any(ShoppingCart.class), any(Long.class)))
                .willReturn(Optional.of(shoppingItem));
        given(shoppingItem.getQuantity()).willReturn(1);

        //when
        String result = shoppingCartService.addShoppingCartItem(cartPostRequest);

        //then
        //행위에 대한 검증 - shoppingItem 엔티티의 수량을 변경하며 DirtyChecking으로 update가 이루어진다.
        then(shoppingItem).should().changeQuantity(shoppingItem.getQuantity() + cartPostRequest.getRequestQuantity());

        assertThat(result).isEqualTo("2");
    }


}
