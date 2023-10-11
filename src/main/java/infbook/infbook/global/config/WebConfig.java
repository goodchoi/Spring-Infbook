package infbook.infbook.global.config;

import infbook.infbook.domain.shoppingcart.repository.ShoppingItemRepository;
import infbook.infbook.domain.shoppingcart.service.ShoppingCartService;
import infbook.infbook.global.config.interceptor.CategoryInterceptor;
import infbook.infbook.global.config.interceptor.ShoppingCartInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ShoppingCartService shoppingCartService;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ShoppingCartInterceptor(shoppingCartService))
                .excludePathPatterns("/admin/**","/css/**","/js/**","/img/**","/webfonts/**","/error-page/**")
                .order(1);
//        registry.addInterceptor(new CategoryInterceptor())
//                .excludePathPatterns("/admin/**","/css/**","/js/**","/img/**","/webfonts/**","/error","/error-page/**","/cart/**");
    }
}
