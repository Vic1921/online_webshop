package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerNoSqlRepository extends MongoRepository<CustomerDocument, String> {
    Optional<CustomerDocument> findByEmail(String email);
}
