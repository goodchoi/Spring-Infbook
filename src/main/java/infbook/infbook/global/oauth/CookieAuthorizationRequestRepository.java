package infbook.infbook.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static infbook.infbook.global.util.CookieUtil.*;

@Component
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {
    public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_request";
    public static final String REDIRECT_URI_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return getCookie(request,OAUTH2_AUTH_REQUEST_COOKIE_NAME)
                .map(cookie -> deserialize(cookie,OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null) {
            removeAuthorizationRequestCookies(request,response);
            return;
        }
        addCookie(response, REDIRECT_URI_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_COOKIE_NAME);

        if (!ObjectUtils.isEmpty(redirectUriAfterLogin)) {
            addCookie(response, REDIRECT_URI_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request,response,OAUTH2_AUTH_REQUEST_COOKIE_NAME);
        deleteCookie(request,response,REDIRECT_URI_COOKIE_NAME);
    }
}
