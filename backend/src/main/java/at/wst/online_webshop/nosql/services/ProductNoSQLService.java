package at.wst.online_webshop.nosql.services;

import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.exceptions.ProductNotFoundException;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import at.wst.online_webshop.nosql.repositories.ProductNoSqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static at.wst.online_webshop.nosql.convertors.ProductConvertorNoSQL.convertDocumentToDTO;

@Service
public class ProductNoSQLService {
    private final ProductNoSqlRepository productNoSqlRepository;

    @Autowired
     public ProductNoSQLService(ProductNoSqlRepository productNoSqlRepository){
         this.productNoSqlRepository = productNoSqlRepository;
     }

     public List<ProductDTO> getAllProducts(){
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<ProductDocument> productDocuments = productNoSqlRepository.findAll();

        for(ProductDocument productDocument : productDocuments){
            ProductDTO productDTO = convertDocumentToDTO(productDocument);
            productDTOS.add(productDTO);
        }

        return productDTOS;
     }

     public ProductDTO getProduct(long productId){
        Optional<ProductDocument> optionalProductDocument = productNoSqlRepository.findById(String.valueOf(productId));

        if(optionalProductDocument.isPresent()){
            ProductDocument productDocument = optionalProductDocument.get();
            ProductDTO productDTO = convertDocumentToDTO(productDocument);
            return productDTO;
        }else{
            throw new ProductNotFoundException("Product not found");
        }
     }

}
