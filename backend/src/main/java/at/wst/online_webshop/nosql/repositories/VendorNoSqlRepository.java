package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.VendorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorNoSqlRepository extends MongoRepository<VendorDocument, String> {
}
