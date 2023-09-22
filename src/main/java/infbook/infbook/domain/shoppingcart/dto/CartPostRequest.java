package infbook.infbook.domain.shoppingcart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartPostRequest {

    private Long memberId ;
    @NotNull
    private Long itemId;
    @NotNull
    private Integer requestQuantity;
}
