package infbook.infbook.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderShoppingItemDto {

    private String name;

    private String fileName;

    private Integer price;

    private Integer stock;
    private Integer quantity;

    @QueryProjection
    public OrderShoppingItemDto(String name, String fileName, Integer price, Integer stock, Integer quantity) {
        this.name = name;
        this.fileName = fileName;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
    }
}
