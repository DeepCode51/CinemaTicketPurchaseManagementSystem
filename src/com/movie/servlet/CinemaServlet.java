package com.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Cinema;
import com.movie.service.CinemaService;

@WebServlet("/cinema")
public class CinemaServlet extends HttpServlet{
	
	private CinemaService cinemaService;

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		cinemaService = new CinemaService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findAllCinema".equals(method)){
			findAllCinema(req,resp);
		}else if("updateCinema".equals(method)){
			updateCinema(req,resp);
		}else if("add".equals(method)){
			add(req,resp);
		}
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	private void findAllCinema(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String keyword = req.getParameter("keyword") == null?"":req.getParameter("keyword").toString();
		PageInfo<Cinema> info = cinemaService.findAllCinemaBySplitPage(page, limit, keyword);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("msg", "");
        map.put("code", 0);
        map.put("count", info.getTotal());
        map.put("data", info.getList());
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
        String result ="";
		String cinema_name = req.getParameter("cinema_name") == null?"":req.getParameter("cinema_name").toString();
		String cinema_address = req.getParameter("cinema_address") == null?"":req.getParameter("cinema_address").toString();
		Cinema cinema = new Cinema();
		cinema.setCinema_name(cinema_name);
		cinema.setCinema_address(cinema_address);
		List<Cinema> list = cinemaService.findCinemaByName(cinema.getCinema_name());
		if(list.size() > 0) {
			result = "fail";
		}else {
			Integer rs = cinemaService.addCinema(cinema);
			if(rs > 0) {
				result =  "success";
			}else {
				result = "fail";
			}
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("state", result);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void updateCinema(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String result = "";
		Long cinema_id = req.getParameter("cinema_id") == null?1:Long.valueOf(req.getParameter("cinema_id"));
		String cinema_name = req.getParameter("cinema_name") == null?"":req.getParameter("cinema_name").toString();
		String cinema_address = req.getParameter("cinema_address") == null?"":req.getParameter("cinema_address").toString();
		Cinema cinema = new Cinema();
		cinema.setCinema_id(cinema_id);
		cinema.setCinema_name(cinema_name);
		cinema.setCinema_address(cinema_address);
		Integer rs = cinemaService.updateCinema(cinema);
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
