package infbook.infbook.global.config.interceptor;

import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import infbook.infbook.domain.shoppingcart.service.ShoppingCartService;
import infbook.infbook.global.jwt.JwtPrincipal;
import infbook.infbook.global.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class ShoppingCartInterceptor implements HandlerInterceptor {

    private final ShoppingCartService shoppingCartService;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (Objects.isNull(authentication) || (authentication.getPrincipal().toString().equals("anonymousUser"))) {
//            //log.info("미로그인 유저");
//            return HandlerInterceptor.super.preHandle(request, response, handler);
//        }
//
//        if (Objects.isNull(request.getSession().getAttribute("shopping_cart_count"))) {
//            //log.info("로그인했지만 장바구니 정보 없음 계산중");
//            JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
//            Long memberId = principal.getUserId();
//            request.getSession().setAttribute("shopping_cart_count"
//                    , shoppingItemRepository.countShoppingItemByMember(memberId));
//            return HandlerInterceptor.super.preHandle(request, response, handler);
//        }
//        //log.info("장바구니정보 가지고 있음");
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && request.getMethod().equals("GET")) {
            if (authentication.getPrincipal() instanceof JwtPrincipal) {
                JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
                int shoppingItemCount = shoppingCartService.getShoppingItemCount(principal.getUserId());
                modelAndView.addObject("shopping_count",shoppingItemCount);
            }
        }
    }
}
