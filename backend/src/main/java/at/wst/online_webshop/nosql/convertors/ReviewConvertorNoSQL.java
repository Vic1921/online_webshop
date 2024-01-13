package at.wst.online_webshop.nosql.convertors;

import at.wst.online_webshop.nosql.documents.ReviewDocument;
import at.wst.online_webshop.nosql.dtos.ReviewNoSqlDTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConvertorNoSQL {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewNoSqlDTO convertDocumentToDTO(ReviewDocument review) {
        ReviewNoSqlDTO reviewDTO = modelMapper.map(review, ReviewNoSqlDTO.class);
        return reviewDTO;
    }

    public static List<ReviewNoSqlDTO> convertDocumentToDtoList(List<ReviewDocument> reviewDocuments) {
        return reviewDocuments.stream()
                .map(ReviewConvertorNoSQL::convertDocumentToDTO)
                .collect(Collectors.toList());
    }

}
