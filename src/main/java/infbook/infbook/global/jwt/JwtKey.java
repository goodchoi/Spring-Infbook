package infbook.infbook.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public class JwtKey {

    private static final Map<String, String> SECRET_KEY_MAP = Map.of(
            "key1","InfbookSpringBootJPAQueryDslSOauth2.0JWTSpringDATAJPA"
            ,"key2", "InfbookSOauth2JWTSpringDATAJPSpringBootJPAQueryDsl"
            ,"key3", "InfbookJPAQueryDslSSpringDATAJPAOauth2JWTSpringBootJPAQueryDsl"
    );
    private static final String[] SECRET_KEY_SET = SECRET_KEY_MAP.keySet().toArray(new String[0]);

    /**
     * 토큰 생성시 사용되는 시크릿키를 임의로 생성하기.
     */
    public static Pair<String, Key> getRandomKey() {
        String kid = SECRET_KEY_SET[new Random().nextInt(SECRET_KEY_SET.length)];
        String secretKey = SECRET_KEY_MAP.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * header에 명시된 kid 로 SecretKey 찾기
     *
     */
    public static Key getKey(String kid) {
        String findKey = SECRET_KEY_MAP.getOrDefault(kid, null);
        return findKey == null ? null : Keys.hmacShaKeyFor(findKey.getBytes(StandardCharsets.UTF_8));
    }
}
