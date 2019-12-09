package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Daomain.Teacher;
import com.example.demo.Service.TeacherService;
import com.example.demo.Util.JSONUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@RestController
public class TeacherController {
    @RequestMapping(value = "teacher.ctl", method = RequestMethod.GET)
    public String getTeacher(@RequestParam(value = "id", required = false)String id_str){
        JSONObject message = new JSONObject();
        try{
            if(id_str == null){
                return responseTeachers();
            }else {
                return responseTeacher(Integer.parseInt(id_str));
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

    @RequestMapping(value = "teacher.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        JSONObject message=new JSONObject();
        String teacher_json= JSONUtil.getJSON(request);
        Teacher teacherToAdd= JSON.parseObject(teacher_json,Teacher.class);
        try{
            boolean add=TeacherService.getInstance().add(teacherToAdd);
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

    @RequestMapping(value = "teacher.ctl",method=RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false)String id_str){
        JSONObject message=new JSONObject();
        int id=Integer.parseInt(id_str);
        try{
            boolean delete=TeacherService.getInstance().delete(id);
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

    @RequestMapping(value = "teacher.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request)throws IOException{
        JSONObject message=new JSONObject();
        String teacher_json=JSONUtil.getJSON(request);
        Teacher teacherToUpdate=JSON.parseObject(teacher_json,Teacher.class);
        try{
            boolean update=TeacherService.getInstance().update(teacherToUpdate);
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
    private String responseTeacher(int id)throws SQLException{
        Teacher teacher=TeacherService.getInstance().find(id);
        String teacher_json= JSON.toJSONString(teacher);
        return teacher_json;
    }
    private String responseTeachers()throws SQLException{
        Collection<Teacher> teachers=TeacherService.getInstance().findAll();
        String teachers_json=JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        return teachers_json;
    }


}
