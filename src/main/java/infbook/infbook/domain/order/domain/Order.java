package infbook.infbook.domain.order.domain;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false,updatable = false)
    private int totalPrice;

    @Column(nullable = false,updatable = false,columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime orderDate;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Builder
    public Order(Member member, int totalPrice, LocalDateTime orderDate, Delivery delivery, List<OrderItem> orderItems, OrderStatus orderStatus) {
        this.member = member;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.delivery = delivery;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }


    public static Order createInitialOrder(Member member,Delivery delivery){
        return Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.WAITING)
                .delivery(delivery)
                .orderItems(new ArrayList<>())
                .totalPrice(0)
                .build();
    }

    public void plusTotalPrice(int add){
        this.totalPrice += add;
    }

    public void addOrderItem(OrderItem newOrderItem) {
        this.orderItems.add(newOrderItem);
        plusTotalPrice(newOrderItem.getQuantity() * newOrderItem.getItem().getPrice());
    }

    public void completedPayment(){
        this.orderStatus = OrderStatus.SUCCESS;
        this.delivery.completedPayment();
    }
}
