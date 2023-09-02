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
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_SKU", nullable = false)
    private String productSKU;

    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;

    @OneToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    public Product(String name, double price, String sku, int quantity, Vendor vendor) {
        this.productName = name;
        this.productPrice = price;
        this.productSKU = sku;
        this.productQuantity = quantity;
        this.vendor = vendor;
    }
}
