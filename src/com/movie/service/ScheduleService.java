package com.movie.service;

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

public class ScheduleService{
	private ScheduleMapper scheduleMapper = new ScheduleMapper();
	private HallMapper hallMapper = new HallMapper();
	private MovieMapper movieMapper = new MovieMapper();
	private OrderMapper orderMapper = new OrderMapper();
	private CinemaMapper cinemaMapper = new CinemaMapper();
	
	public Schedule findScheduleById(long schedule_id) {
		Schedule schedule = this.scheduleMapper.findScheduleById(schedule_id);
		Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
		hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		schedule.setSchedule_hall(hall);
		schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
		List<Order> list = this.orderMapper.findOrdersByScheduleId(schedule_id);
		schedule.setOrderList(list);
		return schedule;
	}

	public Integer addSchedule(Schedule schedule) {
		return this.scheduleMapper.addSchedule(schedule);
	}

	public Integer updateSchedule(Schedule schedule) {
		return this.scheduleMapper.updateSchedule(schedule);
	}

	/**
	 * 场次下架功能 而非删除
	 */
	public Integer deleteSchedule(long schedule_id) {
		return this.scheduleMapper.deleteSchedule(schedule_id);
	}

	public Integer addScheduleRemain(long schedule_id) {
		return this.scheduleMapper.addScheduleRemain(schedule_id);
	}

	public Integer delScheduleRemain(long schedule_id) {
		return this.scheduleMapper.delScheduleRemain(schedule_id);
	}
	
	public int findScheduleByMovieName(String movie_name,int schedule_state) {
		List<Schedule> schedules = this.scheduleMapper.findScheduleByMovieName(movie_name,schedule_state);
		return schedules.size();
	}
	
	public List<Schedule> findScheduleByMovieNamePage(Integer page,Integer limit,String movie_name,int schedule_state) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Schedule> schedules = this.scheduleMapper.findScheduleByMovieNamePage(startPage,endPage,movie_name,schedule_state);
		for(Schedule schedule: schedules) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			List<Order> list = this.orderMapper.findOrdersByScheduleId(schedule.getSchedule_id());
			schedule.setOrderList(list);
		}
		return schedules;
	}

	public PageInfo<Schedule> findOffScheduleByMovieName(Integer page, Integer limit, String movie_name) {
		PageHelper.startPage(page, limit);
		List<Schedule> schedules = this.scheduleMapper.findOffScheduleByMovieName(movie_name);
		for(Schedule schedule: schedules) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			List<Order> list = this.orderMapper.findOrdersByScheduleId(schedule.getSchedule_id());
			schedule.setOrderList(list);
		}
		PageInfo<Schedule> info = new PageInfo<Schedule>(schedules);
		return info;
	}
	public PageInfo<Schedule> findAllScheduleByState(Integer page,Integer limit,int schedule_state) {
		PageHelper.startPage(page, limit);
		List<Schedule> schedules = this.scheduleMapper.findScheduleByState(schedule_state);
		for(Schedule schedule: schedules) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			List<Order> list = this.orderMapper.findOrdersByScheduleId(schedule.getSchedule_id());
			schedule.setOrderList(list);
		}
		PageInfo<Schedule> info = new PageInfo<Schedule>(schedules);
		return info;
	}
	
	public PageInfo<Schedule> findAllSchedule(Integer page,Integer limit) {
		PageHelper.startPage(page, limit);
		List<Schedule> schedules = this.scheduleMapper.findAllSchedule();
		for(Schedule schedule: schedules) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
			List<Order> list = this.orderMapper.findOrdersByScheduleId(schedule.getSchedule_id());
			schedule.setOrderList(list);
		}
		PageInfo<Schedule> info = new PageInfo<Schedule>(schedules);
		return info;
	}

	/**
	 * selectSeat页面提供接口
	 */
	public List<Schedule> findScheduleByCineamIdAndMovieId(long cinema_id, long movie_id,String date) {
		List<Schedule> list = this.scheduleMapper.findScheduleByCinemaAndMovie(cinema_id, movie_id,date);
		for(Schedule schedule: list) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
		}
		return list;
	}
	
	public List<Schedule> findScheduleByCineamIdAndMovieIdPage(long cinema_id, long movie_id,String date,int page,int limit) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Schedule> list = this.scheduleMapper.findScheduleByCinemaAndMoviePage(cinema_id, movie_id,date,startPage,endPage);
		for(Schedule schedule: list) {
			Hall hall = this.hallMapper.findHallById(schedule.getHall_id());
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
			schedule.setSchedule_hall(hall);
			schedule.setSchedule_movie(this.movieMapper.findMovieById(schedule.getMovie_id()));
		}
		return list;
	}

	public List<Order> findOrderByUserName(String user_name) {
		return orderMapper.findOrdersByUserName(user_name);
	}
	
	
}
