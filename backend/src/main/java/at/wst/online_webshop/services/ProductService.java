package at.wst.online_webshop.services;

import at.wst.online_webshop.entities.Product;
import at.wst.online_webshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional
    public List<Product> getAllProducts(){
        System.out.println(productRepository.findAll());
        return this.productRepository.findAll();
    }
}
