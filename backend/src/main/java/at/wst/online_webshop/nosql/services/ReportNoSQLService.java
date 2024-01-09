package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.nosql.dtos.ProductNoSqlDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportNoSQLService {
    /*
    public List<ProductNoSqlDTO> generateTopSellingProductsReport(double minPrice, double maxPrice, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("productPrice").gte(minPrice).lte(maxPrice)
                ),
                Aggregation.lookup("orderItems", "id", "product.id", "orderItems"),
                Aggregation.unwind("orderItems"),
                Aggregation.group("id", "productName", "productCategory", "productDescription", "productPrice", "productImageUrl", "vendor")
                        .count().as("totalSells")
                        .addToSet("vendor").as("vendors"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalSells")),
                Aggregation.limit(limit)
        );

        AggregationResults<ReportResult> result = mongoTemplate.aggregate(aggregation, ProductDocument.class, ReportResult.class);

        return result.getMappedResults();
    }
    */


}
