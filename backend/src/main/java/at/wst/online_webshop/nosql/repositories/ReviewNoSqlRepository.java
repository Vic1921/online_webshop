package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.ReviewDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewNoSqlRepository extends MongoRepository<ReviewDocument, String> {
    @Query(value = "{ 'product.id' : ?0 }")
    List<ReviewDocument> findByProductId(String productId);
}
