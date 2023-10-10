package infbook.infbook.global.jwt;

import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.global.config.security.CustomFailureHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

import static infbook.infbook.global.jwt.JwtProperties.*;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,CustomFailureHandler customFailureHandler) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        setAuthenticationFailureHandler(customFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        // 로그인할 때 입력한 username과 password를 가지고 authenticationToken를 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
                new ArrayList<>()
        );

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();
        Member findMember = principal.getMember();
        String token = JwtUtils.createToken(findMember);
        //쿠키 생성
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setMaxAge(JWT_EXPIRATION_TIME);
        cookie.setPath("/");
        response.addCookie(cookie);
        super.successfulAuthentication(request,response,chain,authResult);
    }


}
