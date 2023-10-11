package infbook.infbook.domain.shoppingcart.controller;

import infbook.infbook.domain.shoppingcart.dto.CartPostRequest;
import infbook.infbook.domain.shoppingcart.dto.CartItemDto;
import infbook.infbook.domain.shoppingcart.dto.CartPutRequest;
import infbook.infbook.domain.shoppingcart.dto.CartPutResponse;
import infbook.infbook.domain.shoppingcart.service.ShoppingCartService;
import infbook.infbook.global.jwt.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping(value = "/cart")
    public String getShoppingCart(@AuthenticationPrincipal JwtPrincipal user, Model model) {

        List<CartItemDto> shoppingItemList = shoppingCartService.getShoppingCartListByLoginUser(user.getUserId());
        model.addAttribute("itemList", shoppingItemList);

        return "shopping/shoppingcart";
    }

    @ResponseBody
    @PostMapping(value = "/cart")
    public String ajaxPostItemToCart(@AuthenticationPrincipal JwtPrincipal user,
                                     @RequestBody CartPostRequest cartPostRequest, HttpServletRequest request) {

        cartPostRequest.setMemberId(user.getUserId());

        String result = shoppingCartService.addShoppingCartItem(cartPostRequest);
        if (result.equals("1")) { //장바구니에 새로 추가하는 경우 기존 의 장바구니 개수를 다시 갱신하기 위해 세션 초기화
            shoppingCartService.removeShoppingItemCountCache(user.getUserId());
        }

        return result;
    }

    @ResponseBody
    @PutMapping(value = "/cart")
    public CartPutResponse ajaxPutItemInCart(@RequestBody CartPutRequest cartPutRequest) {
        return shoppingCartService.modifyShoppingCartItem(cartPutRequest);
    }

    @ResponseBody
    @DeleteMapping(value = "/cart")
    public String ajaxDeleteItemInCart(@RequestBody Map<String, String> param, HttpServletRequest request,@AuthenticationPrincipal JwtPrincipal user) {
        shoppingCartService.deleteShoppingItem(UUID.fromString(param.get("spItemId")),user.getUserId());
        shoppingCartService.removeShoppingItemCountCache(user.getUserId());
        return "ok";
    }
}
