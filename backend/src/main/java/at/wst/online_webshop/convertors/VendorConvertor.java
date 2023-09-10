package at.wst.online_webshop.convertors;

import at.wst.online_webshop.dtos.VendorDTO;
import at.wst.online_webshop.entities.Vendor;
import org.modelmapper.ModelMapper;

public class VendorConvertor {

    private static ModelMapper modelMapper = new ModelMapper();

    public static VendorDTO convertToDto(Vendor vendor) {
        VendorDTO vendorDTO = modelMapper.map(vendor, VendorDTO.class);
        return vendorDTO;
    }

    public static Vendor convertToEntity(VendorDTO vendorDTO) {
        Vendor vendor = modelMapper.map(vendorDTO, Vendor.class);
        return vendor;
    }
}
