package at.wst.online_webshop.controller;


import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.dtos.ReviewDTO;
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
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/bestsellers")
    public List<ProductDTO> getTopProductsBetweenPriceRangeReport(@RequestParam double minValue, @RequestParam double maxValue, @RequestParam int limit){
        return reportService.generateTopProductsBetweenPriceRangeReport(minValue, maxValue, limit);

    }

    @GetMapping("/top-reviewers")
    public List<ReviewDTO> getTopReviewersReport(@RequestParam double price, @RequestParam int limit){
        return reportService.generateTopReviewersReport(price, limit);
    }


}
