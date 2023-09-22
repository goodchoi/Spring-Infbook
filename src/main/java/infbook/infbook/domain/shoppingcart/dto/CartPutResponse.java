package infbook.infbook.domain.shoppingcart.dto;

import lombok.Data;

@Data
public class CartPutResponse {
    private String result;
    private Integer itemQuantity;

    public CartPutResponse(String result, Integer itemQuantity) {
        this.result = result;
        this.itemQuantity = itemQuantity;
    }

    public static CartPutResponse  successPutResponse() {
        return new CartPutResponse("success",null);
    }

    public static CartPutResponse  outOfStockPutResponse() {
        return new CartPutResponse("out_of_stock",null);
    }

    public static CartPutResponse  insufficientStockPutResponse(Integer stockQuantity) {
        return new CartPutResponse("insufficient",stockQuantity);
    }
}
