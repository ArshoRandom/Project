package com.management.demo.interceptor;


import com.management.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TimeCheckInterceptor implements HandlerInterceptor {


    private int requestsCounts;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> log = new HashMap<>();
        requestsCounts++;
        log.put("request_time", System.currentTimeMillis());
        log.put("request_URL", request.getRequestURL());
        log.put("request_counts", requestsCounts);
        request.setAttribute("LOG", log);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, Object> log = (Map<String, Object>) request.getAttribute("LOG");
        long respTime = System.currentTimeMillis();
        log.put("response_time", respTime);
        log.put("request_duration", respTime - (Long) log.get("request_time"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Map<String, Object> logs = (Map<String, Object>) request.getAttribute("LOG");
        long completionTime = System.currentTimeMillis();
        logs.put("completion_duration", completionTime - (Long) logs.get("request_time"));
        log.info(JsonUtil.serialize(logs));
    }
}
