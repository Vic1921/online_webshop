package at.wst.online_webshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name= "order_item_quantity")
    private int orderItemQuantity;

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", product=" + product +
                ", orderItemQuantity=" + orderItemQuantity +
                '}';
    }

    public OrderItem(Order order, Product product, int orderItemQuantity) {
        this.order = order;
        this.product = product;
        this.orderItemQuantity = orderItemQuantity;
    }
}
