package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderIdAndCustomer_CustomerIdAndOrderItems_Product_ProductId(Long orderId, Long customerId, Long productId);
    List<Order> findByCustomer_CustomerId(Long customerId);

}
