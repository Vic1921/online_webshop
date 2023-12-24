package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.entities.Review;
import org.modelmapper.ModelMapper;

public class ReviewConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewDTO convertToDto(Review review) {
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        reviewDTO.setCustomerName(review.getCustomer().getName());
        return reviewDTO;
    }

    public static Review convertToEntity(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        return review;
    }
}
