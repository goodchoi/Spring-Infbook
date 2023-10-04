package infbook.infbook.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            //cookie에서 Token을 꺼낸다.
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception igonoring) {

        }
        if (token != null) {
            try {
                Authentication authentication = this.getUserNamePAsswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                //실패시 쿠키 초기화
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * 쿠키에서 꺼낸 JWT를 가지고 AuthenticationToken을 생성한다.
     */
    private Authentication getUserNamePAsswordAuthenticationToken(String token) {
        Long userId= JwtUtils.getUserId(token);
        String role = JwtUtils.getRole(token);

        if (userId != null && role != null ) {
            return new UsernamePasswordAuthenticationToken(
                    new JwtPrincipal(token,userId)
                    ,null
                    , List.of(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }


}
