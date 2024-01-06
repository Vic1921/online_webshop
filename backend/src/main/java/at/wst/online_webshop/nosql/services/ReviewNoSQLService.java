package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.exceptions.FailedReviewException;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ReviewNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static at.wst.online_webshop.nosql.convertors.ReviewConvertorNoSQL.convertDocumentToDtoList;

@Service
public class ReviewNoSQLService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewNoSQLService.class);
    private final ReviewNoSqlRepository reviewNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;

    @Autowired
    public ReviewNoSQLService(ReviewNoSqlRepository reviewNoSqlRepository, ProductNoSqlRepository productNoSqlRepository) {
        this.reviewNoSqlRepository = reviewNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
    }

    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        if (!productNoSqlRepository.findById(String.valueOf(productId)).isPresent()) {
            throw new FailedReviewException("Product not found.");
        }
        ProductDocument productDocument = productNoSqlRepository.findById(String.valueOf(productId)).get();
        logger.info(productDocument.toString());
        List<ReviewDocument> reviewDocuments = productDocument.getReviews();
        List<ReviewDTO> reviewDTOS = convertDocumentToDtoList(reviewDocuments);
        logger.info(reviewDocuments.toString());
        logger.info(reviewDTOS.toString());
        return reviewDTOS;
    }
}

