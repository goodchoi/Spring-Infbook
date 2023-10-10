package infbook.infbook.global.oauth.handler;

import infbook.infbook.domain.member.domain.UserLevel;
import infbook.infbook.domain.member.dto.CustomUserDetails;
import infbook.infbook.global.jwt.JwtProperties;
import infbook.infbook.global.jwt.JwtUtils;
import infbook.infbook.global.oauth.CookieAuthorizationRequestRepository;
import infbook.infbook.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static infbook.infbook.global.jwt.JwtProperties.*;
import static infbook.infbook.global.oauth.CookieAuthorizationRequestRepository.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${project.url}")
    private String redirectUrl;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String validatedTargetUrl = getTargetUrl(request, response, authentication);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, validatedTargetUrl);
    }

    private String getTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUriInCookie = CookieUtil.getCookie(request, REDIRECT_URI_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUriInCookie.isPresent() && !isCheckedRedirectUri(redirectUriInCookie.get())) {
            throw new IllegalArgumentException("redirect URI가 올바르지 않습니다. 쿠키에 저장된 URI : " + redirectUriInCookie);
        }
        String targetUrl = redirectUriInCookie.orElse(getDefaultTargetUrl());

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        if(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(UserLevel.ROLE_ANONYMOUS.toString()))) {
            targetUrl = redirectUrl + "member/moreInfo";
        }
        String jwt = JwtUtils.createToken(principal.getMember());
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, jwt);
        cookie.setMaxAge(JWT_EXPIRATION_TIME);
        cookie.setPath("/");
        response.addCookie(cookie);
        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isCheckedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUrl);

        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort();
    }
}
