package at.wst.online_webshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    @Column(name = "product_imageurl", nullable = false)
    private String productImageUrl;


    //JsonIgnore to handle the bidirectional relationship between reviews and products, because there is infinite recursion during serialization
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    public Product(String name, String productDescription, String productCategory, double price, String sku, int quantity, String productImageUrl, Vendor vendor) {
        this.productName = name;
        this.productPrice = price;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productSKU = sku;
        this.productImageUrl = productImageUrl;
        this.productQuantity = quantity;
        this.reviews = new ArrayList<>();
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productSKU='" + productSKU + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productQuantity=" + productQuantity +
                ", productImageUrl='" + productImageUrl + '\'' +
                '}';
    }
}
