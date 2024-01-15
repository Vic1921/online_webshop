package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.nosql.dtos.ProductNoSqlDTO;
import at.wst.online_webshop.nosql.dtos.ReviewNoSqlDTO;
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
                Aggregation.group("orderItems.product.$id")
                        .sum("orderItems.quantity").as("totalQuantitySold"),
                Aggregation.sort(Sort.Direction.DESC, "totalQuantitySold"),
                Aggregation.limit(limit),
                Aggregation.lookup("products", "_id", "_id", "productDetails"),
                Aggregation.unwind("productDetails"),
                Aggregation.match(Criteria.where("productDetails.productPrice").gte(minPrice).lte(maxPrice)),
                Aggregation.project("productDetails.productName", "totalQuantitySold",
                        "productDetails.vendor.vendorName", "productDetails.productCategory",
                        "productDetails.productDescription", "productDetails.productPrice",
                        "productDetails.productImageUrl"),
                Aggregation.project().andExclude("_id")
        );

        List<ProductNoSqlDTO> topSellersReport = mongoTemplate.aggregate(aggregation, "orders", ProductNoSqlDTO.class)
                .getMappedResults();

        logger.info(topSellersReport.toString());

        return topSellersReport;
    }


    public List<ReviewNoSqlDTO> generateTopReviewersReport(double price, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("reviews", "reviews .$id", "_id", "reviewObjects"),
                        Aggregation.unwind("reviewObjects"),
                        Aggregation.lookup("products", "reviewObjects.product", "_id", "productDetails"),
                        Aggregation.unwind("productDetails"),
                        Aggregation.match(Criteria.where("productDetails.productPrice").gt(price)),
                        Aggregation.sort(Sort.by(Sort.Direction.DESC, "reviewObjects.reviewRating")),
                        Aggregation.group("reviewObjects._id")
                                .first("reviewObjects._id").as("reviewId")
                                .first("reviewObjects.reviewRating").as("reviewRating")
                                .first("reviewObjects.reviewComment").as("reviewComment")
                                .first("reviewObjects.reviewDate").as("reviewDate")
                                .first("productDetails._id").as("productId"),
                        Aggregation.project()
                                .andExclude("_id")
                                .andInclude("reviewId", "productId", "reviewDate", "reviewRating", "reviewComment"),
                        Aggregation.limit(limit)
                );


        List<ReviewNoSqlDTO> topCustomerReviews = mongoTemplate.aggregate(aggregation, "customers", ReviewNoSqlDTO.class)
                .getMappedResults();

        logger.info(topCustomerReviews.toString());

        return topCustomerReviews;
    }

}
