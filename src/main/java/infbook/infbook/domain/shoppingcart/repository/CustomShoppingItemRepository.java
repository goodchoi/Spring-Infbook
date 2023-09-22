package infbook.infbook.domain.shoppingcart.repository;

import infbook.infbook.domain.order.dto.OrderShoppingItemDto;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;

import java.util.List;

public interface CustomShoppingItemRepository {

    List<CartItemDto> findShoppingItemByMemberId(Long id);
    List<OrderShoppingItemDto> findOrderShoppingItemByMemberId(Long id);
}
