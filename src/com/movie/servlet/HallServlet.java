package com.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.movie.bean.Cinema;
import com.movie.bean.Hall;
import com.movie.service.CinemaService;
import com.movie.service.HallService;

@WebServlet("/hall")
public class HallServlet extends HttpServlet{
	
	private HallService hallService;
	private CinemaService cinemaService;

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		hallService = new HallService();
		cinemaService = new CinemaService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findAllHall".equals(method)){
			findAllHall(req,resp);
		}else if("findAllCinema".equals(method)){
			findAllCinema(req,resp);
		}else if("add".equals(method)){
			add(req,resp);
		}else if("updateHall".equals(method)){
			updateHall(req,resp);
		}
		
		
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	public void findAllHall(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?1:Integer.valueOf(req.getParameter("limit"));
		String keyword = req.getParameter("keyword") == null?"":req.getParameter("keyword").toString();
		
		List<Hall> info = hallService.findAllHallBySplitPage(page, limit, keyword);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("msg", "");
		map.put("code", 0);
		map.put("count", hallService.findAllHallCount(page, limit, keyword));
		map.put("data", info);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllCinema(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Cinema> cinemaList = cinemaService.findAllCinemas();
		ArrayList<Object> cinema = new ArrayList<>();
		for(int i = 0;i < cinemaList.size();i++) {
			/*JSONObject cinemaObj = new JSONObject();
			List<Hall> hallList = hallService.findHallByCinemaId(cinemaList.get(i).getCinema_id());
			ArrayList<String> hallArr = new ArrayList<String>();
			for(int j = 0;j < hallList.size();j++) {
				hallArr.add(hallList.get(j).getHall_name());
			}
			cinemaObj.put(cinemaList.get(i).getCinema_name(), hallList);
			cinema.add(cinemaObj);*/
			Map<String,Object> hashMap = new HashMap<String,Object>();
			hashMap.put("id", cinemaList.get(i).getCinema_id());
			hashMap.put("value", cinemaList.get(i).getCinema_name());
			cinema.add(hashMap);
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("msg", "");
		map.put("code", 0);
		map.put("cinema", cinema);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	
	public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String hall_name = req.getParameter("hall_name") == null?"":req.getParameter("hall_name").toString();
		String hall_capacity = req.getParameter("hall_capacity") == null?"":req.getParameter("hall_capacity").toString();
		Long cinema_id = req.getParameter("cinema_id") == null?1:Long.valueOf(req.getParameter("cinema_id"));

		Hall hall = new Hall();
		hall.setCinema_id(cinema_id);
		hall.setHall_name(hall_name);
		if(!"".equals(hall_capacity)){
			hall.setHall_capacity(Integer.valueOf(hall_capacity));
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		
        //根据影院id和放映厅名称，查看是否已经有存在的放映厅
        List<Hall> hallList = hallService.getHallByParams(cinema_id,hall_name);
        if(hallList != null && hallList.size()>0){
        	map.put("state", "fail");
        	map.put("msg", "不能添加重复的放映厅");
        }else{
        	Integer rs = hallService.addHall(hall);
    		if(rs > 0) {
    			map.put("state", "success");
    		}else {
    			map.put("state", "fail");
    			map.put("msg", "添加失败!");
    		}
        }
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void updateHall(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String hall_name = req.getParameter("hall_name") == null?"":req.getParameter("hall_name").toString();
		String hall_capacity = req.getParameter("hall_capacity") == null?"":req.getParameter("hall_capacity").toString();
		Long hall_id = req.getParameter("hall_id") == null?1:Long.valueOf(req.getParameter("hall_id"));
		Hall hall = new Hall();
		hall.setHall_id(hall_id);
		hall.setHall_name(hall_name);
		if(!"".equals(hall_capacity)){
			hall.setHall_capacity(Integer.valueOf(hall_capacity));
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		
        Integer rs = hallService.updateHall(hall);
		if(rs > 0) {
			map.put("state", "success");
		}else {
			map.put("state", "fail");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
}
