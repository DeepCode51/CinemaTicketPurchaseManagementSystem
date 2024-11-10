package com.movie.service;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Cinema;
import com.movie.bean.Hall;
import com.movie.dao.CinemaMapper;
import com.movie.dao.HallMapper;
import com.movie.dao.ScheduleMapper;

public class CinemaService{
	private CinemaMapper cinemaMapper = new CinemaMapper();
	private HallMapper hallMapper = new HallMapper();
	private ScheduleMapper scheduleMapper = new ScheduleMapper();
	
	public Cinema findCinemaById(long cinema_id) {
		Cinema cinema = this.cinemaMapper.findCinemaById(cinema_id);
		List<Hall> list = this.hallMapper.findHallByCinemaId(cinema_id);
		cinema.setHallList(list);
		return cinema;
	}

	public List<Cinema> findCinemasByMovieId(long movie_id,long cinema_id,String dataStr) {
		List<Cinema> cinemaList = this.cinemaMapper.findCinemasByMovieId(movie_id,cinema_id,dataStr);
		for(Cinema cinema : cinemaList) {
			List<Hall> hallList = this.hallMapper.findHallByCinemaId(cinema.getCinema_id());
			for(Hall hall : hallList) {
				hall.setScheduleList(this.scheduleMapper.findScheduleByCinemaAndMovieAndHall(hall.getHall_id(), hall.getCinema_id(), movie_id,dataStr));
			}
			cinema.setHallList(hallList);
		}
		return cinemaList;
	}
	
	public List<Cinema> findCinemasByMovieIdPage(long movie_id,long cinema_id,String dataStr,int page,int limit) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Cinema> cinemaList = this.cinemaMapper.findCinemasByMovieIdPage(movie_id,cinema_id,dataStr,startPage,endPage);
		for(Cinema cinema : cinemaList) {
			List<Hall> hallList = this.hallMapper.findHallByCinemaId(cinema.getCinema_id());
			for(Hall hall : hallList) {
				hall.setScheduleList(this.scheduleMapper.findScheduleByCinemaAndMovieAndHall(hall.getHall_id(), hall.getCinema_id(), movie_id,dataStr));
			}
			cinema.setHallList(hallList);
		}
		return cinemaList;
	}

	public Integer addCinema(Cinema cinema) {
		return this.cinemaMapper.addCinema(cinema);
	}

	public Integer updateCinema(Cinema cinema) {
		return this.cinemaMapper.updateCinema(cinema);
	}

	public Integer deleteCinema(long cinema_id) {
		return this.cinemaMapper.deleteCinema(cinema_id);
	}

	public List<Cinema> findAllCinemas() {
		List<Cinema> list = this.cinemaMapper.findAllCinemas();
		for(Cinema cinema : list) {
			List<Hall> hallList = this.hallMapper.findHallByCinemaId(cinema.getCinema_id());
			cinema.setHallList(hallList);
		}
		return list;
	}

	public List<Cinema> findCinemasLikeName(String cinema_name) {
		List<Cinema> list = this.cinemaMapper.findCinemasLikeName(cinema_name);
		for(Cinema cinema : list) {
			List<Hall> hallList = this.hallMapper.findHallByCinemaId(cinema.getCinema_id());
			cinema.setHallList(hallList);
		}
		return list;
	}

	public PageInfo<Cinema> findAllCinemaBySplitPage(Integer page,
			Integer limit, String keyword) {
		PageHelper.startPage(page, limit);
		List<Cinema> list = new ArrayList<Cinema>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.cinemaMapper.findCinemasLikeName(keyword);
		}else {
			list = this.cinemaMapper.findAllCinemas();
		}
		PageInfo<Cinema> info = new PageInfo<Cinema>(list);
		return info;
	}

	public List<Cinema> findCinemaByName(String name) {
		return this.cinemaMapper.findCinemaByName(name);
	}
	
}
