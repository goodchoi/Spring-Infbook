package infbook.infbook.global.jwt;


import infbook.infbook.domain.member.domain.Member;
import infbook.infbook.domain.model.Address;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

/**
 * JWT와 관련된 모든 유틸메서드를 정의
 */
public class JwtUtils {

    /**
     * 토큰에서 userId 찾기.
     */
    public static Long getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }
    /**
     * 토큰에서 userRole 찾기
     */
    public static String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * member로 토큰을 생성한다.
     * 토큰의 구조
     * -Header : alg, kid
     * -PayLoad : userId,role, iat:발행시간 , exp:만료시간
     * -Signature : JwtKey.getRandomKey() 로 구한 임의의 SecretKey를 HS512로 암호화
     */
    public static String createToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("userId",member.getId());
        claims.put("role",member.getUserLevel());
        Date now = new Date();
        Pair<String, Key> key = JwtKey.getRandomKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }


}
