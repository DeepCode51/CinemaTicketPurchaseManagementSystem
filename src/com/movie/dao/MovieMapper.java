package com.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movie.bean.Movie;
import com.movie.util.JdbcUtil;



public class MovieMapper {
	public Movie findMovieById(long movie_id){
		String sql = "select * from movie where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, movie_id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(movie_id);
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				return movie;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Movie findMovieByName(String movie_cn_name){
		String sql = "select * from movie  where movie_cn_name = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, movie_cn_name);
			rs = ps.executeQuery();
			if(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				return movie;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return null;
	}
	public Integer addMovie(Movie movie){
		String sql = "insert into movie(movie_cn_name,movie_fg_name,movie_actor,movie_director,movie_detail,movie_duration,movie_type,movie_score,movie_releaseDate,movie_country,movie_picture,movie_state) "+
				"values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, movie.getMovie_cn_name());
			ps.setString(2, movie.getMovie_fg_name());
			ps.setString(3, movie.getMovie_actor());
			ps.setString(4, movie.getMovie_director());
			ps.setString(5, movie.getMovie_detail());
			ps.setString(6, movie.getMovie_duration());
			ps.setString(7, movie.getMovie_type());
			ps.setFloat(8, movie.getMovie_score());
			ps.setDate(9, new java.sql.Date(movie.getReleaseDate().getTime()));
			ps.setString(10, movie.getMovie_country());
			ps.setString(11, movie.getMovie_picture());
			ps.setInt(12, movie.getMovie_state());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteMovie(long movie_id){
		String sql = "delete from movie where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, movie_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateMovie(Movie movie){
		String sql = "update movie set movie_cn_name = ?,"+
			"movie_fg_name = ?,"+
			"movie_actor = ?,"+
			"movie_director = ?,"+
			"movie_detail = ?,"+
			"movie_duration = ?,"+
			"movie_type = ?,"+
			"movie_country = ?,"+
			"movie_picture = ?,"+
			"movie_releaseDate = ?"+
		"where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, movie.getMovie_cn_name());
			ps.setString(2, movie.getMovie_fg_name());
			ps.setString(3, movie.getMovie_actor());
			ps.setString(4, movie.getMovie_director());
			ps.setString(5, movie.getMovie_detail());
			ps.setString(6, movie.getMovie_duration());
			ps.setString(7, movie.getMovie_type());
			ps.setString(8, movie.getMovie_country());
			ps.setString(9, movie.getMovie_picture());
			ps.setDate(10, new java.sql.Date(movie.getReleaseDate().getTime()));
			ps.setLong(11, movie.getMovie_id());
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer deleteMovieCommentCount(long movie_id){
		String sql = "update movie set movie_commentCount = movie_commentCount -1 where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, movie_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer updateMovieCommentCountAndScore(long movie_id,float movie_score){
		String sql = "update movie set movie_commentCount = movie_commentCount +1,movie_score = ? where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFloat(1, movie_score);
			ps.setLong(2, movie_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public Integer changeMovieBoxOffice(float movie_boxOffice,long movie_id){
		String sql = "update movie set movie_boxOffice = movie_boxOffice + ? where movie_id = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFloat(1, movie_boxOffice);
			ps.setLong(2, movie_id);
			ps.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, ps, null);
		}
		return 0;
	}
	public List<Movie> findAllMoviesPage(int movie_state,int startPage,int endPage){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie  where movie_state = ? limit ?,?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, movie_state);
			ps.setInt(2, startPage);
			ps.setInt(3, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> findAllMovies(int movie_state){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie  where movie_state = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, movie_state);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> findMoviesLikeName(String name){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie where movie_cn_name like ? and movie_state = 1";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+name+"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> findMoviesLikeType(String order,String type,String area,String year,String searchMovie){
		List<Movie> list = new ArrayList<Movie>();
		StringBuilder sql = new StringBuilder("select * from movie where movie_state = 1");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			if(!"".equals(type)){
				sql.append(" and movie_type like ?  ");
			}
			if(!"".equals(area)){
				sql.append(" and movie_country like ?  ");
			}
			if(!"".equals(year)){
				sql.append(" and movie_releaseDate like ?  ");
			}
			if(searchMovie!=null && !"".equals(searchMovie)){
				sql.append(" and (movie_cn_name like ? or movie_fg_name like ? )");
			}
			switch (order) {
				case "热门":
					sql.append(" order by movie_commentCount DESC");
					break;
				case "时间":
					sql.append(" order by movie_releaseDate DESC");
					break;
				case "评价":
					sql.append(" order by movie_score DESC");
					break;
			}
			ps = conn.prepareStatement(sql.toString());
			int i=1;
			if(!"".equals(type)){
				ps.setString(i, "%"+type+"%");
				i++;
			}
			if(!"".equals(area)){
				ps.setString(i, "%"+area+"%");
				i++;
			}
			if(!"".equals(year)){
				ps.setString(i, "%"+year+"%");
				i++;
			}
			if(searchMovie!=null && !"".equals(searchMovie)){
				ps.setString(i, "%"+searchMovie+"%");
				i++;
				ps.setString(i, "%"+searchMovie+"%");
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	public List<Movie> findMoviesLikeTypePage(int startPage,int endPage,String order,String type,String area,String year,String searchMovie){
		List<Movie> list = new ArrayList<Movie>();
		StringBuilder sql = new StringBuilder("select * from movie where movie_state = 1");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			if(!"".equals(type)){
				sql.append(" and movie_type like ?  ");
			}
			if(!"".equals(area)){
				sql.append(" and movie_country like ?  ");
			}
			if(!"".equals(year)){
				sql.append(" and movie_releaseDate like ?  ");
			}
			if(searchMovie!=null && !"".equals(searchMovie)){
				sql.append(" and (movie_cn_name like ? or movie_fg_name like ? )");
			}
			switch (order) {
				case "热门":
					sql.append(" order by movie_commentCount DESC");
					break;
				case "时间":
					sql.append(" order by movie_releaseDate DESC");
					break;
				case "评价":
					sql.append(" order by movie_score DESC");
					break;
			}
			sql.append(" limit ?,? ");
			ps = conn.prepareStatement(sql.toString());
			int i=1;
			if(!"".equals(type)){
				ps.setString(i, "%"+type+"%");
				i++;
			}
			if(!"".equals(area)){
				ps.setString(i, "%"+area+"%");
				i++;
			}
			if(!"".equals(year)){
				ps.setString(i, "%"+year+"%");
				i++;
			}
			if(searchMovie!=null && !"".equals(searchMovie)){
				ps.setString(i, "%"+searchMovie+"%");
				i++;
				ps.setString(i, "%"+searchMovie+"%");
				i++;
			}
			ps.setInt(i, startPage);
			i++;
			ps.setInt(i, endPage);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	//上映时间  参评人数  评分
	public List<Movie> sortMovieByDate(){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie where movie_state = 1  order by movie_releaseDate DESC";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> sortMovieByCount(){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie where movie_state = 1 order by movie_commentCount DESC";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> sortMovieByScore(){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie where movie_state = 1 order by movie_score DESC";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(conn, ps, rs);
		}
		return list;
	}
	public List<Movie> sortMovieByBoxOffice(){
		List<Movie> list = new ArrayList<Movie>();
		String sql = "select * from movie where movie_state = 1 order by movie_boxOffice desc";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie();
				movie.setMovie_id(rs.getLong("movie_id"));
				movie.setMovie_cn_name(rs.getString("movie_cn_name"));
				movie.setMovie_fg_name(rs.getString("movie_fg_name"));
				movie.setMovie_actor(rs.getString("movie_actor"));
				movie.setMovie_director(rs.getString("movie_director"));
				movie.setMovie_detail(rs.getString("movie_detail"));
				movie.setMovie_duration(rs.getString("movie_duration"));
				movie.setMovie_type(rs.getString("movie_type"));
				movie.setMovie_score(rs.getFloat("movie_score"));
				movie.setMovie_boxOffice(rs.getFloat("movie_boxOffice"));
				movie.setMovie_commentCount(rs.getLong("movie_commentCount"));
				movie.setReleaseDate(rs.getDate("movie_releaseDate"));
				movie.setMovie_picture(rs.getString("movie_picture"));
				movie.setMovie_country(rs.getString("movie_country"));
				movie.setMovie_state(rs.getInt("movie_state"));
				list.add(movie);
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
