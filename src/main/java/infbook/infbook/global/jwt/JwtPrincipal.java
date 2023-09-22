package infbook.infbook.global.jwt;

import infbook.infbook.domain.model.Address;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class JwtPrincipal {
    private final String token;
    private final Long userId;

    public JwtPrincipal(String token, Long userId) {
        this.token = token;
        this.userId = userId;

    }
}
