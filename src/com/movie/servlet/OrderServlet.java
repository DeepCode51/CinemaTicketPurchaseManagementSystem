package com.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.movie.bean.Order;
import com.movie.bean.User;
import com.movie.service.MovieService;
import com.movie.service.OrderService;
import com.movie.service.ScheduleService;

@WebServlet("/order")
public class OrderServlet extends HttpServlet{
	
	private OrderService orderService;
	private ScheduleService scheduleService;  //支付、退票成功 座位+-
	private MovieService movieService; 

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		orderService = new OrderService();
		scheduleService = new ScheduleService();
		movieService = new MovieService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findOrderById".equals(method)){
			findOrderById(req,resp);
		}else if("findOrderByUserName".equals(method)){
			findOrderByUserName(req,resp);
		}else if("findRefundOrderByUser".equals(method)){
			findRefundOrderByUser(req,resp);
		}else if("findAllOrders".equals(method)){
			findAllOrders(req,resp);
		}else if("findAllOrdersPage".equals(method)){
			findAllOrdersPage(req,resp);
		}else if("findAllRefundOrder".equals(method)){
			findAllRefundOrder(req,resp);
		}else if("buyTickets".equals(method)){
			buyTickets(req,resp);
		}else if("applyForRefund".equals(method)){
			applyForRefund(req,resp);
		}else if("agreeForRefund".equals(method)){
			agreeForRefund(req,resp);
		}
		
		
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	
	//查看订单是否 是支付的（返回给前端的数据）
	public void findOrderById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String order_id = req.getParameter("order_id") == null?"":req.getParameter("order_id").toString();

		Order order = orderService.findOrderById(order_id);
		List<Order> list = new ArrayList<Order>();
		list.add(order);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", list.size());
		map.put("data",list);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findOrderByUserName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		List<Order> info = orderService.findOrdersByUserNamePage(page, limit, user_name);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", orderService.findOrdersByUserName(user_name));
		map.put("data", info);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findRefundOrderByUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();

		List<Order> list = this.orderService.findRefundOrderByUserName(user_name);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", list.size());
		map.put("data", list);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllOrders(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Order> list = orderService.findAllOrders();
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", list.size());
		map.put("data", list);
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllOrdersPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String keyword = req.getParameter("keyword") == null?"":req.getParameter("keyword").toString();
		PageInfo<Order> info = orderService.findAllOrdersBySplitPage(page, limit, keyword);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", info.getTotal());
		map.put("data", info.getList());
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllRefundOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		PageInfo<Order> info = orderService.findOrdersByState(page, limit, 0);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", info.getTotal());
		map.put("data", info.getList());
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void buyTickets(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		long schedule_id = req.getParameter("schedule_id") == null?1:Long.valueOf(req.getParameter("schedule_id"));
		Integer price = req.getParameter("price") == null?10:Integer.valueOf(req.getParameter("price"));
		String [] position = req.getParameterValues("position[]");
		String[] copyPosition = Arrays.copyOf(position, position.length);
		User user = (User)req.getSession().getAttribute("user");
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		
		if(user == null) {
			map.put("code",200);
			map.put("msg", "您未登录,登录之后才可购票~");
		}else {
			int done = 0;
			int order_price = price / position.length;
			String user_id = "";
			switch(String.valueOf(user.getUser_id()).length()) {
			case 1: user_id = "000" + String.valueOf(user.getUser_id()); break;
			case 2: user_id = "00" + String.valueOf(user.getUser_id()); break;
			case 3: user_id = "0" + String.valueOf(user.getUser_id()); break;
			case 4: user_id = String.valueOf(user.getUser_id()); break;
			}
			for(int i = 0;i < position.length;i++) {
				Order order = new Order();
				String order_id = "";
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
				order_id += dateFormat.format(date);
				order_id += user_id;
				order.setOrder_position(copyPosition[i]);
				String index = "";
				switch(position[i].length()) {
					case 4: 
						index = "0" + position[i].replaceAll("排", "0");
						index = index.replaceAll("座", "");
						break;
					case 5:
						if(position[i].charAt(2) >= 48 && position[i].charAt(2) <= 57) {
							index = "0" + position[i].replaceAll("排", "");
							index = index.replaceAll("座", "");
						}else {
							index = position[i].replaceAll("排", "0");
							index = index.replaceAll("座", "");
						}
						break;
					case 6:
						index = position[i].replaceAll("排", ""); 
						index = index.replaceAll("座", "");
						break;
				}
				order_id += index;
				order.setOrder_id(order_id);
				order.setSchedule_id(schedule_id);
				order.setUser_id(user.getUser_id());
				order.setOrder_price(order_price);
				order.setOrder_time(new Date());
				order.setOrder_state(1);
				Integer rs = this.orderService.addOrder(order);
				Integer rs1 = this.scheduleService.delScheduleRemain(schedule_id);
				done++;
			}
			if(done == position.length) {
				float sum = (float)price/10000;
				Integer rs2 = this.movieService.changeMovieBoxOffice(sum, this.scheduleService.findScheduleById(schedule_id).getMovie_id());
				map.put("code",0);
				map.put("msg", "购票成功~");
			}else {
				map.put("code",200);
				map.put("msg", "购票失败~");
			}
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void applyForRefund(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String order_id = req.getParameter("order_id") == null?"":req.getParameter("order_id").toString();
		Integer rs = orderService.updateOrderStateToRefund(order_id);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
        
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "退票申请已发送~");
		}else {
			map.put("code", 200);
			map.put("msg", "操作失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void agreeForRefund(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String order_id = req.getParameter("order_id") == null?"":req.getParameter("order_id").toString();
		
		Integer rs = this.orderService.updateOrderStateToRefunded(order_id);
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		
		if(rs > 0) {
			Order order = this.orderService.findOrderById(order_id);
			int price = order.getOrder_price();
			long movie_id = order.getOrder_schedule().getMovie_id();
			Integer rs2 = this.movieService.changeMovieBoxOffice((float)price/10000, movie_id);
			map.put("code", 0);
			map.put("msg", "退票成功");
		}else {
			map.put("code", 200);
			map.put("msg", "退票失败");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
}
