package at.wst.online_webshop.repositories;

import at.wst.online_webshop.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreetAndCityAndPostalCodeAndCountry(
            String street, String city, String postalCode, String country
    );
}