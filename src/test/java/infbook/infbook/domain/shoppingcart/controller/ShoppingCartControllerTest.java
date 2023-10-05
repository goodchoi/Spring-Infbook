package infbook.infbook.domain.shoppingcart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import infbook.infbook.abstractTest.ControllerTest;
import infbook.infbook.global.jwt.JwtProperties;
import infbook.infbook.global.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ShoppingCartControllerTest extends ControllerTest {

    /**
     * ajax로 장바구니에 상품 추가 요청시 발생하는 에러에 대한 검증.
     * (1). 찾으려는 상품이 없는 경우 예외 발생하여 ControllerAdvice에 의해 에러 결과를  Json형식으로 반환.
     */
    @Test
    void ajaxAddItemToCart_AjaxItemNotFoundException() throws Exception {

        String token = JwtUtils.createToken(savedMember);
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME,token);

        mockMvc.perform(post("/cart")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTestCartAddRequestJson("1", "1"))
                        .cookie(cookie)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("AJX003")); //Item
    }

    private String getTestCartAddRequestJson(String itemId, String requestQuantity) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("requestQuantity", requestQuantity);
        return objectMapper.writeValueAsString(map);
    }


}