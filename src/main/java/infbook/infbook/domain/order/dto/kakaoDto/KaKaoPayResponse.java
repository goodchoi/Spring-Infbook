package infbook.infbook.domain.order.dto.kakaoDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KaKaoPayResponse {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private CardInfo cardInfo;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private String payload;

    @Data
    public static class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    private static class CardInfo {
        private String purchase_corp;
        private String purchase_corp_code;
        private String issuer_corp;
        private String issuer_corp_code;
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
        private String bin;
        private String card_type;
        private String install_month;
        private String approved_id;
        private String card_mid;
        private String interest_free_install;
        private String card_item_code;

    }
}
