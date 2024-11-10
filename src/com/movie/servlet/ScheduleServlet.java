package com.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Cinema;
import com.movie.bean.Hall;
import com.movie.bean.Movie;
import com.movie.bean.Order;
import com.movie.bean.Schedule;
import com.movie.bean.User;
import com.movie.service.CinemaService;
import com.movie.service.HallService;
import com.movie.service.MovieService;
import com.movie.service.ScheduleService;

@WebServlet("/schedule")
public class ScheduleServlet extends HttpServlet{
	
	private CinemaService cinemaService;
	private ScheduleService scheduleService;
	private MovieService movieService;
	private HallService hallService;
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		cinemaService = new CinemaService();
		scheduleService = new ScheduleService();
		movieService = new MovieService();
		hallService = new HallService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findScheduleById".equals(method)){
			findScheduleById(req,resp);
		}else if("findAllScheduleByState".equals(method)){
			findAllScheduleByState(req,resp);
		}else if("findAllSchedule".equals(method)){
			findAllSchedule(req,resp);
		}else if("findScheduleByMovieName".equals(method)){
			findScheduleByMovieName(req,resp);
		}else if("findOffScheduleByMovieName".equals(method)){
			findOffScheduleByMovieName(req,resp);
		}else if("findScheduleByCinemaAndMovie".equals(method)){
			findScheduleByCinemaAndMovie(req,resp);
		}else if("addSchedule".equals(method)){
			addSchedule(req,resp);
		}else if("updateSchedulePrice".equals(method)){
			updateSchedulePrice(req,resp);
		}else if("offlineSchedule".equals(method)){
			offlineSchedule(req,resp);
		}
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void findScheduleById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long schedule_id = req.getParameter("schedule_id") == null?1:Long.valueOf(req.getParameter("schedule_id"));

		User user = (User)req.getSession().getAttribute("user");
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        if(user == null) {
			map.put("code",200);
			map.put("msg", "您未登录,登录之后才可购票~");
		}else {
			Schedule schedule = scheduleService.findScheduleById(schedule_id);
			List<Order> orderList = scheduleService.findOrderByUserName(user.getUser_name());
			//如果已支付订单数量大于等于3，每张电影票优惠金额10元。
			if(orderList.size()>=3){
				map.put("coupon", 10);
			}else{
				map.put("coupon", 0);
			}
			map.put("code", 0);
			map.put("data",schedule);
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllScheduleByState(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		Integer schedule_state = req.getParameter("schedule_state") == null?10:Integer.valueOf(req.getParameter("schedule_state"));

		PageInfo<Schedule> info = scheduleService.findAllScheduleByState(page, limit, schedule_state);
		ArrayList<Integer> incomeArr = new ArrayList<Integer>();
		for(int j = 0;j < info.getList().size();j++) {
			List<Order> orderList = info.getList().get(j).getOrderList();
			int income = 0;
			if(orderList!=null && orderList.size()>0){
				for(int i = 0;i < orderList.size();i++) {
					income += orderList.get(i).getOrder_price();
				}
			}
			incomeArr.add(income);
		}
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
		map.put("count", info.getTotal());
		map.put("data", info.getList());
		map.put("income", incomeArr);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllSchedule(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		
		PageInfo<Schedule> info = scheduleService.findAllSchedule(page, limit);
		List<Movie> movieList = movieService.findAllMovies(1);
		List<Cinema> cinemaList = cinemaService.findAllCinemas();
		ArrayList<String> movieArr = new ArrayList<String>();
		ArrayList<Integer> incomeArr = new ArrayList<Integer>();
		for(int j = 0;j < info.getList().size();j++) {
			List<Order> orderList = info.getList().get(j).getOrderList();
			int income = 0;
			if(orderList != null && orderList.size()>0){
				for(int i = 0;i < orderList.size();i++) {
					income += orderList.get(i).getOrder_price();
				}
			}
			incomeArr.add(income);
		}
		for(int i = 0;i < movieList.size();i++) {
			movieArr.add(movieList.get(i).getMovie_cn_name());
		}
		ArrayList<Object> cinema = new ArrayList<>();
		for(int i = 0;i < cinemaList.size();i++) {
			JSONObject cinemaObj = new JSONObject();
			List<Hall> hallList = hallService.findHallByCinemaId(cinemaList.get(i).getCinema_id());
			ArrayList<String> hallArr = new ArrayList<String>();
			for(int j = 0;j < hallList.size();j++) {
				hallArr.add(hallList.get(j).getHall_name());
			}
			cinemaObj.put(cinemaList.get(i).getCinema_name(), hallList);
			cinema.add(cinemaObj);
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
		map.put("count", info.getTotal());
		map.put("data", info.getList());
		map.put("movieName", movieArr);
		map.put("cinema", cinema);
		map.put("income", incomeArr);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findScheduleByMovieName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String movie_name = req.getParameter("movie_name") == null?"":req.getParameter("movie_name").toString();
		Integer schedule_state = req.getParameter("schedule_state") == null?1:Integer.valueOf(req.getParameter("schedule_state"));
		List<Schedule> info = scheduleService.findScheduleByMovieNamePage(page,limit,movie_name,schedule_state);
		ArrayList<Integer> incomeArr = new ArrayList<Integer>();
		for(int j = 0;j < info.size();j++) {
			List<Order> orderList = info.get(j).getOrderList();
			int income = 0;
			for(int i = 0;i < orderList.size();i++) {
				income += orderList.get(i).getOrder_price();
			}
			incomeArr.add(income);
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
		map.put("count", scheduleService.findScheduleByMovieName(movie_name,schedule_state));
		map.put("data", info);
		map.put("income", incomeArr);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findOffScheduleByMovieName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String movie_name = req.getParameter("movie_name") == null?"":req.getParameter("movie_name").toString();
		PageInfo<Schedule> info = scheduleService.findOffScheduleByMovieName(page, limit, movie_name);
		ArrayList<Integer> incomeArr = new ArrayList<Integer>();
		for(int j = 0;j < info.getList().size();j++) {
			List<Order> orderList = info.getList().get(j).getOrderList();
			int income = 0;
			for(int i = 0;i < orderList.size();i++) {
				income += orderList.get(i).getOrder_price();
			}
			incomeArr.add(income);
		}
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
		map.put("count", info.getTotal());
		map.put("data", info.getList());
		map.put("income", incomeArr);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findScheduleByCinemaAndMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long cinema_id = req.getParameter("cinema_id") == null?1:Long.valueOf(req.getParameter("cinema_id"));
		Long movie_id = req.getParameter("movie_id") == null?1:Long.valueOf(req.getParameter("movie_id"));
		int dataIndex = req.getParameter("dataIndex") == null?0:Integer.valueOf(req.getParameter("dataIndex"));
		int page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		int limit = req.getParameter("limit") == null?5:Integer.valueOf(req.getParameter("limit"));
		Calendar calendar = Calendar.getInstance(); 
		String date = "";
		if(dataIndex == 0){
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			date = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}else if(dataIndex == 1){
			calendar.add(Calendar.DATE, 1);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			date = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}else if(dataIndex == 2){
			calendar.add(Calendar.DATE, 2);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			date = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}
		List<Schedule> list = scheduleService.findScheduleByCineamIdAndMovieIdPage(cinema_id, movie_id,date,page,limit);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		map.put("code",0);
		map.put("count",scheduleService.findScheduleByCineamIdAndMovieId(cinema_id, movie_id,date).size());
		map.put("data", list);
		map.put("cinema", cinemaService.findCinemaById(cinema_id));
		map.put("movie", movieService.findMovieById(movie_id));
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void addSchedule(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String movie_name = req.getParameter("movie_name") == null?"":req.getParameter("movie_name").toString();
		String hall_name = req.getParameter("hall_name") == null?"":req.getParameter("hall_name").toString();
		String cinema_name = req.getParameter("cinema_name") == null?"":req.getParameter("cinema_name").toString();
		String schedule_startTime = req.getParameter("schedule_startTime") == null?"":req.getParameter("schedule_startTime").toString();
		Integer schedule_price = req.getParameter("schedule_price") == null?1:Integer.valueOf(req.getParameter("schedule_price"));
		Schedule schedule = new Schedule();
		Hall hall = this.hallService.findHallByCinemaAndHallName(cinema_name, hall_name);
		schedule.setMovie_id(this.movieService.findMovieByName(movie_name).getMovie_id());
		schedule.setHall_id(hall.getHall_id());
		schedule.setSchedule_price(schedule_price);
		schedule.setSchedule_startTime(schedule_startTime);
		schedule.setSchedule_remain(hall.getHall_capacity());
		schedule.setSchedule_state(1);
		Integer rs = this.scheduleService.addSchedule(schedule);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
        
		if(rs > 0) {
			map.put("code", 0);
			map.put("mgs", "增加成功~");
		}else {
			map.put("code", 200);
			map.put("mgs", "增加失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void updateSchedulePrice(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long schedule_id = req.getParameter("schedule_id") == null?1:Long.valueOf(req.getParameter("schedule_id"));
		Integer schedule_price = req.getParameter("schedule_price") == null?1:Integer.valueOf(req.getParameter("schedule_price"));
		Schedule schedule = new Schedule();
		schedule.setSchedule_id(schedule_id);
		schedule.setSchedule_price(schedule_price);
		Integer rs = this.scheduleService.updateSchedule(schedule);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		
		if(rs > 0) {
			map.put("code", 0);
			map.put("mgs", "修改成功~");
		}else {
			map.put("code", 200);
			map.put("mgs", "修改失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	} 
	
	public void offlineSchedule(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long schedule_id = req.getParameter("schedule_id") == null?1:Long.valueOf(req.getParameter("schedule_id"));

		Integer rs = this.scheduleService.deleteSchedule(schedule_id);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		if(rs > 0) {
			map.put("code", 0);
			map.put("mgs", "下架成功~");
		}else {
			map.put("code", 200);
			map.put("mgs", "下架失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
}
