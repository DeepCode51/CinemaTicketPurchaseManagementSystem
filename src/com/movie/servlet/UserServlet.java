package com.movie.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSON;
import com.movie.bean.User;
import com.movie.service.UserService;
import com.movie.util.SystemParamsUtils;

@WebServlet("/user")
public class UserServlet extends HttpServlet{
	
	private UserService userService;

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		userService = new UserService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("login".equals(method)){
			login(req,resp);
		}else if("logout".equals(method)){
			logout(req,resp);
		}else if("register".equals(method)){
			register(req,resp);
		}else if("updateUser".equals(method)){
			updateUser(req,resp);
		}else if("modifyUserPwd".equals(method)){
			modifyUserPwd(req,resp);
		}else if("findAllUser".equals(method)){
			findAllUser(req,resp);
		}else if("findUserInfosByName".equals(method)){
			findUserInfosByName(req,resp);
		}else{//如果是multipart/form-data类型的提交，证明参数有上传的头像
			try {
				uploadHeadImg(req,resp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		String user_pwd = req.getParameter("user_pwd") == null?"":req.getParameter("user_pwd").toString();
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		User user = userService.login(user_name, user_pwd);
		if(user != null) {
			HttpSession session = req.getSession();
			session.setAttribute("user", user);
			if(user.getUser_role() == 0) {
				map.put("msg", "usersuccess");
				map.put("data", user);
			}else {
				map.put("msg", "adminsuccess");
				map.put("data", user);
			}
		}else{
			map.put("msg", "fail");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("user");
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("state", "success");
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		String user_pwd = req.getParameter("user_pwd") == null?"":req.getParameter("user_pwd").toString();
		String user_email = req.getParameter("user_email") == null?"":req.getParameter("user_email").toString();
		User user = new User();
		user.setUser_name(user_name);
		user.setUser_pwd(user_pwd);
		user.setUser_email(user_email);
		user.setUser_role(0);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		String result = "";
		List<User> list = userService.findUserByName(user_name);
		if(list.size() > 0) {
			result = "fail";
		}else {
			Integer rs = userService.addUser(user);
			if(rs > 0) {
				result =  "success";
			}else {
				result =  "fail";
			}
		}
		
		map.put("state", result);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	public void modifyUserPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		String oldPwd = req.getParameter("oldPwd") == null?"":req.getParameter("oldPwd").toString();
		String newPwd = req.getParameter("newPwd") == null?"":req.getParameter("newPwd").toString();

		String result = "";
		if(user.getUser_pwd().equals(oldPwd)) {
			user.setUser_pwd(newPwd);
			userService.updateUserInfo(user);
			session.removeAttribute("user");
			result =  "success";
		}else {
			result =  "fail";
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("state", result);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	public void findUserInfosByName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		List<User> list = userService.findUserLikeName(user_name);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("msg", "");
        map.put("code", 0);
        map.put("count", list.size());
        map.put("data", list);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}

	public void uploadHeadImg(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		User user = new User();
		
		//创建一个解析器工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
		
        //解析请求，将表单中每个输入项封装成一个FileItem对象
        List<FileItem> fileItems = upload.parseRequest(req);
        //所有表单非文件类型的集合
        Map<String,Object> param = new HashMap<String,Object>();  
        // 迭代表单数据
        for (FileItem fileItem : fileItems) {
            //判断输入的类型是 普通输入项 还是文件
            if (!fileItem.isFormField()) {
                //上传的是文件，获得文件上传字段中的文件名
                //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名
                String fileName = fileItem.getName();
                System.out.println(fileName);
                //Substring是字符串截取，返回值是一个截取后的字符串
                //lastIndexOf(".")是从右向左查,获取.之后的字符串
                String ext = fileName.substring(fileName.lastIndexOf("."));
                //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法, UUID的唯一缺陷在于生成的结果串会比较长
                String name = UUID.randomUUID()+ext;
                //将FileItem对象中保存的主体内容保存到某个指定的文件中
    			String path = SystemParamsUtils.getSysConfig().get("address").toString()+ "/upload/head/" + name;
                File file = new File(path);
            
                String filePath = "/file/upload/head/" + name;
                user.setUser_headImg(filePath);
                fileItem.write(file);
            }else{
            	param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
            }
        }
        user.setUser_id(Long.valueOf(param.get("user_id").toString()));
		user.setUser_name(param.get("user_name") == null?"":param.get("user_name").toString());
		user.setUser_email(param.get("user_email") == null?"":param.get("user_email").toString());
        resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        Integer rs = userService.updateUserInfo(user);
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "");
			map.put("data",userService.findUserById(Long.valueOf(param.get("user_id").toString())));
		}else {
			map.put("code", 200);
			map.put("msg", "");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
        
	}
	
	public void findAllUser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?1:Integer.valueOf(req.getParameter("limit"));
		String keyword = req.getParameter("keyword") == null?"":req.getParameter("keyword").toString();
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("msg", "");
        map.put("code", 0);
        map.put("count", userService.findAllUserBySplitPageCount(keyword));
        map.put("data", userService.findAllUserBySplitPage(page, limit, keyword));
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	public void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String result = "";
		Long user_id = req.getParameter("user_id") == null?1:Long.valueOf(req.getParameter("user_id"));
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		String user_pwd = req.getParameter("user_pwd") == null?"":req.getParameter("user_pwd").toString();
		String user_email = req.getParameter("user_email") == null?"":req.getParameter("user_email").toString();

		User user = new User();
		user.setUser_id(user_id);
		user.setUser_name(user_name);
		user.setUser_pwd(user_pwd);
		user.setUser_email(user_email);
		Integer rs = userService.updateUserInfo(user);
		if(rs > 0) {
			result =  "success";
		}else {
			result =  "fail";
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("state", result);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
}
