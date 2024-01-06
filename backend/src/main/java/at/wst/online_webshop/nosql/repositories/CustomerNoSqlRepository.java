package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerNoSqlRepository extends MongoRepository<CustomerDocument, String> {
    Optional<CustomerDocument> findByEmail(String email);

    @Query("{'reviews.product.id': ?0}")
    List<CustomerDocument> findByReviews_Product_Id(String productId);

}
