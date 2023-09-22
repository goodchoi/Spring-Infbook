package infbook.infbook.domain.order.repository;

import infbook.infbook.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select ord from Order ord join fetch Delivery del where ord.Id =: orderId")
    Optional<Order> findOrderJoinFetchDeliveryByOrderID(@Param(value = "orderId") Long orderId);

}
