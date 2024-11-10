package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Cinema;
import com.movie.util.JdbcUtil;

public class CinemaMapper {
	
	public Cinema findCinemaById(long cinema_id){
		String sql = "select * from cinema where cinema_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				return cinema;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Integer addCinema(Cinema cinema){
		String sql = "insert into cinema(cinema_name,cinema_address) values(?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, cinema.getCinema_name());
			ps.setString(2, cinema.getCinema_address());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateCinema(Cinema cinema){
		String sql = "update cinema set cinema_name = ?,cinema_address = ? where cinema_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, cinema.getCinema_name());
			ps.setString(2, cinema.getCinema_address());
			ps.setLong(3, cinema.getCinema_id());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteCinema(long cinema_id){
		String sql = "delete from cinema where cinema_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Cinema> findAllCinemas(){
		List<Cinema> list = new ArrayList<Cinema>();
		String sql = "select * from cinema";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				list.add(cinema);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Cinema> findCinemasLikeName(String cinema_name){
		List<Cinema> list = new ArrayList<Cinema>();
		String sql = "select * from cinema where cinema_name like ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+cinema_name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				list.add(cinema);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Cinema> findCinemasByMovieIdPage(long movie_id,long cinema_id,String dataStr,int startPage,int endPage){
		List<Cinema> list = new ArrayList<Cinema>();
		StringBuilder sql = new StringBuilder("select distinct cinema.* from hall,schedule,cinema where hall.hall_id=schedule.hall_id and hall.cinema_id=cinema.cinema_id and schedule.schedule_state = 1 and schedule.movie_id = ?");
		if(cinema_id!=0){
			sql.append(" and hall.cinema_id = ? ");
		}
		sql.append(" and unix_timestamp(schedule.schedule_startTime) >= unix_timestamp(now()) and schedule.schedule_startTime like ? ");
		sql.append(" limit ?,?");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql.toString());
			int i=1;
			ps.setLong(i, movie_id);
			i++;
			if(cinema_id!=0){
				ps.setLong(i, cinema_id);
				i++;
			}
			ps.setString(i, "%"+dataStr+"%");
			i++;
			ps.setInt(i, startPage);
			i++;
			ps.setInt(i, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				list.add(cinema);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Cinema> findCinemasByMovieId(long movie_id,long cinema_id,String dataStr){
		List<Cinema> list = new ArrayList<Cinema>();
		StringBuilder sql = new StringBuilder("select distinct cinema.* from hall,schedule,cinema where hall.hall_id=schedule.hall_id and hall.cinema_id=cinema.cinema_id and schedule.schedule_state = 1 and schedule.movie_id = ?");
		if(cinema_id!=0){
			sql.append(" and hall.cinema_id = ? ");
		}
		sql.append(" and unix_timestamp(schedule.schedule_startTime) >= unix_timestamp(now()) and schedule.schedule_startTime like ? ");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql.toString());
			int i=1;
			ps.setLong(i, movie_id);
			i++;
			if(cinema_id!=0){
				ps.setLong(i, cinema_id);
				i++;
			}
			ps.setString(i, "%"+dataStr+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				list.add(cinema);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Cinema> findCinemaByName(String cinema_name){
		List<Cinema> list = new ArrayList<Cinema>();
		String sql = "select * from cinema where cinema_name = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, cinema_name);
			rs = ps.executeQuery();
			while(rs.next()) {
				Cinema cinema = new Cinema();
				cinema.setCinema_id(rs.getLong("cinema_id"));
				cinema.setCinema_name(rs.getString("cinema_name"));
				cinema.setCinema_address(rs.getString("cinema_address"));
				list.add(cinema);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
}
