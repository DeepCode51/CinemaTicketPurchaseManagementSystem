package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Order;
import com.movie.util.JdbcUtil;

public class OrderMapper {
	
	public Order findOrderById(String order_id){
		String sql = "select * from orderinfo where order_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, order_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Order order = new Order();
				order.setOrder_id(order_id);
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getDate("order_time"));
				return order;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	
	public Integer addOrder(Order order){
		String sql = "insert into orderinfo(order_id,order_position,schedule_id,user_id,order_price,order_time,order_state) values(?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, order.getOrder_id());
			ps.setString(2, order.getOrder_position());
			ps.setLong(3, order.getSchedule_id());
			ps.setLong(4, order.getUser_id());
			ps.setInt(5, order.getOrder_price());
			ps.setTimestamp(6, new Timestamp(order.getOrder_time().getTime()));
			ps.setInt(7, order.getOrder_state());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	//申请退票
	public Integer updateOrderStateToRefund(String order_id){
		String sql = "update orderinfo set order_state = 0 where order_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, order_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	
	 //同意退票
	public Integer updateOrderStateToRefunded(String order_id){
		String sql = "update orderinfo set order_state = 2 where order_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, order_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Order> findRefundOrderByUserName(String user_name){
		String sql = "select orderinfo.*  from orderinfo,user where orderinfo.user_id = user.user_id and user.user_name = ? and orderinfo.order_state = 0 order by order_time desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user_name);
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getDate("order_time"));
				list.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public List<Order> findOrdersByUserName(String user_name){
		String userNameSql = "";
		if(!"".equals(user_name)){
			userNameSql = " and user.user_name = ? ";
		}
		String sql = "select orderinfo.*  from orderinfo,user where orderinfo.user_id = user.user_id "+userNameSql+" order by order_time desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			if(!"".equals(user_name)){
				ps.setString(1, user_name);
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getTimestamp("order_time"));
				list.add(order);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Order> findOrdersByUserNamePage(int startPage,int endPage,String user_name){
		String userNameSql = "";
		if(!"".equals(user_name)){
			userNameSql = " and user.user_name = ? ";
		}
		String sql = "select orderinfo.*  from orderinfo,user where orderinfo.user_id = user.user_id "+userNameSql+" order by order_time desc limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			if(!"".equals(user_name)){
				ps.setString(1, user_name);
				ps.setInt(2, startPage);
				ps.setInt(3, endPage);
			}else{
				ps.setInt(1, startPage);
				ps.setInt(2, endPage);
			}
			
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getTimestamp("order_time"));
				list.add(order);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Order> findAllOrders(){
		String sql = "select * from orderinfo order by order_time desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getDate("order_time"));
				list.add(order);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Order> findOrdersByScheduleId(long schedule_id){
		String sql = "select * from orderinfo where schedule_id = ? and order_state != 2 order by order_time desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, schedule_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getDate("order_time"));
				list.add(order);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Order> findOrdersByState(int order_state){
		String sql = "select * from orderinfo where order_state = ? order by order_time desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<Order>();
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, order_state);
			rs = ps.executeQuery();
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_id(rs.getString("order_id"));
				order.setOrder_position(rs.getString("order_position"));
				order.setOrder_state(rs.getInt("order_state"));
				order.setSchedule_id(rs.getLong("schedule_id"));
				order.setUser_id(rs.getLong("user_id"));
				order.setOrder_price(rs.getInt("order_price"));
				order.setOrder_time(rs.getDate("order_time"));
				list.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	
}
