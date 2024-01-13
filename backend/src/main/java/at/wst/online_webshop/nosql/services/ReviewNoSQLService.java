package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.exceptions.FailedReviewException;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.dtos.ReviewNoSqlDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static at.wst.online_webshop.nosql.convertors.ReviewConvertorNoSQL.convertDocumentToDTO;
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

    public List<ReviewNoSqlDTO> getReviewsWithCustomerByProductId(String productId) {
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

        AggregationResults<ReviewNoSqlDTO> aggregationResults = mongoTemplate.aggregate(aggregation, "reviews", ReviewNoSqlDTO.class);

        List<ReviewNoSqlDTO> result = aggregationResults.getMappedResults();

        return result;

    }

    public ReviewNoSqlDTO addReview(String customerId, String productId, String comment, int rating) {
        // Validate existence of customer and product
        boolean customerExists = customerNoSqlRepository.existsById(customerId);
        boolean productExists = productNoSqlRepository.existsById(productId);

        if (!customerExists) {
            throw new FailedReviewException("Customer not found.");
        }
        if (!productExists) {
            throw new FailedReviewException("Product not found.");
        }

        // Validate rating and comment
        if (rating < 1 || rating > 5) {
            throw new FailedReviewException("Invalid rating. Must be between 1 and 5.");
        }
        if (comment == null || comment.isEmpty()) {
            throw new FailedReviewException("Comment cannot be empty or null.");
        }

        logger.info("Adding review for product: " + productId);
        // get the ProductDocument after confirming it exists
        ProductDocument product = productNoSqlRepository.findById(productId).orElseThrow(() -> new FailedReviewException("Product could not be retrieved: " + productId));

        // Create and save the review
        ReviewDocument review = new ReviewDocument();
        review.setReviewRating(rating);
        review.setReviewComment(comment);

        logger.info("Setting customer: " + customerId);
        // Format date time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentDateTime.format(formatter);
        review.setReviewDate(LocalDate.parse(formattedDateTime));

        logger.info("Setting product2222: " + productId);
        // Set the product at last
        review.setProduct(product);

        logger.info("Saving review: " + review.toString());
        ReviewDocument savedReview = reviewNoSqlRepository.save(review);

        logger.info("Saved review: " + savedReview.toString());

        // Convert to DTO and return
        return convertDocumentToDTO(savedReview);
    }


}

