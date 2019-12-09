package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Daomain.Department;
import com.example.demo.Service.DepartmentService;
import com.example.demo.Util.JSONUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@RestController
public class DepartmentController {
    @RequestMapping(value = "department.ctl", method = RequestMethod.GET)
    public String getDepartment(@RequestParam(value = "id", required = false)String id_str,@RequestParam(value = "paraType",required = false)String schoolId_str){
        JSONObject message = new JSONObject();
        try{
            if(id_str == null){
                return responseDepartments();
            }else if(schoolId_str == null){
                return responseDepartment(Integer.parseInt(id_str));
            } else{
                int schoolId=Integer.parseInt(id_str);
                return responseDepartmentBySchool(schoolId);
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

    @RequestMapping(value = "department.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        JSONObject message=new JSONObject();
        String department_json= JSONUtil.getJSON(request);
        Department departmentToAdd= JSON.parseObject(department_json,Department.class);
        try{
            boolean add=DepartmentService.getInstance().add(departmentToAdd);
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

    @RequestMapping(value = "department.ctl",method=RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false)String id_str){
        JSONObject message=new JSONObject();
        int id=Integer.parseInt(id_str);
        try{
            boolean delete=DepartmentService.getInstance().delete(id);
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

    @RequestMapping(value = "department.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request)throws IOException{
        JSONObject message=new JSONObject();
        String department_json=JSONUtil.getJSON(request);
        Department departmentToUpdate=JSON.parseObject(department_json,Department.class);
        try{
            boolean update=DepartmentService.getInstance().update(departmentToUpdate);
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
    private String responseDepartment(int id)throws SQLException{
        Department department=DepartmentService.getInstance().find(id);
        String department_json= JSON.toJSONString(department);
        return department_json;
    }
    private String responseDepartments()throws SQLException{
        Collection<Department> departments=DepartmentService.getInstance().findAll();
        String departments_json=JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        return departments_json;
    }
    private String responseDepartmentBySchool(int schoolId)throws SQLException{
        Collection<Department> departments=DepartmentService.getInstance().findAllBySchool(schoolId);
        String depBySch_json=JSON.toJSONString(departments,SerializerFeature.DisableCircularReferenceDetect);
        return depBySch_json;
    }
}
