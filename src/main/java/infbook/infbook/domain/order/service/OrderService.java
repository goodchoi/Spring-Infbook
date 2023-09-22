package infbook.infbook.domain.order.service;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.order.domain.*;
import infbook.infbook.domain.order.dto.OrderShoppingItemDto;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoPayResponse;
import infbook.infbook.domain.order.repository.DeliveryRepository;
import infbook.infbook.domain.order.repository.OrderItemRepository;
import infbook.infbook.domain.order.repository.OrderRepository;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import infbook.infbook.domain.order.repository.PayRepository;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingItemRepository shoppingItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final PayRepository payRepository;
    private final MemberRepository memberRepository;

    public List<OrderShoppingItemDto> getOrderRequestList(Long memberId) {
        return shoppingItemRepository.findOrderShoppingItemByMemberId(memberId);
    }

    public Optional<Order> insertShoppingCartItemToOrder(Long memberId, Address address) {
        List<ShoppingItem> shoppingItem = shoppingItemRepository.findShoppingItemListByMemberId(memberId);
        if (shoppingItem.stream().anyMatch((si) -> si.getQuantity() > si.getItem().getStockQuantity())) {
            return Optional.empty();
        }

        Delivery newDelivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.PREPAY)
                .address(address)
                .build();

        Order newOrder = Order.createInitialOrder(memberRepository.getReferenceById(memberId),newDelivery);

        List<OrderItem> orderItems = shoppingItemToOrderItem(newOrder, shoppingItem);

        orderRepository.save(newOrder);
        orderItemRepository.saveAll(orderItems);
        deliveryRepository.save(newDelivery);
        return Optional.of(newOrder);
    }

    private List<OrderItem> shoppingItemToOrderItem(Order order, List<ShoppingItem> shoppingItem) {
        return shoppingItem.stream().map(si -> {
                    OrderItem newOrderItem = OrderItem.builder()
                            .item(si.getItem())
                            .quantity(si.getQuantity())
                            .order(order)
                            .build();
                    order.addOrderItem(newOrderItem);
                    return newOrderItem;
                })
                .toList();
    }

    public void completeOrderRequest(String oid, KaKaoPayResponse kaKaoPayResponse,Long memberId) {
        Order order = orderRepository.findById(Long.parseLong(oid)).orElseThrow(() -> new RuntimeException());
        Pay createdPay = Pay.builder().
                id(kaKaoPayResponse.getTid())
                .total_amount(kaKaoPayResponse.getAmount().getTotal())
                .paid_time(kaKaoPayResponse.getApproved_at())
                .requested_time(kaKaoPayResponse.getCreated_at())
                .order(order)
                .build();
        order.completedPayment();
        payRepository.save(createdPay);

        shoppingItemRepository.deleteAllInShoppingCart(memberId);
    }

    private List<Long> transletShoppingItemCode(String shoppingItemCodes){
        return Arrays.stream(shoppingItemCodes.split(","))
                .map(Long::parseLong)
                .toList();
    }
}
