package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductPriceBetween(double minPrice, double maxPrice);

}
