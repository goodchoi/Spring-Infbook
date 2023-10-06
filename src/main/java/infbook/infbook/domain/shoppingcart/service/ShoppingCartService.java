package infbook.infbook.domain.shoppingcart.service;

import infbook.infbook.domain.item.domain.Item;
import infbook.infbook.domain.item.repository.ItemAdminRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingCart;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import infbook.infbook.domain.shoppingcart.dto.CartPostRequest;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;
import infbook.infbook.domain.shoppingcart.dto.CartPutRequest;
import infbook.infbook.domain.shoppingcart.dto.CartPutResponse;
import infbook.infbook.domain.shoppingcart.repository.ShoppingCartRepository;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import infbook.infbook.exception.ajax.AjaxCartNotFoundException;
import infbook.infbook.exception.ajax.AjaxItemNotFoundException;
import infbook.infbook.exception.ajax.AjaxShoppingItemNotFoundException;
import infbook.infbook.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemAdminRepository itemAdminRepository;

    public List<CartItemDto> getShoppingCartListByLoginUser(Long id) {
        return shoppingItemRepository.findShoppingItemByMemberId(id);
    }

    /**
     * memberId 검증 -ok
     * stock quantity 어떻게 처리할지 결정.
     * 캐싱을 사용하면 현재 메소드의 상당부분을 처리할 수 있겠으나 현재 구조에선 많은 쿼리문이 필요할 수 밖에없음.
     * <p>
     * <p>
     * 1-insert new Shopping item
     * 2-update shopping Item
     * 3-fail_out_of_stock
     */
    public String addShoppingCartItem(CartPostRequest cartPostRequest) {

        Item findItem = getItemCount(cartPostRequest.getItemId());

        if (findItem.getStockQuantity() < cartPostRequest.getRequestQuantity()) {
            return "3";
        }

        ShoppingCart findShoppingCart = getShoppingCart(cartPostRequest.getMemberId());

        Optional<ShoppingItem> optionalShoppingItem = shoppingItemRepository.
                findShoppingItemByShoppingCartAndItemId(findShoppingCart, cartPostRequest.getItemId());

        if (optionalShoppingItem.isPresent()) {
            ShoppingItem shoppingItem = optionalShoppingItem.get();
            if (shoppingItem.getQuantity() + cartPostRequest.getRequestQuantity() > findItem.getStockQuantity()) {
                return "3";
            }
            shoppingItem.changeQuantity(cartPostRequest.getRequestQuantity() + shoppingItem.getQuantity() );
            return "2";
        }

        ShoppingItem newShoppingItem = ShoppingItem.builder()
                .shoppingCart(findShoppingCart)
                .item(findItem)
                .quantity(cartPostRequest.getRequestQuantity()).build();
        shoppingItemRepository.save(newShoppingItem);

        return "1";
    }

    public CartPutResponse modifyShoppingCartItem(CartPutRequest cartPutRequest) {
        ShoppingItem shoppingItem = getShoppingItem(cartPutRequest.getShoppingItemId());
        Integer stockQuantity = getItemCount(shoppingItem.getItem().getId()).getStockQuantity();
        if (stockQuantity == 0 ) {
            return CartPutResponse.outOfStockPutResponse();
        }
        if (stockQuantity >= cartPutRequest.getRequestQuantity()) {
            shoppingItem.changeQuantity(cartPutRequest.getRequestQuantity());
            return CartPutResponse.successPutResponse();
        }
        if (shoppingItem.getQuantity() > cartPutRequest.getRequestQuantity()) {
            shoppingItem.changeQuantity(cartPutRequest.getRequestQuantity());
        }
        return CartPutResponse.insufficientStockPutResponse(stockQuantity);
    }

    public String deleteShoppingItem(UUID shoppingItemId) {
        ShoppingItem shoppingItem = getShoppingItem(shoppingItemId);
        shoppingItemRepository.delete(shoppingItem);
        return "1";
    }

    private ShoppingItem getShoppingItem(UUID shoppingItemId) {
        return shoppingItemRepository.findById(shoppingItemId)
                .orElseThrow(() -> new AjaxShoppingItemNotFoundException(
                        String.format("%s에 해당하는 쇼핑상품이 존재하지 않습니다.", shoppingItemId),
                        ErrorCode.SI_NOT_FOUND));
    }

    private ShoppingCart getShoppingCart(Long memberId) {
        return shoppingCartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AjaxCartNotFoundException(
                        String.format("%s에 해당하는 쇼핑카트가 존재하지 않습니다.", memberId),
                        ErrorCode.CART_NOT_FOUND));
    }

    private Item getItemCount(Long itemId) {
        return itemAdminRepository.findById(itemId)
                .orElseThrow(() -> new AjaxItemNotFoundException(
                        String.format("%s에 해당하는 아이템이 존재하지 않습니다.", itemId),
                        ErrorCode.ITEM_NOT_FOUND));
    }

}
