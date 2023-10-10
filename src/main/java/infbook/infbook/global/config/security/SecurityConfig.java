package infbook.infbook.global.config.security;

import infbook.infbook.global.jwt.JwtAuthenticationFilter;
import infbook.infbook.global.jwt.JwtAuthorizationFilter;
import infbook.infbook.global.jwt.JwtProperties;
import infbook.infbook.global.oauth.CustomOAuth2UserService;
import infbook.infbook.global.oauth.handler.OAuth2AuthenticationFailureHandler;
import infbook.infbook.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import static infbook.infbook.global.jwt.JwtProperties.*;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webfonts/**", "/error/**");

    }
    //시큐리티 설정 적용 하지않을 곳 커스텀.(인증,인가 x)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomFailureHandler handler, CustomSuccessHandler successHandler, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return http.
                authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/member/**").hasAnyRole("ANONYMOUS","ADMIN")
                .requestMatchers("/book/**", "/", "/item/**", "/error", "/error-page/**","/oauth2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/member/login")
                .loginProcessingUrl("/login")
                .failureHandler(handler)
                .successHandler(successHandler)
                .and()
                .logout()
                .clearAuthentication(true)
                .deleteCookies(JWT_COOKIE_NAME)
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .requestCache((cache) -> cache
                        .requestCache(requestCache))
                .csrf()
                .ignoringRequestMatchers("/**")
                .and()
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")
                //.authorizationRequestRepository()
                .and()
                .redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager,handler), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .getOrBuild();

//        AuthenticationManager authenticationManager = admin.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(bCryptPasswordEncoder())
//                .and()
//                .build();

    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(HttpSecurity httpSecurity) throws Exception {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter("/member/login_prc");
//        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(httpSecurity));
//        jwtAuthenticationFilter.setAuthenticationSuccessHandler(customSuccessHandler);
//        jwtAuthenticationFilter.setAuthenticationFailureHandler(customFailureHandler);
//        return jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(bCryptPasswordEncoder())
//                .and()
//                .build();
//    }


}
