package infbook.infbook.domain.order.repository;

import infbook.infbook.domain.order.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
