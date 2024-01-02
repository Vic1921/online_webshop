package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.AddressDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressNoSqlRepository extends MongoRepository<AddressDocument, String> {
}
