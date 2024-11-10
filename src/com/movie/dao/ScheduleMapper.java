package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Schedule;
import com.movie.util.JdbcUtil;

public class ScheduleMapper {
	
	public Schedule findScheduleById(long schedule_id){
		String sql = "select * from schedule where schedule_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				return schedule;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Integer addSchedule(Schedule schedule){
		String sql = "insert into schedule(hall_id,movie_id,schedule_price,schedule_remain,schedule_startTime,schedule_state) values(?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule.getHall_id());
			ps.setLong(2, schedule.getMovie_id());
			ps.setInt(3, schedule.getSchedule_price());
			ps.setInt(4, schedule.getSchedule_remain());
			ps.setString(5, schedule.getSchedule_startTime());
			ps.setInt(6, schedule.getSchedule_state());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateSchedule(Schedule schedule){
		String sql = "update schedule set schedule_price = ?  where schedule_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule.getHall_id());
			ps.setLong(2, schedule.getMovie_id());
			ps.setInt(1, schedule.getSchedule_price());
			ps.setInt(4, schedule.getSchedule_remain());
			ps.setString(5, schedule.getSchedule_startTime());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteSchedule(long schedule_id){
		String sql = "update schedule set schedule_state = 0 where schedule_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer addScheduleRemain(long schedule_id){
		String sql = "update schedule set schedule_remain = schedule_remain + 1 where schedule_id = ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer delScheduleRemain(long schedule_id){
		String sql = "update schedule set schedule_remain = schedule_remain - 1 where schedule_id = ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Schedule> findScheduleByMovieNamePage(int startPage,int endPage,String movie_name,int schedule_state){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,movie where schedule.movie_id = movie.movie_id and schedule.schedule_state = ? and movie.movie_cn_name like ? limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, schedule_state);
			ps.setString(2, "%"+movie_name+"%");
			ps.setInt(3, startPage);
			ps.setInt(4, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findScheduleByMovieName(String movie_name,int schedule_state){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,movie where schedule.movie_id = movie.movie_id and schedule.schedule_state = ? and movie.movie_cn_name like ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, schedule_state);
			ps.setString(2, "%"+movie_name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findOffScheduleByMovieName(String movie_name){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,movie where schedule.movie_id = movie.movie_id and schedule.schedule_state = 0 and movie.movie_cn_name like ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+movie_name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findScheduleByState(int schedule_state){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select * from schedule where schedule_state = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, schedule_state);
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findAllSchedule(){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select * from schedule";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findScheduleByCinemaAndMoviePage(long cinema_id,long movie_id,String date,int startPage,int endPage){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,hall where schedule.hall_id=hall.hall_id and hall.cinema_id = ? and movie_id = ? and schedule_state = 1 and unix_timestamp(schedule_startTime) >= unix_timestamp(now()) and schedule_startTime like ?  limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			ps.setLong(2, movie_id);
			ps.setString(3, "%"+date+"%");
			ps.setInt(4, startPage);
			ps.setInt(5, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findScheduleByCinemaAndMovie(long cinema_id,long movie_id,String date){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,hall where schedule.hall_id=hall.hall_id and hall.cinema_id = ? and movie_id = ? and schedule_state = 1 and unix_timestamp(schedule_startTime) >= unix_timestamp(now()) and schedule_startTime like ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			ps.setLong(2, movie_id);
			ps.setString(3, "%"+date+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Schedule> findScheduleByCinemaAndMovieAndHall(long hall_id,long cinema_id,long movie_id,String dataStr){
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select schedule.* from schedule,hall where schedule.hall_id=hall.hall_id and hall.cinema_id = ? and movie_id = ? and schedule.hall_id = ? and schedule.schedule_state = 1 and unix_timestamp(schedule.schedule_startTime) >= unix_timestamp(now()) and schedule.schedule_startTime like ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			ps.setLong(2, movie_id);
			ps.setLong(3, hall_id);
			ps.setString(4, "%"+dataStr+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setSchedule_id(rs.getLong("schedule_id"));
				schedule.setHall_id(rs.getLong("hall_id"));
				schedule.setMovie_id(rs.getLong("movie_id"));
				schedule.setSchedule_price(rs.getInt("schedule_price"));
				schedule.setSchedule_startTime(rs.getString("schedule_startTime"));
				schedule.setSchedule_remain(rs.getInt("schedule_remain"));
				schedule.setSchedule_state(rs.getInt("schedule_state"));
				list.add(schedule);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
}
