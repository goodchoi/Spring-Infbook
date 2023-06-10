package infbook.infbook.domain.order.domain;

import infbook.infbook.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private int totalPrice;

    private LocalDate orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public Order(Member member, Delivery delivery, int totalPrice, LocalDate orderDate, OrderStatus orderStatus) {
        this.member = member;
        this.delivery = delivery;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
