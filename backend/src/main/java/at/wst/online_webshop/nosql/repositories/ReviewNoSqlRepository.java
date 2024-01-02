package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewNoSqlRepository extends MongoRepository<OrderDocument, String> {
}
