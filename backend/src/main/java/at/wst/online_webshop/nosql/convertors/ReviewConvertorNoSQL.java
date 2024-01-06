package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.dtos.ReviewDTO;
import at.wst.online_webshop.nosql.documents.ReviewDocument;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewDTO convertDocumentToDTO(ReviewDocument review) {
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        return reviewDTO;
    }

    public static List<ReviewDTO> convertDocumentToDtoList(List<ReviewDocument> reviewDocuments) {
        return reviewDocuments.stream()
                .map(ReviewConvertorNoSQL::convertDocumentToDTO)
                .collect(Collectors.toList());
    }

}
