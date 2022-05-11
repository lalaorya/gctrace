package com.hhj.gctrace.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author virtual
 * @Date 2022/5/11 20:22
 * @description：
 */
@Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        if (res.getHeader("Access-Control-Allow-Origin") == null) {
            // 携带cookie时，Origin不能用通配符*
            res.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin") == null ? "*" : req.getHeader("Origin"));
        }
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE, PATCH");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, Activity-Id, X-Token");
        if (RequestMethod.OPTIONS.name().equals(req.getMethod())) {
            res.setStatus(HttpStatus.OK.value());
            return ;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}

