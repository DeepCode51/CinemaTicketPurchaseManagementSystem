package com.movie.service;

import java.util.ArrayList;
import java.util.List;



import com.movie.bean.Hall;
import com.movie.dao.CinemaMapper;
import com.movie.dao.HallMapper;

public class HallService {

	private HallMapper hallMapper = new HallMapper();
	private CinemaMapper cinemaMapper = new CinemaMapper();
	
	public Hall findHallById(long hall_id) {
		Hall hall = this.hallMapper.findHallById(hall_id);
		hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		return hall;
	}

	public Hall findHallByCinemaAndHallName(String cinema_name, String hall_name) {
		return this.hallMapper.findHallByCinemaAndHallName(cinema_name, hall_name);
	}

	public Integer addHall(Hall hall) {
		return this.hallMapper.addHall(hall);
	}

	public Integer updateHall(Hall hall) {
		return this.hallMapper.updateHall(hall);
	}

	public Integer deleteHall(long hall_id) {
		return this.hallMapper.deleteHall(hall_id);
	}

	public List<Hall> findHallByCinemaId(long cinema_id) {
		List<Hall> list = this.hallMapper.findHallByCinemaId(cinema_id);
		for(Hall hall : list) {
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		}
		return list;
	}

	public List<Hall> findAllHalls() {
		List<Hall> list = this.hallMapper.findAllHalls();
		for(Hall hall : list) {
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		}
		return list;
	}
	
	public List<Hall> findAllHallBySplitPage(Integer page,
			Integer limit, String keyword) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Hall> list = new ArrayList<Hall>();
		list = this.hallMapper.findAllHallsPage(startPage,endPage,keyword);
		for(Hall hall : list) {
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		}
		return list;
	}
	
	public int findAllHallCount(Integer page,
			Integer limit, String keyword) {
		List<Hall> list = new ArrayList<Hall>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.hallMapper.findHallLikeName(keyword);
		}else {
			list = this.hallMapper.findAllHalls();
		}
		for(Hall hall : list) {
			hall.setHall_cinema(this.cinemaMapper.findCinemaById(hall.getCinema_id()));
		}
		return list.size();
	}

	public List<Hall> getHallByParams(Long cinema_id, String hall_name) {
		return this.hallMapper.getHallByParams(cinema_id,hall_name);
	}
}
