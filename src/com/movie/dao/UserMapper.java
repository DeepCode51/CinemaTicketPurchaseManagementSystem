package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.User;
import com.movie.util.JdbcUtil;

public class UserMapper {
	
	public User findUserById(long user_id){
		String sql = "select * from user where user_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	
	public Integer addUser(User user){
		String sql = "insert into user(user_name,user_pwd,user_email,user_role) values(?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUser_name());
			ps.setString(2, user.getUser_pwd());
			ps.setString(3, user.getUser_email());
			ps.setInt(4,user.getUser_role());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteUser(long user_id){
		String sql = "delete from user where user_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, user_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateUser(User user){
		StringBuilder sql = new StringBuilder("update user set ");
		if(user.getUser_name() != null && !"".equals(user.getUser_name())){
			sql.append("user_name = '"+user.getUser_name()+"',");
		}
		if(user.getUser_pwd() != null && !"".equals(user.getUser_pwd())){
			sql.append("user_pwd = '"+user.getUser_pwd()+"',");
		}
		if(user.getUser_email() != null && !"".equals(user.getUser_email())){
			sql.append("user_email = '"+user.getUser_email()+"',");
		}
		if(user.getUser_headImg() != null && !"".equals(user.getUser_headImg())){
			sql.append("user_headImg = '"+user.getUser_headImg()+"',");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" where user_id = ?");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, user.getUser_id());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<User> findAllUser(){
		List<User> list = new ArrayList<User>();
		String sql = "select * from user";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<User> findAllUserPage(int startPage,int endPage){
		List<User> list = new ArrayList<User>();
		String sql = "select * from user limit ?,?";
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
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<User> findUserByName(String name){
		List<User> list = new ArrayList<User>();
		String sql = "select * from user where user_name = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<User> findUserLikeName(String name){
		List<User> list = new ArrayList<User>();
		String sql = "select * from user where user_name like ? ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<User> findUserLikeNamePage(int startPage,int endPage,String name){
		List<User> list = new ArrayList<User>();
		String sql = "select * from user where user_name like ? limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+name+"%");
			ps.setInt(2, startPage);
			ps.setInt(3, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getLong("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_pwd(rs.getString("user_pwd"));
				user.setUser_role(rs.getInt("user_role"));
				user.setUser_email(rs.getString("user_email"));
				user.setUser_headImg(rs.getString("user_headImg"));
				list.add(user);
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
