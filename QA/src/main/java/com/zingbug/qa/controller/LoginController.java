package com.zingbug.qa.controller;

import com.sun.deploy.net.HttpResponse;
import com.zingbug.qa.serviec.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登陆注册
 * Created by ZingBug on 2019/6/30.
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam(value = "password") String password,
                      @RequestParam(value = "username") String username,
                      @RequestParam(value = "next") String next,
                      @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                      HttpServletResponse response)
    {
        try {
            Map<String,String> map=userService.register(password,username);
            if(map.containsKey("ticket"))
            {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme)
                {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";//返回首页
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }
        catch (Exception e)
        {
            log.error("注册异常"+e.getMessage());
            return "login";
        }
    }
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "next") String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response)
    {
        try {
            Map<String,String> map=userService.login(password,username);
            if(map.containsKey("ticket"))
            {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme)
                {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";//返回首页
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }
        catch (Exception e)
        {
            log.error("登陆异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/logpage"},method = {RequestMethod.GET})
    public String regloginPage(Model model,@RequestParam(value = "next",required = false) String next)
    {
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket)
    {
        userService.logout(ticket);
        return "redirect:/";
    }


}
