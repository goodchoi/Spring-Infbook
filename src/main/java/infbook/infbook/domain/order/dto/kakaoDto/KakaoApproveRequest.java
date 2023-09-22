package infbook.infbook.domain.order.dto.kakaoDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoApproveRequest {

    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private Integer total_amount;
    private Integer tax_free_amount;
    private String approval_url;
    private String cancel_url;
    private String fail_url;

    @Builder
    public KakaoApproveRequest(String cid, String partner_order_id, String partner_user_id, String item_name, String item_code, Integer quantity, Integer total_amount, Integer tax_free_amount, String approval_url, String cancel_url, String fail_url) {
        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.item_code = item_code;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.tax_free_amount = tax_free_amount;
        this.approval_url = approval_url;
        this.cancel_url = cancel_url;
        this.fail_url = fail_url;
    }
}
