package infbook.infbook.global.config.security;

import infbook.infbook.domain.member.service.CustomUserDetailService;
import infbook.infbook.global.jwt.JwtAuthenticationFilter;
import infbook.infbook.global.jwt.JwtAuthorizationFilter;
import infbook.infbook.global.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomFailureHandler customFailureHandler;
    private final CustomSuccessHandler customSuccessHandler;
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**","/js/**","/img/**","/webfonts/**","/error");

    }
    //시큐리티 설정 적용 하지않을 곳 커스텀.(인증,인가 x)


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomFailureHandler handler ,CustomSuccessHandler successHandler) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        return http.
                httpBasic().disable().
                authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/member/**", "/book/**","/","/item/**","/error","/error-page/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login_prc")
                .failureHandler(handler)
                .successHandler(successHandler)
                .and()
                .logout()
                .deleteCookies(JwtProperties.COOKIE_NAME)
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .requestCache((cache) -> cache
                        .requestCache(requestCache))
                .csrf().ignoringRequestMatchers("/**")
                //.headers().frameOptions().disable()
                .and()
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(http),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter("/member/login_prc");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(httpSecurity));
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(customSuccessHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customFailureHandler);
        return jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

}
