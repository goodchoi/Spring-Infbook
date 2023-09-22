package infbook.infbook.domain.order.domain;

import infbook.infbook.domain.order.dto.kakaoDto.KaKaoPayResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pay {
    @Id
    @Column(nullable = false,updatable = false)
    private String id;
    @Column(nullable = false,updatable = false)
    private Integer total_amount;

    @Column(nullable = false,updatable = false)
    private LocalDateTime requested_time;

    @Column(nullable = false,updatable = false)
    private LocalDateTime paid_time;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Pay(String id, Integer total_amount, LocalDateTime requested_time, LocalDateTime paid_time,Order order) {
        this.id = id;
        this.total_amount = total_amount;
        this.requested_time = requested_time;
        this.paid_time = paid_time;
        this.order = order;
    }


}
