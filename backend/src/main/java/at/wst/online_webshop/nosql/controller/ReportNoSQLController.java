package at.wst.online_webshop.nosql.controller;

import at.wst.online_webshop.controller.ReportController;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.nosql.dtos.ProductNoSqlDTO;
import at.wst.online_webshop.nosql.services.ReportNoSQLService;
import at.wst.online_webshop.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    @Autowired
    public ReportNoSQLController(ReportNoSQLService reportNoSQLService) {

        this.reportNoSQLService = reportNoSQLService;
    }

    @GetMapping("/bestsellers")
    public List<ProductNoSqlDTO> getTopProductsBetweenPriceRangeReport(@RequestParam double minValue, @RequestParam double maxValue, @RequestParam int limit) {
        return reportNoSQLService.generateTopProductsBetweenPriceRangeReport(minValue, maxValue, limit);
    }

    @GetMapping("/top-reviewers")
    public List<ReviewDTO> getTopReviewersReport(@RequestParam double price, @RequestParam int limit){
        return reportNoSQLService.generateTopReviewersReport(price, limit);
    }

}
