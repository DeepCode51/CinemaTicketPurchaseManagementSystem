package com.movie.service;

import java.util.ArrayList;
import java.util.List;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Hall;
import com.movie.bean.Order;
import com.movie.bean.Schedule;
import com.movie.dao.CinemaMapper;
import com.movie.dao.HallMapper;
import com.movie.dao.MovieMapper;
import com.movie.dao.OrderMapper;
import com.movie.dao.ScheduleMapper;
import com.movie.dao.UserMapper;

public class OrderService {

	private OrderMapper orderMapper = new OrderMapper();
	private UserMapper userMapper = new UserMapper();
	private ScheduleMapper scheduleMapper = new ScheduleMapper();
	private HallMapper hallMapper = new HallMapper();
	private MovieMapper movieMapper = new MovieMapper();
	private CinemaMapper cinemaMapper = new CinemaMapper();
	
	public Order findOrderById(String order_id) {
		Order order = this.orderMapper.findOrderById(order_id);
		if(order != null) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}else {
			order = null;
		}
		return order;
	}
	
	public List<Order> findRefundOrderByUserName(String user_name) {
		List<Order> list = this.orderMapper.findRefundOrderByUserName(user_name);
		if(list.size() > 0) {
			for(Order order : list) {
				order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
				Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
				Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
				hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
				schedule.setSchedule_hall(hall);
				schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
				order.setOrder_schedule(schedule);
			}
		}else {
			list = null;
		}
		return list;
	}

	public Integer addOrder(Order order) {
		return this.orderMapper.addOrder(order);
	}
	
	public Integer updateOrderStateToRefund(String order_id) {
		return this.orderMapper.updateOrderStateToRefund(order_id);
	}
	public Integer updateOrderStateToRefunded(String order_id) {
		return this.orderMapper.updateOrderStateToRefunded(order_id);
	}
	
	public List<Order> findOrdersByUserNamePage(Integer page,Integer limit,String user_name) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Order> list = this.orderMapper.findOrdersByUserNamePage(startPage,endPage,user_name);
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		return list;
	}
	
	public int findOrdersByUserName(String user_name) {
		List<Order> list = this.orderMapper.findOrdersByUserName(user_name);
		return list.size();
	}
	
	public List<Order> findAllOrders() {
		List<Order> list = this.orderMapper.findAllOrders();
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		return list;
	}
	
	public PageInfo<Order> findOrdersByState(Integer page,Integer limit,int order_state) {
		PageHelper.startPage(page, limit);
		List<Order> list = this.orderMapper.findOrdersByState(order_state);
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		PageInfo<Order> info = new PageInfo<Order>(list);
		return info;
	}
	
	public PageInfo<Order> findAllOrdersBySplitPage(Integer page, Integer limit, String keyword) {
		PageHelper.startPage(page, limit);
		List<Order> list = new ArrayList<Order>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.orderMapper.findOrdersByUserName(keyword);
		}else {
			list = this.orderMapper.findAllOrders();
		}
		
		for(Order order : list) {
			order.setOrder_user(this.userMapper.findUserById(order.getUser_id()));
			Schedule schedule = this.scheduleMapper.findScheduleById(order.getSchedule_id());
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			order.setOrder_schedule(schedule);
		}
		PageInfo<Order> info = new PageInfo<Order>(list);
		return info;
	}
}
