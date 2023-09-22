package infbook.infbook.domain.order.service;

import infbook.infbook.domain.order.domain.Order;
import infbook.infbook.domain.order.domain.OrderItem;
import infbook.infbook.domain.order.dto.kakaoDto.KaKaoApproveResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@Slf4j
class KaKaoPayServiceTest {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com/v1/payment")
            .defaultHeader(AUTHORIZATION, "KakaoAK d2128e2c4f21efb2ae5ddf9a4bf91f52")
            .defaultHeader(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build()
            .mutate().build();


    @Test
    void requestPayApprove() {

        MultiValueMap<String, String> kaKaoApproveRequest = createKaKaoApproveRequest();
        log.info("vo ={}", kaKaoApproveRequest);


        Mono<KaKaoApproveResponse> stringMono = webClient.post()
                .uri("/ready")
                .body(BodyInserters.fromFormData(kaKaoApproveRequest))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() != HttpStatus.OK) {
                        return clientResponse
                                .createException()
                                .flatMap(error -> {
                                    log.warn("카카오페이 API 예외 status {} ,내용 {}",error.getStatusCode(),error.getResponseBodyAs(String.class));
                                    return Mono.error(new RuntimeException("카카오페이 API 예외 발생") {
                                    });
                                });
                    }

                    return clientResponse.bodyToMono(KaKaoApproveResponse.class);
                });
        KaKaoApproveResponse block = stringMono.doOnError(throwable -> log.error("카카오페이 예외발생={}",throwable.getMessage())).block();
        log.info("======================result={}", block);

    }

    private MultiValueMap<String, String> createKaKaoApproveRequest() {
        MultiValueMap<String, String> vo = new LinkedMultiValueMap<>();
        vo.add("cid", "TC0ONETIME");
        vo.add("partner_order_id", "1");
        vo.add("partner_user_id", "infbook");
        vo.add("item_name", "자바의 정석 외 3권");
        vo.add("item_code", "100,200,300,400");
        vo.add("quantity", "5");
        vo.add("total_amount", "10000");
        vo.add("tax_free_amount", "0");
        vo.add("approval_url", "http://localhost:8080/");
        vo.add("cancel_url", "http://localhost:8080/");
        vo.add("fail_url", "http://localhost:8080/");

        return vo;
    }


}