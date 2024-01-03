package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.OrderDocument;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductNoSqlRepository extends MongoRepository<ProductDocument, String> {
}
