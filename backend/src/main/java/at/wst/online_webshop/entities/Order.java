package at.wst.online_webshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "order_date", nullable = false)
    private String orderDate;

    @Column(name = "order_total_mount", nullable = false)
    private Double orderTotalMount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Order(Date orderDate, double totalPrice, Customer customer, List<OrderItem> orderItems) {
        this.orderDate = orderDate.toString();
        this.orderTotalMount = totalPrice;
        this.customer = customer;
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", orderTotalMount=" + orderTotalMount +
                ", customer=" + customer +
                '}';
    }
}
