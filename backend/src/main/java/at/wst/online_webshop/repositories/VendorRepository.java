package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Transactional
    @Modifying
    @Query(value = "ALTER SEQUENCE vendor_id_seq RESTART WITH 1", nativeQuery = true)
    public void resetSequence();
}
