package infbook.infbook.domain.order.controller;

import infbook.infbook.domain.member.repository.MemberRepository;
import infbook.infbook.domain.shoppingcart.service.ShoppingCartService;
import infbook.infbook.global.jwt.JwtPrincipal;
import infbook.infbook.domain.model.Address;
import infbook.infbook.domain.order.domain.Delivery;
import infbook.infbook.domain.order.domain.Order;
import infbook.infbook.domain.order.domain.Pay;
import infbook.infbook.domain.order.dto.OrderShoppingItemDto;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoApproveResponse;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoPayResponse;
import infbook.infbook.domain.order.service.KaKaoPayService;
import infbook.infbook.domain.order.service.OrderService;
import infbook.infbook.domain.shoppingcart.domain.ShoppingItem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@SessionAttributes({"oid", "tid"})
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final KaKaoPayService kaKaoPayService;
    private final MemberRepository memberRepository;

    @GetMapping("/order")
    public String getOrderForm(@AuthenticationPrincipal JwtPrincipal user, Model model, HttpServletRequest request) {

        if (shoppingCartService.getShoppingItemCount(user.getUserId()) < 1) {
            return "redirect:/cart";
        }
        List<OrderShoppingItemDto> findItem = orderService.getOrderRequestList(user.getUserId());
        model.addAttribute("memberAddress", Objects.requireNonNull(memberRepository.
                findById(user.getUserId()).orElse(null)).getAddress());
        model.addAttribute("itemList", findItem);
        return "shopping/order";
    }


    /**
     * 두가지 비지니스 로직이 요구됨.
     * 1. 장바구니에담긴 "구매가능한" 장바구니 - 상품에대해 주문 엔티티로 Insert 하고 목록을 반환.
     * 만약, 하나라도 만족하지않으면 빈 목록을 반환하고, 2번을 진행하지않고 반환결과를 return
     * 2. 1번이 정상 진행된 경우에대하여 카카오페이 API 결제 준비 로직을 수행.
     * 최종적으로
     * 정상흐름시 - 다음 수행할 url을 반환하게됨.
     * 1번의 예외경우시도 마찬가지.
     */
    @ResponseBody
    @PostMapping("/order")
    public String requestOrderAndPay(@RequestBody Address address,
                                     @AuthenticationPrincipal JwtPrincipal user, Model model
            , HttpServletRequest request
    ) {
        if ((shoppingCartService.getShoppingItemCount(user.getUserId()) < 1)) {
            return "no_cart";
        }
        Optional<Order> optioanlCreatedOrder = orderService.insertShoppingCartItemToOrder(user.getUserId(),address);

        if (optioanlCreatedOrder.isEmpty()) {
            return "insufficent_stock";
        }

        Order createdOrder = optioanlCreatedOrder.get();
        KaKaoApproveResponse kaKaoApproveResponse = kaKaoPayService.requestPayApprove(createdOrder);

        model.addAttribute("tid", kaKaoApproveResponse.getTid());
        model.addAttribute("oid", String.valueOf(createdOrder.getId()));

        return kaKaoApproveResponse.getNext_redirect_pc_url();
    }

    @GetMapping("/order/pay")
    public String requestPay(@AuthenticationPrincipal JwtPrincipal user,
                             @RequestParam("pg_token") String pg_token, @ModelAttribute("tid") String tid
            , @ModelAttribute("oid") String oid
            , @ModelAttribute("address") Address address, SessionStatus sessionStatus, HttpServletRequest request
            , RedirectAttributes redirectAttributes
    ) {

        KaKaoPayResponse kaKaoPayResponse = kaKaoPayService.requestPay(pg_token, tid, oid);

        orderService.completeOrderRequest(oid, kaKaoPayResponse, user.getUserId());

        shoppingCartService.removeShoppingItemCountCache(user.getUserId());

        redirectAttributes.addFlashAttribute("pay_check", "true");
        //주문완료시 주문완료 페이지로 리다이렉트 되는데 주문이 성공적으로 끝난경우를 표시하기위함. 이 표시가 없는 채로 주문완료 페이지를 요청하면
        //에러페이지로 이동함.
        return "redirect:/order/complete";
    }

    @GetMapping("/order/complete")
    public String completeOrder(Model model) {
        if (!model.containsAttribute("pay_check")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 접근");
        }
        return "shopping/orderComplete";
    }
}
