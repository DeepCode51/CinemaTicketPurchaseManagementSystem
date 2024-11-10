package com.movie.service;

import java.util.ArrayList;
import java.util.List;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Comment;
import com.movie.dao.CommentMapper;

public class CommentService {

	private CommentMapper commentMapper = new CommentMapper();
	
	public Comment findCommentById(long comment_id) {
		return this.commentMapper.findCommentById(comment_id);
	}

	public Integer addComemnt(Comment comment) {
		return this.commentMapper.addComemnt(comment);
	}

	public Integer updateComment(Comment comment) {
		return this.commentMapper.updateComment(comment);
	}

	public Integer deleteComment(long comment_id) {
		return this.commentMapper.deleteComment(comment_id);
	}

	public List<Comment> findAllComments() {
		return this.commentMapper.findAllComments();
	}
	
	
	
	public List<Comment> findAllCommentsBySplitPage(Integer page, Integer limit, String user_name) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Comment> list = new ArrayList<Comment>();
		list = this.commentMapper.findCommentsByUserNamePage(startPage,endPage,user_name);
		return list;
	}
	
	public int findAllCommentsBySplitPageCount(String keyword) {
		List<Comment> list = new ArrayList<Comment>();
			list = this.commentMapper.findCommentsByUserNameCount(keyword);
		return list.size();
	}
	
	
	public PageInfo<Comment> findCommentsByUserName(Integer page, Integer limit,String user_name) {
		PageHelper.startPage(page,limit);
		List<Comment> list = new ArrayList<Comment>();
		list = this.commentMapper.findCommentsByUserName(user_name);
		PageInfo<Comment> info = new PageInfo<Comment>(list);
		return info;
	}

	public List<Comment> findCommentsByMovieId(long movie_id) {
		return this.commentMapper.findCommentsByMoiveId(movie_id);
	}

	public List<Comment> findAllCommentsPageByMovieIdPage(Integer page,
			Integer limit, long movie_id) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<Comment> list = new ArrayList<Comment>();
		list = this.commentMapper.findCommentsByMoiveIdPage(startPage,endPage,movie_id);
		return list;
	}
	
	public int findAllCommentsPageByMovieIdCount(long movie_id) {
		List<Comment> list = new ArrayList<Comment>();
		list = this.commentMapper.findCommentsByMoiveId(movie_id);
		return list.size();
	}
}
