package infbook.infbook.domain.order.repository;

import infbook.infbook.domain.order.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay,String> {
}
