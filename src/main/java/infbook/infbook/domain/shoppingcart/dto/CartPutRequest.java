package infbook.infbook.domain.shoppingcart.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CartPutRequest {

    @NotNull
    private UUID shoppingItemId;
    @NotNull
    private Integer requestQuantity;

}
