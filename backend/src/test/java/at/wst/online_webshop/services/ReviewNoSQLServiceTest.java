package at.wst.online_webshop.services;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.nosql.repositories.CustomerNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ReviewNoSqlRepository;
import at.wst.online_webshop.nosql.services.ReviewNoSQLService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ReviewNoSQLServiceTest {

    @Autowired
    private ReviewNoSQLService reviewNoSQLService;
    @MockBean
    private CustomerNoSqlRepository mockCustomerRepository;
    @MockBean
    private ProductNoSqlRepository mockProductRepository;
    @MockBean
    private ReviewNoSqlRepository mockReviewRepository;
    @MockBean
    private MongoTemplate mockMongoTemplate;

    @Test
    public void testAddReview() {
        String customerId = "123123";
        String productId = "100213";
        String comment = "Great product!";
        int rating = 5;

        Mockito.when(mockProductRepository.existsById(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockCustomerRepository.existsById(Mockito.anyString())).thenReturn(true);

        ReviewDTO result = reviewNoSQLService.addReview(customerId, productId, comment, rating);

        assertNotNull(result);
        assertEquals(comment, result.getReviewComment());
        assertEquals(Integer.valueOf(rating), result.getReviewRating());
        Mockito.verify(mockReviewRepository, Mockito.times(1)).save(Mockito.any());
    }
}
