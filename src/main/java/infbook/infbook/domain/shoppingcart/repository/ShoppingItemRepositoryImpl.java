package infbook.infbook.domain.shoppingcart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import infbook.infbook.domain.order.dto.OrderShoppingItemDto;
import infbook.infbook.domain.order.dto.QOrderShoppingItemDto;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;
import infbook.infbook.domain.shoppingcart.dto.QCartItemDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static infbook.infbook.domain.item.domain.QItem.*;
import static infbook.infbook.domain.member.domain.QMember.*;
import static infbook.infbook.domain.shoppingcart.domain.QShoppingCart.*;
import static infbook.infbook.domain.shoppingcart.domain.QShoppingItem.*;

public class ShoppingItemRepositoryImpl implements CustomShoppingItemRepository {

    private final JPAQueryFactory queryFactory;


    public ShoppingItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartItemDto> findShoppingItemByMemberId(Long id) {

        return queryFactory.select(
                        new QCartItemDto(
                                item.id, item.name, item.publisher, item.fileName, item.publicationDate
                                , item.price, shoppingItem.quantity, shoppingItem.id, item.stockQuantity
                        ))
                .from(shoppingCart)
                .join(shoppingCart.member, member).on(member.id.eq(id))
                .join(shoppingCart.shoppingItems, shoppingItem)
                .join(shoppingItem.item, item)
                .fetch();
    }

    @Override
    public List<OrderShoppingItemDto> findOrderShoppingItemByMemberId(Long id) {
        return queryFactory.select(
                        new QOrderShoppingItemDto(
                                item.name, item.fileName, item.price, item.stockQuantity,
                                shoppingItem.quantity
                        ))
                .from(shoppingCart)
                .join(shoppingCart.member, member).on(member.id.eq(id))
                .join(shoppingCart.shoppingItems, shoppingItem)
                .join(shoppingItem.item, item)
                .fetch();
    }
}
