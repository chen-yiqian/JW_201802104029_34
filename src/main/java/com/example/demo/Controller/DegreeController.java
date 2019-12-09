package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Daomain.Degree;
import com.example.demo.Service.DegreeService;
import com.example.demo.Util.JSONUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@RestController
public class DegreeController {
    @RequestMapping(value = "degree.ctl", method = RequestMethod.GET)
    public String getDegree(@RequestParam(value = "id", required = false)String id_str){
        JSONObject message = new JSONObject();
        try{
            if(id_str == null){
                return responseDegrees();
            }else {
                return responseDegree(Integer.parseInt(id_str));
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

    @RequestMapping(value = "degree.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        JSONObject message=new JSONObject();
        String degree_json= JSONUtil.getJSON(request);
        Degree degreeToAdd= JSON.parseObject(degree_json,Degree.class);
        try{
            boolean add=DegreeService.getInstance().add(degreeToAdd);
            if(add){
                message.put("message","添加成功");
            }else {
                message.put("message","添加失败");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value = "degree.ctl",method=RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false)String id_str){
        JSONObject message=new JSONObject();
        int id=Integer.parseInt(id_str);
        try{
            boolean delete=DegreeService.getInstance().delete(id);
            if(delete){
                message.put("message","删除成功");
            }else {
                message.put("message","删除失败");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
        }
        return message;
    }

    @RequestMapping(value = "degree.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request)throws IOException{
        JSONObject message=new JSONObject();
        String degree_json=JSONUtil.getJSON(request);
        Degree degreeToUpdate=JSON.parseObject(degree_json,Degree.class);
        try{
            boolean update=DegreeService.getInstance().update(degreeToUpdate);
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
    private String responseDegree(int id)throws SQLException{
        Degree degree=DegreeService.getInstance().find(id);
        String degree_json= JSON.toJSONString(degree);
        return degree_json;
    }
    private String responseDegrees()throws SQLException{
        Collection<Degree> degrees=DegreeService.getInstance().findAll();
        String degrees_json=JSON.toJSONString(degrees, SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;
    }


}
