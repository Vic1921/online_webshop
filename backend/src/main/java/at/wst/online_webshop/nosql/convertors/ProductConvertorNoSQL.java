package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.CustomerDTO;
import at.wst.online_webshop.dtos.ProductDTO;
import at.wst.online_webshop.nosql.documents.CustomerDocument;
import at.wst.online_webshop.nosql.documents.ProductDocument;
import org.modelmapper.ModelMapper;

public class ProductConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO convertDocumentToDTO(ProductDocument productDocument) {
        try {
            ProductDTO productDTO =  modelMapper.map(productDocument, ProductDTO.class);

            return productDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting JSON to ProductDTO", e);
        }
    }
}
