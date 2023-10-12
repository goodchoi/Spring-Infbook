package infbook.infbook.domain.order.service;

import infbook.infbook.domain.order.domain.Order;
import infbook.infbook.domain.order.domain.OrderItem;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoApproveResponse;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoPayResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpHeaders.*;

@Slf4j
@Service
public class KaKaoPayService {

    @Value("${project.url}")
    private String url;

    private final WebClient webClient;

    public KaKaoPayService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com/v1/payment")
                .defaultHeader(AUTHORIZATION, "KakaoAK d2128e2c4f21efb2ae5ddf9a4bf91f52")
                .defaultHeader(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate().build();
    }

    public KaKaoApproveResponse requestPayApprove(Order order) {

        MultiValueMap<String, String> kaKaoApproveRequest = createKaKaoApproveRequest(order);

        Mono<KaKaoApproveResponse> kaKaoApproveResponseMono = webClient.post()
                .uri("/ready")
                .body(BodyInserters.fromFormData(kaKaoApproveRequest))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() != HttpStatus.OK) {
                        return clientResponse
                                .createException()
                                .flatMap(error -> {
                                    log.warn("예외 내용 = {} ", error.getResponseBodyAs(String.class));
                                    return Mono.error(new RuntimeException("카카오페이 준비 요청 중 API 예외 발생") {
                                    });
                                });
                    }
                    return clientResponse.bodyToMono(KaKaoApproveResponse.class);
                });
        return kaKaoApproveResponseMono.block();
    }

    public KaKaoPayResponse requestPay(String pg_token, String tid, String oid) {
        MultiValueMap<String, String> kaKaoPayRequest = createKaKaoPayRequest(tid, pg_token, oid);
        log.info("request = {}", kaKaoPayRequest);
        Mono<KaKaoPayResponse> kaKaoPayResponseMono = webClient.post()
                .uri("/approve")
                .body(BodyInserters.fromFormData(kaKaoPayRequest))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() != HttpStatus.OK) {
                        return clientResponse
                                .createException()
                                .flatMap(error -> {
                                    log.warn("예외 내용 = {} ", error.getResponseBodyAs(String.class));
                                    return Mono.error(new RuntimeException("카카오페이 결제 요청 중 API 예외 발생") {
                                    });
                                });
                    }
                    return clientResponse.bodyToMono(KaKaoPayResponse.class);
                });

        return kaKaoPayResponseMono.block();
    }

    private MultiValueMap<String, String> createKaKaoPayRequest(String tid, String pg_token, String oid) {
        MultiValueMap<String, String> vo = new LinkedMultiValueMap<>();
        vo.add("cid", "TC0ONETIME");
        vo.add("tid", tid);
        vo.add("partner_order_id", oid);
        vo.add("partner_user_id", "infbook");
        vo.add("pg_token", pg_token);

        return vo;
    }

    private MultiValueMap<String, String> createKaKaoApproveRequest(Order order) {
        MultiValueMap<String, String> vo = new LinkedMultiValueMap<>();
        vo.add("cid", "TC0ONETIME");
        vo.add("partner_order_id", String.valueOf(order.getId()));
        vo.add("partner_user_id", "infbook");
        vo.add("item_name", buildItemNames(order.getOrderItems()));
        vo.add("item_code", buildItemCodes(order.getOrderItems()));
        vo.add("quantity", sumQuantity(order.getOrderItems()));
        vo.add("total_amount", String.valueOf(order.getTotalPrice()));
        vo.add("tax_free_amount", "0");
        vo.add("approval_url", url + "/order/pay");
        vo.add("cancel_url", url + "/order/cart");
        vo.add("fail_url", url + "/order/cart");

        return vo;
    }

    private String sumQuantity(List<OrderItem> orderItems) {
        return String.valueOf(orderItems.stream().mapToInt(OrderItem::getQuantity).sum());
    }

    private String buildItemCodes(List<OrderItem> orderItems) {
        return orderItems.stream().map(oi -> String.valueOf(oi.getId())).collect(joining(","));
    }

    private String buildItemNames(List<OrderItem> orderItems) {
        String represent = orderItems.get(0).getItem().getName();

        if (orderItems.size() > 1) {
            represent += " 외 " + (orderItems.size() - 1) + "권";
        }
        return represent;
    }
}
