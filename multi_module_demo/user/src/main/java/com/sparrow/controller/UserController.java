package com.sparrow.controller;

import com.sparrow.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjianchun
 * @create 2018/4/13
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, @RequestParam("name") String name){
        request.setAttribute("name", name);
        return "user_index";
    }

    @RequestMapping(value = "userInfo", method = RequestMethod.POST)
    public String userInfo(HttpServletRequest request, User user){
        request.setAttribute("user", user);
        request.setAttribute("name", user.getName());
        return "user_index";
    }

}