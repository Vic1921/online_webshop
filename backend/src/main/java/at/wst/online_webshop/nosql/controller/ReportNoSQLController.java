package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.nosql.dtos.ProductNoSqlDTO;
import at.wst.online_webshop.nosql.dtos.ReviewNoSqlDTO;
import at.wst.online_webshop.nosql.services.ReportNoSQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/reports")
public class ReportNoSQLController {
    private final ReportNoSQLService reportNoSQLService;

    @Autowired
    public ReportNoSQLController(ReportNoSQLService reportNoSQLService) {

        this.reportNoSQLService = reportNoSQLService;
    }

    @GetMapping("/bestsellers")
    public List<ProductNoSqlDTO> getTopProductsBetweenPriceRangeReport(@RequestParam double minValue, @RequestParam double maxValue, @RequestParam int limit) {
        return reportNoSQLService.generateTopProductsBetweenPriceRangeReport(minValue, maxValue, limit);
    }

    @GetMapping("/top-reviewers")
    public List<ReviewNoSqlDTO> getTopReviewersReport(@RequestParam double price, @RequestParam int limit){
        return reportNoSQLService.generateTopReviewersReport(price, limit);
    }

}
