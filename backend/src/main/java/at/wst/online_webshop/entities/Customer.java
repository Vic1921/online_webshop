package at.wst.online_webshop.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}