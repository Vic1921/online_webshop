package at.wst.online_webshop.nosql.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AddressDocument {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public AddressDocument(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
}
