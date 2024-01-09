package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ReviewNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ReviewNoSQLService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewNoSQLService.class);
    private final ReviewNoSqlRepository reviewNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final MongoTemplate mongoTemplate;


    @Autowired
    public ReviewNoSQLService(MongoTemplate mongoTemplate, CustomerNoSqlRepository customerNoSqlRepository, ReviewNoSqlRepository reviewNoSqlRepository, ProductNoSqlRepository productNoSqlRepository) {
        this.reviewNoSqlRepository = reviewNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<ReviewDTO> getReviewsWithCustomerByProductId(String productId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("product.$id").is(productId)),
                lookup("products", "product.$id", "_id", "product"),
                unwind("product"),
                lookup("customers", "customer.customerId", "customerId", "customer"),
                unwind("customer"),
                project("reviewRating", "reviewComment", "reviewDate")
                        .and("product.productName").as("productName")
                        .and("customer.name").as("customerName")
        );

        logger.info(mongoTemplate.aggregate(aggregation, "reviews", ReviewDocument.class)
                .getMappedResults().toString());

        AggregationResults<ReviewDTO> aggregationResults = mongoTemplate.aggregate(aggregation, "reviews", ReviewDTO.class);

        List<ReviewDTO> result = aggregationResults.getMappedResults();

        return result;

    }


}

