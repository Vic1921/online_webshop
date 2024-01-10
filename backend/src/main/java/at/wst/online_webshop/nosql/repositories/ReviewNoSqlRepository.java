package at.wst.online_webshop.nosql.repositories;

import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.request.CustomerReviewAnalysis;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewNoSqlRepository extends MongoRepository<ReviewDocument, String> {
    @Aggregation(pipeline = {
            "{ $unwind: '$reviews' }",
            "{ $lookup: { from: 'products', localField: 'reviews.product.$id', foreignField: '_id', as: 'productDetails' } }",
            "{ $unwind: '$productDetails' }",
            "{ $match: { 'productDetails.price': { $gt: 50 } } }",
            "{ $group: { _id: '$customerId', customerName: { $first: '$name' }, reviews: { $push: { reviewId: '$reviews._id', rating: '$reviews.rating', comment: '$reviews.comment', reviewDate: '$reviews.date', productId: '$productDetails._id', productName: '$productDetails.name', productPrice: '$productDetails.price', productImageUrl: '$productDetails.imageUrl' } } } }",
            "{ $project: { _id: 0, customerId: '$_id', customerName: 1, reviews: { $slice: ['$reviews', 5] } } }",
            "{ $sort: { 'reviews.rating': -1 } }",
            "{ $limit: 5 }"
    })
    List<CustomerReviewAnalysis> getTopFiveCustomersByReviewRating();


}
