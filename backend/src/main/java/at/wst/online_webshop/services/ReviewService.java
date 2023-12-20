package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.ReviewConvertor;
import at.wst.online_webshop.entities.Customer;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.repositories.OrderRepository;
import at.wst.online_webshop.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.entities.Review;
import at.wst.online_webshop.exceptions.FailedReviewException;
import at.wst.online_webshop.repositories.CustomerRepository;
import at.wst.online_webshop.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static at.wst.online_webshop.convertors.ReviewConvertor.convertToDto;


@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public ReviewDTO addReview(Long customerId, Long productId, String comment, int rating, Long orderId) {
        // Validate and get customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new FailedReviewException("Customer not found."));

        // Validate order
        if (orderId != null) {
            boolean orderExists = orderRepository.existsByOrderIdAndCustomer_CustomerIdAndProducts_ProductId(orderId, customerId, productId);
            if (!orderExists) {
                throw new FailedReviewException("No order found for this product and customer.");
            }
        }

        // Validate and get product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new FailedReviewException("Product not found."));

        // Validate rating and comment
        if (rating < 1 || rating > 5) {
            throw new FailedReviewException("Invalid rating. Must be between 1 and 5.");
        }

        if (comment == null || comment.isEmpty()) {
            throw new FailedReviewException("Comment cannot be empty or null.");
        }

        // Create and save the review
        Review review = new Review(product, customer, rating, comment);
        Review savedReview = reviewRepository.save(review);

        // Convert to DTO and return
        return convertToDto(savedReview);
    }

    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        // Validate and get product
        return productRepository.findById(productId)
                .map(product1 -> product1.getReviews().stream().map(ReviewConvertor::convertToDto).collect(Collectors.toList()))
                .orElseThrow(() -> new FailedReviewException("Product not found."));
    }

}

