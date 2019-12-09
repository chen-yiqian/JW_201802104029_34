package com.example.demo.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Daomain.User;
import com.example.demo.Service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@RestController
public class LoginController {
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void post(@RequestParam(value = "username", required = false) String username,
                     @RequestParam(value = "password", required = false) String password,
                     HttpServletRequest request, HttpServletResponse response)
            throws JSONException, IOException {
        JSONObject message = new JSONObject();
        try {
            User LoggedUser = UserService.getInstance().login(username, password);
            if (LoggedUser != null) {
                message.put("message", "登录成功");
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(10 * 60);
                session.setAttribute("currentUser", LoggedUser);
                response.getWriter().println(message);
                return;
            } else {
                message.put("message", "用户名或密码错误");
                response.getWriter().println(message);
                return;
            }
        } catch (
                SQLException e) {
            message.put("message", "数据库异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "其他异常");
            e.printStackTrace();
        }
        response.getWriter().println(message);
    }
}
