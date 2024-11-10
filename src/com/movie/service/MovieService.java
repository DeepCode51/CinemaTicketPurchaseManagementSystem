package com.movie.service;

import java.math.BigDecimal;
import java.util.List;


import com.movie.bean.Comment;
import com.movie.bean.Movie;
import com.movie.dao.CommentMapper;
import com.movie.dao.MovieMapper;
import com.movie.dao.UserMapper;

public class MovieService{
	
	private MovieMapper movieMapper = new MovieMapper();
	private CommentMapper commentMapper = new CommentMapper();
	private UserMapper userMapper = new UserMapper();

	public Movie findMovieById(long movie_id) {
		Movie movie = this.movieMapper.findMovieById(movie_id);
		List<Comment> list = this.commentMapper.findCommentsByMoiveId(movie_id);
		for(Comment comment : list) {
			comment.setComment_user(this.userMapper.findUserById(comment.getUser_id()));
		}
		movie.setCommentList(list);
		return movie;
	}
	
	public Integer changeMovieBoxOffice(float price, long movie_id) {
		return this.movieMapper.changeMovieBoxOffice(price, movie_id);
	}

	public Movie findMovieByName(String movie_cn_name) {
		return this.movieMapper.findMovieByName(movie_cn_name);
	}

	public Integer updateMovieCommentCountAndScore(long movie_id) {
		int comment_score = 0;
		List<Comment> list = this.commentMapper.findCommentsByMoiveId(movie_id);
		if(list !=null && list.size()>0){
			for(Comment comment : list){
				comment_score += comment.getComment_score();
			}
		}
		Float movie_score = new BigDecimal((float)comment_score/(list.size())).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		return this.movieMapper.updateMovieCommentCountAndScore(movie_id,movie_score);
	}
	
	public Integer delCommentCount(long movie_id) {
		return this.movieMapper.deleteMovieCommentCount(movie_id);
	}

	public Integer addMovie(Movie movie) {
		return this.movieMapper.addMovie(movie);
	}

	public Integer deleteMovie(long movie_id) {
		return this.movieMapper.deleteMovie(movie_id);
	}

	public Integer updateMovie(Movie movie) {
		return this.movieMapper.updateMovie(movie);
	}

	public List<Movie> findAllMoviesPage(int movie_state,int page,int limit) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		return this.movieMapper.findAllMoviesPage(movie_state,startPage,endPage);
	}
	
	public List<Movie> findAllMovies(int movie_state) {
		return this.movieMapper.findAllMovies(movie_state);
	}
	
	public List<Movie> findMoviesLikeName(String name) {
		return this.movieMapper.findMoviesLikeName(name);
	}

	public List<Movie> findMoviesLikeType(String order,String type,String area,String year,String searchMovie) {
		return this.movieMapper.findMoviesLikeType(order,type,area,year,searchMovie);
	}
	
	public List<Movie> findMoviesLikeTypePage(int page,int limit,String order,String type,String area,String year,String searchMovie) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		return this.movieMapper.findMoviesLikeTypePage(startPage,endPage,order,type,area,year,searchMovie);
	}

	public List<Movie> sortMovieByDate() {
		return this.movieMapper.sortMovieByDate();
	}

	public List<Movie> sortMovieByCount() {
		return this.movieMapper.sortMovieByCount();
	}

	public List<Movie> sortMovieByScore() {
		return this.movieMapper.sortMovieByScore();
	}

	public List<Movie> sortMovieByBoxOffice() {
		return this.movieMapper.sortMovieByBoxOffice();
	}
	
}
