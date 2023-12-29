package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.repositories.OrderItemRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Generates a report highlighting the top 10 best-selling products between a specific price range.
     * The report provides insights into the top-selling products within the specified price range
     * and includes details about the vendors associated with those products.
     *
     * Entities Used:
     * - Products
     * - Vendor
     * - Order
     *
     * Report Specifications:
     * - Filtered by: Price range (Minimum Price - Maximum Price)
     * - Sorted by: Total quantity sold (e.g., last year)
     *
     * @param minPrice The minimum price for product filtering.
     * @param maxPrice The maximum price for product filtering.
     * @return A list of ProductDTOs representing the top 10 most purchased products.
     */
    public List<ProductDTO> generateTopProductsBetweenPriceRangeReport(double minPrice, double maxPrice, int limit) {
        Query query = entityManager.createNativeQuery(
                "SELECT p.product_id, p.product_name, p.product_category, p.product_description, p.product_price, p.product_imageurl, p.product_total_sells, " +
                        "v.vendor_id, v.vendor_name " +
                        "FROM products p " +
                        "JOIN order_items oi ON p.product_id = oi.product_id " +
                        "JOIN orders o ON o.order_id = oi.order_id " +
                        "JOIN vendors v ON v.vendor_id = p.vendor_id " +
                        "WHERE p.product_price BETWEEN :minPrice AND :maxPrice " +
                        "GROUP BY p.product_id, p.product_name,p.product_category, p.product_description, p.product_price, p.product_imageurl, p.product_total_sells, v.vendor_id, v.vendor_name " +
                        "ORDER BY p.product_total_sells DESC " +
                        "LIMIT :limit");

        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        query.setParameter("limit", limit);

        List<Object[]> result = query.getResultList();
        List<ProductDTO> productDTOList = new ArrayList<>();


        for(Object[] row : result){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(((BigInteger) row[0]).longValue());
            productDTO.setProductName((String) row[1]);
            productDTO.setProductCategory((String) row[2]);
            productDTO.setProductDescription((String) row[3]);
            productDTO.setProductPrice((Double) row[4]);
            productDTO.setProductImageUrl((String) row[5]);
            productDTO.setProductTotalSells((Integer) row[6]);
            productDTO.setVendorName((String) row[8]);

            productDTOList.add(productDTO);
        }

        return productDTOList;
    }
}
