package infbook.infbook.domain.shoppingcart.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CartItemDto {

    private Long id;

    private String name;

    private String publisher;

    private String fileName;

    private LocalDate publicationDate;

    private Integer price;

    private Integer quantity;

    private UUID spItemId;

    private Integer stock;

    @QueryProjection
    public CartItemDto(Long id, String name, String publisher, String fileName,
                       LocalDate publicationDate, Integer price, Integer quantity, UUID spItemId,Integer stock) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.fileName = fileName;
        this.publicationDate = publicationDate;
        this.price = price;
        this.quantity = quantity;
        this.spItemId = spItemId;
        this.stock = stock;
    }
}
