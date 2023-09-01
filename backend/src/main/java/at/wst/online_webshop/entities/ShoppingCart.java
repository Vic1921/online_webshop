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
@Table(name = "cart_ids")
public class ShoppingCart {
    @Id
    @Column(name = "cart_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne(mappedBy = "shoppingCart")
    private Customer customer;

    @OneToMany(mappedBy = "shoppingCart")
    private List<Product> products;
}
