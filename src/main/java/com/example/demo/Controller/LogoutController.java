package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LogoutController {
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public JSONObject logout(HttpServletRequest request){
        JSONObject message=new JSONObject();
        HttpSession session=request.getSession();
        if(session != null) {
            //使会话失效，防止其他用户利用服务器内存中的session进行资源访问
            session.invalidate();
        }
        message.put("message","已退出");
        return message;
    }
}
