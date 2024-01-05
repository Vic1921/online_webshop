package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderNoSqlRepository extends MongoRepository<OrderDocument, String> {
    List<OrderDocument> findByCustomerCustomerId(String customerId);
}
