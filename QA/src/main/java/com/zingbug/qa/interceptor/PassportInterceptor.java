package com.zingbug.qa.interceptor;

import com.sun.org.apache.regexp.internal.RE;
import com.zingbug.qa.model.HostHolder;
import com.zingbug.qa.pojo.LoginTicket;
import com.zingbug.qa.pojo.User;
import com.zingbug.qa.serviec.LoginTicketService;
import com.zingbug.qa.serviec.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by ZingBug on 2019/6/30.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket=null;
        if(request.getCookies()!=null)
        {
            for(Cookie cookie:request.getCookies())
            {
                if(cookie.getName().equals("ticket"))
                {
                    ticket=cookie.getValue();
                    break;
                }
            }
        }

        if(ticket!=null)
        {
            LoginTicket loginTicket=loginTicketService.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0)
            {
                return true;
            }
            User user=userService.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null)
        {
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
