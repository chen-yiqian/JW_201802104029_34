package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Daomain.User;
import com.example.demo.Service.UserService;
import com.example.demo.Util.JSONUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@RestController
public class UserController {
    @RequestMapping(value = "user.ctl", method = RequestMethod.GET)
    public String getUser(@RequestParam(value = "id", required = false)String id_str){
        JSONObject message = new JSONObject();
        try{
            if(id_str == null){
                return responseUsers();
            }else {
                return responseUser(Integer.parseInt(id_str));
            }
        }catch (SQLException e){
            message.put("message","列表失败，数据库操作异常");
            e.printStackTrace();
            return  message.toString();
        }catch (Exception e){
            message.put("message","列表失败，网络异常");
            e.printStackTrace();
            return message.toString();
        }
    }

    @RequestMapping(value = "user.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request)throws IOException{
        JSONObject message=new JSONObject();
        String user_json=JSONUtil.getJSON(request);
        User userToUpdate=JSON.parseObject(user_json,User.class);
        try{
            boolean update=UserService.getInstance().update(userToUpdate);
            if(update){
                message.put("message","修改成功");
            }else {
                message.put("message","修改失败");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
        }
        return message;
    }
    private String responseUser(int id)throws SQLException{
        User user=UserService.getInstance().find(id);
        String user_json= JSON.toJSONString(user);
        return user_json;
    }
    private String responseUsers()throws SQLException{
        Collection<User> users=UserService.getInstance().findAll();
        String users_json=JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
        return users_json;
    }


}
