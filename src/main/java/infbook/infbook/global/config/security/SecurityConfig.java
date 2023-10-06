package infbook.infbook.global.config.security;

import infbook.infbook.global.jwt.JwtAuthenticationFilter;
import infbook.infbook.global.jwt.JwtAuthorizationFilter;
import infbook.infbook.global.jwt.JwtProperties;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.LazyCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                .requestMatchers("/book/**", "/", "/item/**", "/error", "/error-page/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/member/login")
                .loginProcessingUrl("/login")
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
                .csrf()
                .ignoringRequestMatchers("/member/check/**","/cart")
                .and()
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
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
