package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderIdAndCustomer_CustomerIdAndProducts_ProductId(Long orderId, Long customerId, Long productId);
}
