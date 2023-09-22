package infbook.infbook.domain.shoppingcart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import infbook.infbook.abstractUtils.ControllerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ShoppingCartControllerTest extends ControllerUtil {



    /**
     * ajax로 장바구니에 상품 추가 요청시 발생하는 에러에 대한 검증.
     * (1). 찾으려는 상품이 없는 경우 예외 발생하여 ControllerAdvice에 의해 에러 결과를  Json형식으로 반환.
     */
    @Test
    @WithUserDetails(value = savedMemberId, userDetailsServiceBeanName = "customUserDetailService"
            , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void ajaxAddItemToCart_AjaxItemNotFoundException() throws Exception {

        mockMvc.perform(post("/cart")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTestCartAddRequestJson("1", "1"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AJX003")) //Item
                .andDo(print());
    }

    private String getTestCartAddRequestJson(String itemId, String requestQuantity) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("requestQuantity", requestQuantity);
        return objectMapper.writeValueAsString(map);
    }


}