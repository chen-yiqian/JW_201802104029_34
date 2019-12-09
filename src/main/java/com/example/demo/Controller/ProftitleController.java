package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Daomain.Proftitle;
import com.example.demo.Service.ProftitleService;
import com.example.demo.Util.JSONUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@RestController
public class ProftitleController {
    @RequestMapping(value = "proftitle.ctl", method = RequestMethod.GET)
    public String getProftitle(@RequestParam(value = "id", required = false)String id_str){
        JSONObject message = new JSONObject();
        try{
            if(id_str == null){
                return responseProftitles();
            }else {
                return responseProftitle(Integer.parseInt(id_str));
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

    @RequestMapping(value = "proftitle.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request)throws IOException {
        JSONObject message=new JSONObject();
        String proftitle_json= JSONUtil.getJSON(request);
        Proftitle proftitleToAdd= JSON.parseObject(proftitle_json,Proftitle.class);
        try{
            boolean add=ProftitleService.getInstance().add(proftitleToAdd);
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

    @RequestMapping(value = "proftitle.ctl",method=RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false)String id_str){
        JSONObject message=new JSONObject();
        int id=Integer.parseInt(id_str);
        try{
            boolean delete=ProftitleService.getInstance().delete(id);
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

    @RequestMapping(value = "proftitle.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request)throws IOException{
        JSONObject message=new JSONObject();
        String proftitle_json=JSONUtil.getJSON(request);
        Proftitle proftitleToUpdate=JSON.parseObject(proftitle_json,Proftitle.class);
        try{
            boolean update=ProftitleService.getInstance().update(proftitleToUpdate);
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
    private String responseProftitle(int id)throws SQLException{
        Proftitle proftitle=ProftitleService.getInstance().find(id);
        String proftitle_json= JSON.toJSONString(proftitle);
        return proftitle_json;
    }
    private String responseProftitles()throws SQLException{
        Collection<Proftitle> proftitles=ProftitleService.getInstance().findAll();
        String proftitles_json=JSON.toJSONString(proftitles, SerializerFeature.DisableCircularReferenceDetect);
        return proftitles_json;
    }


}
