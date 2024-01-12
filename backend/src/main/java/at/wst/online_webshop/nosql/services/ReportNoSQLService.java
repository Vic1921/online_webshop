package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.nosql.dtos.ProductNoSqlDTO;
import at.wst.online_webshop.nosql.repositories.ReviewNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportNoSQLService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ReportNoSQLService.class);


    public List<ProductNoSqlDTO> generateTopProductsBetweenPriceRangeReport(double minPrice, double maxPrice, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("orderItems"),
                Aggregation.lookup("products", "orderItems.product.$id", "_id", "productDetails"),
                Aggregation.unwind("productDetails"),
                Aggregation.match(Criteria.where("productDetails.productPrice").gte(minPrice).lte(maxPrice)),
                Aggregation.group("orderItems.product")
                        .sum("orderItems.quantity").as("totalQuantitySold")
                        .first("productDetails.vendor.vendorName").as("vendorName")
                        .first("productDetails.productCategory").as("productCategory")
                        .first("productDetails.productDescription").as("productDescription")
                        .first("productDetails.productPrice").as("productPrice")
                        .first("productDetails.productName").as("productName")
                        .first("productDetails.productImageUrl").as("productImageUrl"),
                Aggregation.sort(Sort.Direction.DESC, ("totalQuantitySold")),
                Aggregation.limit(limit),   
                Aggregation.project("productName", "totalQuantitySold",
                        "productCategory", "productDescription", "productPrice", "productImageUrl", "vendorName")
        );

        List<ProductNoSqlDTO> topSellersReport = mongoTemplate.aggregate(aggregation, "orders", ProductNoSqlDTO.class)
                .getMappedResults();

        logger.info(topSellersReport.toString());

        return topSellersReport;
    }

    public List<ReviewDTO> generateTopReviewersReport(double price, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("reviews"),
                Aggregation.lookup("products", "reviews.product.$id", "_id", "productDetails"),
                Aggregation.unwind("productDetails"),
                Aggregation.match(Criteria.where("productDetails.price").gt(price)),
                Aggregation.sort(Sort.Direction.DESC, "reviews.rating"), // Sorting by review rating in descending order
                Aggregation.group("reviews._id")
                        .first("reviews.customerId").as("customerId")
                        .first("reviews.product.$id").as("productId")
                        .first("reviews.id").as("reviewId")
                        .first("name").as("customerName")
                        .first("reviews.date").as("reviewDate")
                        .first("reviews.rating").as("reviewRating")
                        .first("reviews.comment").as("reviewComment"),
                Aggregation.project(ReviewDTO.class)
                        .andInclude("reviewId", "customerId", "productId", "customerName", "reviewDate", "reviewRating", "reviewComment"),
                Aggregation.limit(limit)
        );


        List<ReviewDTO> topCustomerReviews = mongoTemplate.aggregate(aggregation, "customers", ReviewDTO.class)
                .getMappedResults();

        logger.info(topCustomerReviews.toString());

        return topCustomerReviews;
    }

}
