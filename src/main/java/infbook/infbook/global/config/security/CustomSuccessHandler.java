package infbook.infbook.global.config.security;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import infbook.infbook.global.jwt.JwtPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ShoppingItemRepository shoppingItemRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공 핸들러 적용되는거맞냐?");
        if (authentication.getAuthorities().stream()
                .anyMatch(e -> e.getAuthority().equals("ROLE_ADMIN"))) {
            log.info("로그인 성공 핸들러 적용되는거맞냐? 1트");

            setDefaultTargetUrl("/admin/");
        } else {
            log.info("로그인 성공 핸들러 적용되는거맞냐? 2트");

            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            request.getSession().setAttribute("shopping_cart_count"
                    ,shoppingItemRepository.countShoppingItemByMember(principal.getMember().getId()));
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
