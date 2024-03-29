package at.wst.online_webshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Column(name = "order_payment", nullable = false)
    private String orderPayment;

    @Column(name = "order_shippingdetails", nullable = false)
    private String orderShippingDetails;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @Size(min = 1)
    private List<OrderItem> orderItems;

    public Order(String orderDate, double totalPrice, Customer customer, List<OrderItem> orderItems, String orderPayment, String orderShippingDetails) {
        this.orderDate = orderDate;
        this.orderTotalMount = totalPrice;
        this.customer = customer;
        this.orderItems = orderItems;
        this.orderPayment = orderPayment;
        this.orderShippingDetails = orderShippingDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", orderTotalMount=" + orderTotalMount +
                ", customer=" + customer +
                ", orderItems=" + orderItems +
                '}';
    }
}
