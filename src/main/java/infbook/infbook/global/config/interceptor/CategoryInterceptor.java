package infbook.infbook.global.config.interceptor;

import infbook.infbook.global.util.CategoryUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Arrays;


//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//@Profile("dev")
public class CategoryInterceptor implements HandlerInterceptor {

//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (response.getStatus() == 200) {
//            modelAndView.addObject("categories", CategoryUtil.getCategoryList());
//        }
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
}
