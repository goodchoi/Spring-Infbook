package infbook.infbook.domain.order.domain;

import infbook.infbook.domain.model.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @Builder
    public Delivery(DeliveryStatus deliveryStatus, Address address) {
        this.deliveryStatus = deliveryStatus;
        this.address = address;
    }
}
