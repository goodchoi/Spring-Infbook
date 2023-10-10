package infbook.infbook.global.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public abstract class OauthInfo {
    protected Map<String,Object> attributes;

    public OauthInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getEmail();
}
