package at.wst.online_webshop.nosql.documents;

import at.wst.online_webshop.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorDocument {
    private String vendorId;
    private String vendorName;
    private String vendorAddress;
}
