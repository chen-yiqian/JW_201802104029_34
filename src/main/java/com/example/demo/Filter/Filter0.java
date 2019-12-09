package com.example.demo.Filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//过滤器名称和请求
@Order(1)
@WebFilter(filterName = "Filter0",urlPatterns = "/*")
public class Filter0 implements Filter {
    public void destroy() {
    }
    //重写Filter中声明的方法
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter0 - encodimg begins");
        //将ServletRequest强制转换HttpServletRequest
        HttpServletRequest request=(HttpServletRequest)req;
        //将ServletResponse强制转换HttpServletResponse
        HttpServletResponse response=(HttpServletResponse) resp;
        //定义请求的相对路径
        String path=request.getRequestURI();
        boolean u1=path.contains("/login");
        boolean u2=path.contains("/mysystem");
        //if语句判断是否包含/login
        if(!u1 && !u2){
            String method=request.getMethod();
            if(method.equals("PUT")||method.equals("POST")){
                request.setCharacterEncoding("UTF-8");
            }else {
                response.setContentType("text/html;charset=utf-8");
            }
            //创建JSON对象
            JSONObject message=new JSONObject();
            //访问权限验证
            //如果当前请求对应着服务器内存中的一个session对象，则返回该对象
            //如果服务器内存中没有session对象与当前请求对应，则返回null
            HttpSession session=request.getSession(false);
            if(session==null || session.getAttribute("currentUser") ==null){
                message.put("message","请登录或重新登录");
                //响应到前端
                response.getWriter().println(message);
                //此处重定向到索引页（菜单页）
                return;
            }
        }
        response.setContentType("text/html;charset=utf-8");
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter0 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException {

    }
}
