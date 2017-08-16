package com.nowcoder.interceptor;

import com.nowcoder.model.HostHolder;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 激活拦截器。要求激活账号
 * Created by albert on 2017/8/10.
 */
@Component
public class UnActiveInterceptor implements HandlerInterceptor{
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JedisUtil jedisUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser() != null) {
            String URI = httpServletRequest.getRequestURI();
            if (URI != null && !URI.matches("^/index.*")
                    && !URI.matches("^/") && !URI.matches("^/reglogin.*") &&
                    !URI.matches("^/unactive.*") && !URI.matches("^/active.*")) {


                if (jedisUtil.Sismenber(RedisKeyUtil.getUnactiveKey(), String.valueOf(hostHolder.getUser().getId())))
                    httpServletResponse.sendRedirect("/unactive/"+hostHolder.getUser().getId());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
