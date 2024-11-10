package com.movie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.movie.bean.Comment;
import com.movie.bean.User;
import com.movie.service.CommentService;
import com.movie.service.MovieService;
import com.movie.service.UserService;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet{
	
	private CommentService commentService;
	
	private UserService userService;
	private MovieService movieService;

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		commentService = new CommentService();
		userService = new UserService();
		movieService = new MovieService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findAllComments".equals(method)){
			findAllComments(req,resp);
		}else if("findAllCommentsPage".equals(method)){
			findAllCommentsPage(req,resp);
		}else if("findAllCommentsPageByMovieId".equals(method)){
			findAllCommentsPageByMovieId(req,resp);
		}else if("addCommentByUser".equals(method)){
			addCommentByUser(req,resp);
		}else if("updateComment".equals(method)){
			updateComment(req,resp);
		}else if("deleteComemnt".equals(method)){
			deleteComment(req,resp);
		}else if("findCommentsByUserName".equals(method)){
			findCommentsByUserName(req,resp);
		}
		
		
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void findAllComments(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Comment> list = commentService.findAllComments();
		for(Comment comment: list) {
			comment.setComment_user(userService.findUserById(comment.getUser_id()));
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("msg","");
        map.put("count", list.size());
        map.put("data", list);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllCommentsPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?1:Integer.valueOf(req.getParameter("limit"));
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		
		List<Comment> info = commentService.findAllCommentsBySplitPage(page, limit, user_name);
		for(Comment comment : info) {
			comment.setComment_user(userService.findUserById(comment.getUser_id()));
		}
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", commentService.findAllCommentsBySplitPageCount(user_name));
        map.put("data", info);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findAllCommentsPageByMovieId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		long movie_id = req.getParameter("movie_id") == null?0:Long.valueOf(req.getParameter("movie_id").toString());
		
		List<Comment> info = commentService.findAllCommentsPageByMovieIdPage(page, limit, movie_id);
		for(Comment comment : info) {
			comment.setComment_user(userService.findUserById(comment.getUser_id()));
		}
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", commentService.findAllCommentsPageByMovieIdCount(movie_id));
        map.put("data", info);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void addCommentByUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long movie_id = req.getParameter("movie_id") == null?1:Long.valueOf(req.getParameter("movie_id"));
		String comment_content = req.getParameter("comment_content") == null?"":req.getParameter("comment_content").toString();
		String comment_score = req.getParameter("comment_score") == null?"":req.getParameter("comment_score").toString();
		
		
		User user = (User)req.getSession().getAttribute("user");
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		if(user == null) {
			map.put("code",200);
			map.put("msg", "您未登录,登录之后才可评论~");
		}else {
			Comment comment = new Comment();
			comment.setComment_content(comment_content);
			comment.setMovie_id(movie_id);
			comment.setUser_id(user.getUser_id());
			comment.setComment_time(new Date());
			comment.setComment_score(Integer.valueOf(comment_score));
			Integer rs = commentService.addComemnt(comment);
			if(rs > 0) {
				Integer rs2 = movieService.updateMovieCommentCountAndScore(comment.getMovie_id());
				if(rs2 > 0) {
					map.put("code", 0);
					map.put("msg", "评论成功~");
				}else {
					map.put("code",200);
					map.put("msg", "评论失败~");
				}
			}else {
				map.put("code",200);
				map.put("msg", "评论失败~");
			}
		}
		 String resJSON = JSON.toJSONString(map);
	     out.print(resJSON); // 输出
	}
	
	public void updateComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long comment_id = req.getParameter("comment_id") == null?1:Long.valueOf(req.getParameter("comment_id"));
		String comment_content = req.getParameter("comment_content") == null?"":req.getParameter("comment_content").toString();
		
		Comment comment = this.commentService.findCommentById(comment_id);
		comment.setComment_content(comment_content);
		Integer rs = commentService.updateComment(comment);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "修改成功~");
		}else {
			map.put("code",200);
			map.put("msg", "修改失败~");
		}
		String resJSON = JSON.toJSONString(map);
	     out.print(resJSON); // 输出
	}
	
	public void deleteComment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long comment_id = req.getParameter("comment_id") == null?1:Long.valueOf(req.getParameter("comment_id"));
		Integer rs2 = movieService.delCommentCount(commentService.findCommentById(comment_id).getMovie_id());
		Integer rs = commentService.deleteComment(comment_id);
		System.out.println(rs2);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>(); 
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "删除成功~");
		}else {
			map.put("code", 200);
			map.put("msg", "删除失败~");
		}
		String resJSON = JSON.toJSONString(map);
	    out.print(resJSON); // 输出
	}
	
	public void findCommentsByUserName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?1:Integer.valueOf(req.getParameter("limit"));
		String user_name = req.getParameter("user_name") == null?"":req.getParameter("user_name").toString();
		
		PageInfo<Comment> info = commentService.findCommentsByUserName(page, limit, user_name);
		//System.out.println(info);
		for(Comment comment : info.getList()) {
			comment.setComment_user(userService.findUserById(comment.getUser_id()));
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
		map.put("msg", "");
		map.put("count", info.getTotal());
		map.put("data", info.getList());
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	
}
