package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
