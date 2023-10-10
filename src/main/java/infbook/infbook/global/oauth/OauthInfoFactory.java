package infbook.infbook.global.oauth;

import infbook.infbook.domain.member.domain.Oauth2Provider;

import java.util.Map;

public class OauthInfoFactory {
    public static OauthInfo getOauthInfo(Oauth2Provider oauth2Provider, Map<String, Object> attributes) {
        switch (oauth2Provider) {
            case KAKAO -> {
                return new KakaoAuthInfo(attributes);
            }
            default -> throw new IllegalArgumentException("잘못된 oauth2Provider type 입니다.");
        }
    }
}
