package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerNoSqlRepository extends MongoRepository<CustomerDocument, String> {
}
