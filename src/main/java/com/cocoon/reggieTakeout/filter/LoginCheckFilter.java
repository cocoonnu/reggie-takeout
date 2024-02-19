package com.cocoon.reggieTakeout.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径字符串匹配对象
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        // 不需要拦截的请求路径
        String[] urls = new String[]{
                "/employee/login", // 登录退出登录两个接口
                "/employee/logout",
                "/backend/**", // 静态页面资源
                "/front/**"
        };

        // 如果请求路径不需要拦截则放行
        if (checkoutRequestURI(requestURI, urls)) {
            filterChain.doFilter(request,response);
            return;
        }

        // 如果用户存在session登录状态则放行
        if (request.getSession().getAttribute(GlobalConstant.EMPLOYEE_ID) != null) {
            filterChain.doFilter(request,response);
            return;
        }

        // 否则的话一律响应为用户未登录
        response.getWriter().write(JSON.toJSONString(GlobalResult.error("用户未登录"), SerializerFeature.BrowserCompatible));
    }

    public static boolean checkoutRequestURI(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) return true;
        }
        return false;
    }
}
