package infbook.infbook.global.oauth;

import java.util.Map;

public class KakaoAuthInfo extends OauthInfo {

    private Long id;
    public KakaoAuthInfo(Map<String, Object> attributes) {
        super((Map<String, Object>)attributes.get("kakao_account"));

        this.id = (Long) attributes.get("id");
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}

