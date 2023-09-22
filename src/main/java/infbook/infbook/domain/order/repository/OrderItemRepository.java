package infbook.infbook.domain.order.repository;

import infbook.infbook.domain.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
