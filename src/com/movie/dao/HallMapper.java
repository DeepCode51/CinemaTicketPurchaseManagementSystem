package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Hall;
import com.movie.util.JdbcUtil;

public class HallMapper {
	public Hall findHallById(long hall_id){
		String sql = "select * from hall where hall_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, hall_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				return hall;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Hall findHallByCinemaAndHallName(String cinema_name,String hall_name){
		String sql = "select hall.* from hall,cinema where hall.cinema_id = cinema.cinema_id and cinema_name = ? and hall_name= ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, cinema_name);
			ps.setString(2, hall_name);
			rs = ps.executeQuery();
			if(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				return hall;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Integer addHall(Hall hall){
		String sql = "insert into hall(hall_name,cinema_id,hall_capacity)  values(?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, hall.getHall_name());
			ps.setLong(2, hall.getCinema_id());
			ps.setInt(3,hall.getHall_capacity());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateHall(Hall hall){
		String sql = "update hall set hall_name = ?,hall_capacity = ? where hall_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, hall.getHall_name());
			ps.setInt(2, hall.getHall_capacity());
			ps.setLong(3,hall.getHall_id());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteHall(long hall_id){
		String sql = "delete from hall where hall_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, hall_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Hall> findHallByCinemaId(long cinema_id){
		List<Hall> list = new ArrayList<Hall>();
		String sql = "select * from hall where cinema_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, cinema_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				list.add(hall);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Hall> findAllHallsPage(int startPage,int endPage,String hall_name){
		List<Hall> list = new ArrayList<Hall>();
		String sql = "select * from hall limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, startPage);
			ps.setInt(2, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				list.add(hall);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Hall> findAllHalls(){
		List<Hall> list = new ArrayList<Hall>();
		String sql = "select * from hall";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				list.add(hall);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Hall> findHallLikeName(String hall_name){
		List<Hall> list = new ArrayList<Hall>();
		String sql = "select * from hall where hall_name like ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+hall_name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				list.add(hall);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Hall> getHallByParams(Long cinema_id, String hall_name) {
		List<Hall> list = new ArrayList<Hall>();
		String sql = "select * from hall where hall_name = ? and cinema_id = ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, hall_name);
			ps.setLong(2, cinema_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				Hall hall = new Hall();
				hall.setHall_id(rs.getLong("hall_id"));
				hall.setHall_name(rs.getString("hall_name"));
				hall.setHall_capacity(rs.getInt("hall_capacity"));
				hall.setCinema_id(rs.getLong("cinema_id"));
				list.add(hall);
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
