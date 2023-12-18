package at.wst.online_webshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VendorDTO {

        private Long vendorId;
        private String vendorName;
        private String vendorAddress;
        private List<Long> vendorProducts;
}
