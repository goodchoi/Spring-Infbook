package infbook.infbook.domain.order.domain;

import infbook.infbook.domain.model.Address;
import infbook.infbook.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;


    @Embedded
    private Address address;

    @Builder
    public Delivery(DeliveryStatus deliveryStatus, Address address) {
        this.deliveryStatus = deliveryStatus;
        this.address = address;
    }

    public void completedPayment() {
        this.deliveryStatus = DeliveryStatus.READY;
    }
}
