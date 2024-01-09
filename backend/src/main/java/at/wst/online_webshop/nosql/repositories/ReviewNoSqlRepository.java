package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.request.CustomerReviewAnalysis;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewNoSqlRepository extends MongoRepository<ReviewDocument, String> {
    @Aggregation(pipeline = {
            "{ $match: { _class: 'at.wst.online_webshop.nosql.documents' } }",
            "{ $project: { customerId: '$_id', name: 1, reviews: 1 } }",
            "{ $unwind: '$reviews' }",
            "{ $lookup: { from: 'productDocuments', localField: 'reviews.productId', foreignField: '_id', as: 'productDetails' } }",
            // Unwind product details as the lookup result is an array
            "{ $unwind: '$productDetails' }",
            // Match to filter reviews for products over 50 EUR
            "{ $match: { 'productDetails.price': { $gt: 50 } } }",
            // Group by customerId and count their reviews
            "{ $group: { _id: '$customerId', name: { $first: '$name' }, reviews: { $push: { review: '$reviews', product: '$productDetails' } } } }",
            // Project to structure the output document
            "{ $project: { _id: 0, customerId: '$_id', customerName: '$name', reviews: { $slice: ['$reviews', 5] } } }",
            // Sort by the size of the reviews array in descending order to get customers with most reviews
            "{ $sort: { 'reviews.rating': -1 } }",
            // Limit to top five customers
            "{ $limit: 5 }"
    })
    List<CustomerReviewAnalysis> getTopFiveCustomersByReviewRating();


}
