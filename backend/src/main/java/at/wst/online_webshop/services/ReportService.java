package at.wst.online_webshop.services;

import at.wst.online_webshop.convertors.ProductConvertor;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.repositories.OrderItemRepository;
import at.wst.online_webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    /**
     * Generates a report highlighting the top 10 best-selling products between a specific price range.
     * The report provides insights into the most purchased products, including their total quantity sold.
     *
     * Entities Used:
     * - Products
     * - Customer
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
    public List<ProductDTO> generateTop10ProductsReportsFromTo(double minPrice, double maxPrice){
        //filter by price
        List<Product> allProducts = productService.getProductsInPriceRange(minPrice, maxPrice);

        List<ProductDTO> productDTOReports = new ArrayList<>();

        if(!allProducts.isEmpty() && allProducts != null){
            for(Product product : allProducts){
                int totalQuantitySold = productService.getTotalQuantitySold(product);

                product.setProductTotalSells(totalQuantitySold);

                ProductDTO productDTO = ProductConvertor.convertToDto(product);
                productDTOReports.add(productDTO);
            }
        }

        //sorted by total quantity sold (descending)
        productDTOReports.sort(Comparator.comparingInt(ProductDTO::getProductTotalSells).reversed());

        //10 most purchased products
        return productDTOReports.stream().limit(10).collect(Collectors.toList());
    }
}
