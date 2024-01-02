package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.ShoppingCartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingCartNoSqlRepository extends MongoRepository<ShoppingCartDocument, String> {
}
