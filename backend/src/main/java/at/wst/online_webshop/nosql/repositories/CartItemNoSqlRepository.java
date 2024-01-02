package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.CartItemDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemNoSqlRepository extends MongoRepository<CartItemDocument, String> {
}
