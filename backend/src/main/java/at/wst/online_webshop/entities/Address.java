package at.wst.online_webshop.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country",  nullable = false)
    private String country;

    @OneToMany(mappedBy = "address")
    private List<Customer> customers;

    public Address(String street, String city, String postalCode, String country, List<Customer> customers) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.customers = customers;
    }
}
