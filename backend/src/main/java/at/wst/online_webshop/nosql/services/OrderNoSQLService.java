package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.OrderDTO;
import at.wst.online_webshop.nosql.documents.OrderDocument;
import at.wst.online_webshop.nosql.repositories.OrderNoSqlRepository;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static at.wst.online_webshop.nosql.convertors.OrderConvertorNoSQL.convertDocumentToDtoList;


@Service
public class OrderNoSQLService {

    private static final Logger logger = LoggerFactory.getLogger(OrderNoSQLService.class);
    private final OrderNoSqlRepository orderNoSqlRepository;
    private final ProductNoSqlRepository productNoSqlRepository;


    @Autowired
    public OrderNoSQLService(ProductNoSqlRepository productNoSqlRepository, OrderNoSqlRepository orderNoSqlRepository) {
        this.orderNoSqlRepository = orderNoSqlRepository;
        this.productNoSqlRepository = productNoSqlRepository;
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<OrderDocument> orders = this.orderNoSqlRepository.findByCustomerCustomerId(String.valueOf(customerId));
        List<OrderDTO> orderDTOS = convertDocumentToDtoList(orders);
        for (OrderDTO orderDTO : orderDTOS) {
            logger.info("Order DTO details: {}", orderDTO.toString());
        }
        return orderDTOS;
    }

}
