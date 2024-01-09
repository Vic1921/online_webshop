package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderNoSqlRepository extends MongoRepository<OrderDocument, String> {
    List<OrderDocument> findByCustomerCustomerId(String customerId);

    @Query("{'customer.customerId': ?0, 'orderItems.product.id': ?1}")
    OrderDocument findByCustomerCustomerIdAndOrderItems_Product_ProductId(String customerId, String productId);
}
