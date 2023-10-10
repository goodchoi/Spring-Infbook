package infbook.infbook.global.jwt;

/**
 * JWT에 대한 기본 설정값
 */
public class JwtProperties {
    public static final int JWT_EXPIRATION_TIME = 1800000; //유지 시간 : 30분
    public static final String JWT_COOKIE_NAME = "JWT_AUTH"; //저장되는 쿠키이름
}


