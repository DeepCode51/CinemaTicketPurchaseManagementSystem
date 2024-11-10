package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Comment;
import com.movie.util.JdbcUtil;

public class CommentMapper {
	//用户： 修改评论、增加评论
	//管理员： 删除评论、 修改评论
	//查询用户的评论
	public Comment findCommentById(long comment_id){
		String sql = "select * from comment where comment_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, comment_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(comment_id);
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				return comment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Integer addComemnt(Comment comment){
		String sql = "insert into comment(comment_content,comment_time,movie_id,user_id,comment_score)  values(?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, comment.getComment_content());
			ps.setTimestamp(2, new Timestamp(comment.getComment_time().getTime()));
			ps.setLong(3, comment.getMovie_id());
			ps.setLong(4, comment.getUser_id());
			ps.setInt(5, comment.getComment_score());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateComment(Comment comment){
		String sql = "update comment set comment_content = ?,movie_id = ?,user_id = ? where comment_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, comment.getComment_content());
			ps.setLong(2, comment.getMovie_id());
			ps.setLong(3, comment.getUser_id());
			ps.setLong(4, comment.getComment_id());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteComment(long comment_id){
		String sql = "delete from comment where comment_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, comment_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Comment> findAllComments(){
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select * from comment";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Comment> findCommentsByMoiveId(long movie_id){
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select * from comment  where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, movie_id);
			rs = ps.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Comment> findCommentsByMoiveIdPage(int startPage,int endPage,long movie_id){
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select * from comment  where movie_id = ? limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, movie_id);
			ps.setInt(2, startPage);
			ps.setInt(3, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Comment> findCommentsByUserName(String user_name){
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select comment.* from comment,user where comment.user_id = user.user_id and user.user_name = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, user_name);
			rs = ps.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Comment> findCommentsByUserNamePage(int startPage,int endPage,String user_name){
		String userNameSql = "";
		if(!"".equals(user_name)){
			userNameSql = " and user.user_name = ? ";
		}
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select comment.* from comment,user where comment.user_id = user.user_id "+userNameSql+" limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
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
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Comment> findCommentsByUserNameCount(String user_name){
		String userNameSql = "";
		if(!"".equals(user_name)){
			userNameSql = " and user.user_name = ? ";
		}
		List<Comment> list = new ArrayList<Comment>();
		String sql = "select comment.* from comment,user where comment.user_id = user.user_id "+userNameSql;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			if(!"".equals(user_name)){
				ps.setString(1, user_name);
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setComment_id(rs.getLong("comment_id"));
				comment.setUser_id(rs.getLong("user_id"));
				comment.setMovie_id(rs.getLong("movie_id"));
				comment.setComment_content(rs.getString("comment_content"));
				comment.setComment_time(rs.getTimestamp("comment_time"));
				comment.setComment_score(rs.getInt("comment_score"));
				list.add(comment);
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
