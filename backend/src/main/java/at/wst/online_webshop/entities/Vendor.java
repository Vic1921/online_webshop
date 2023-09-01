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
@Table(name = "vendors")
public class Vendor {
    @Id
    @Column(name = "vendor_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "vendor_address", nullable = false)
    private String vendorAddress;
}

