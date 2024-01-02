package at.wst.online_webshop.nosql.documents;

import javax.persistence.Id;

public class AddressDocument {
    @Id
    private Long addressId;
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
