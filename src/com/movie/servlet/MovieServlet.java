package com.movie.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.movie.bean.Cinema;
import com.movie.bean.Movie;
import com.movie.service.CinemaService;
import com.movie.service.MovieService;
import com.movie.util.SystemParamsUtils;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet{
	
	private MovieService movieService;
	
	private CinemaService cinemaService;
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		movieService = new MovieService();
		cinemaService = new CinemaService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("findMovieById".equals(method)){
			findMovieById(req,resp);
		}else if("findAllMovies".equals(method)){
			findAllMovies(req,resp);
		}else if("findAllMoviesBack".equals(method)){
			findAllMoviesBack(req,resp);
		}else if("findMoviesByName".equals(method)){
			findMoviesByName(req,resp);
		}else if("findMoviesByType".equals(method)){
			findMoviesByType(req,resp);
		}else if("sortAllMovies".equals(method)){
			sortAllMovies(req,resp);
		}else if("deleteMovie".equals(method)){
			deleteMovie(req,resp);
		}else{
			try {
				Movie movie = new Movie();
				//所有表单非文件类型的集合
		        Map<String,Object> param = new HashMap<String,Object>();  
				//创建一个解析器工厂
		        DiskFileItemFactory factory = new DiskFileItemFactory();
		        //文件上传解析器
		        ServletFileUpload upload = new ServletFileUpload(factory);
				//解析请求，将表单中每个输入项封装成一个FileItem对象
		        List<FileItem> fileItems = upload.parseRequest(req);
		        Boolean isFile =false;
		        for (FileItem fileItem : fileItems) {
		        	//判断输入的类型是 普通输入项 还是文件
		            if (!fileItem.isFormField()) {
		                //上传的是文件，获得文件上传字段中的文件名
		                //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名
		                String fileName = fileItem.getName();
		                System.out.println(fileName);
		                //Substring是字符串截取，返回值是一个截取后的字符串
		                //lastIndexOf(".")是从右向左查,获取.之后的字符串
		                String ext = fileName.substring(fileName.lastIndexOf("."));
		                //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法, UUID的唯一缺陷在于生成的结果串会比较长
		                String name = UUID.randomUUID()+ext;
		                //将FileItem对象中保存的主体内容保存到某个指定的文件中
		    			String path =req.getRealPath("/file") + "/upload/head/" + name;
		                File file = new File(path);
		            
		                String filePath = "/file/upload/head/" + name;
		                movie.setMovie_picture(filePath);
		        		
		                fileItem.write(file);
		                isFile = true;
		            }else{
		            	param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
		            }
		        }
		        movie.setMovie_cn_name(param.get("movie_cn_name") == null?"":param.get("movie_cn_name").toString());
				movie.setMovie_fg_name(param.get("movie_fg_name") == null?"":param.get("movie_fg_name").toString());
				movie.setMovie_actor(param.get("movie_actor") == null?"":param.get("movie_actor").toString());
				movie.setMovie_director(param.get("movie_director") == null?"":param.get("movie_director").toString());
				movie.setMovie_detail(param.get("movie_detail") == null?"":param.get("movie_detail").toString());
				movie.setMovie_duration(param.get("movie_duration") == null?"":param.get("movie_duration").toString());
				movie.setMovie_type(param.get("movie_type") == null?"":param.get("movie_type").toString());
				movie.setMovie_country(param.get("movie_country") == null?"":param.get("movie_country").toString());
				String movie_releaseDate = param.get("movie_releaseDate") == null?"":param.get("movie_releaseDate").toString();
				if(!"".equals(movie_releaseDate)){
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie_releaseDate);
					movie.setReleaseDate(date);
					
				}
				resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
		        PrintWriter out = resp.getWriter();
		        Map<String,Object> map = new HashMap<>();
		        //新增电影
		        if("addMovie".equals(param.get("method").toString())){
		        	movie.setMovie_state(1);
		        	Integer rs = movieService.addMovie(movie);
		    		if(rs > 0) {
		    			map.put("code", 0);
		    			map.put("msg", "添加成功~");
		    		}else {
		    			map.put("code", 200);
		    			map.put("msg", "添加失败~");
		    		}
		        }else{//更新电影
		        	movie.setMovie_id(Long.valueOf(param.get("movie_id").toString()));
		        	if(!isFile){
		    			Movie oldMovie = this.movieService.findMovieById(movie.getMovie_id());
		    			movie.setMovie_picture(oldMovie.getMovie_picture());
		    		}
		    		Integer rs = movieService.updateMovie(movie);
		    		if(rs > 0) {
		    			map.put("code", 0);
		    			map.put("msg", "修改成功~");
		    		}else {
		    			map.put("code", 200);
		    			map.put("msg", "修改失败~");
		    		}
		        }
		        String resJSON = JSON.toJSONString(map);
		        out.print(resJSON); // 输出
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// 重定向
		//resp.sendRedirect("product_list");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	private void findAllMoviesBack(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		List<Movie> list = movieService.findAllMoviesPage(1,page,limit);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("count", movieService.findAllMovies(1).size());
        map.put("data", list);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	private void findAllMovies(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		List<Movie> list = movieService.findAllMovies(1);
		List<Movie> offList = movieService.sortMovieByScore();
		String type[] = {"喜剧","动作","爱情","动画","科幻","惊悚","冒险","犯罪","悬疑"};
		ArrayList<Object> typeArr = new ArrayList<Object>();
		for(int i = 0;i < type.length;i++) {
			List<Movie> movieList = this.movieService.findMoviesLikeType("",type[i],"","","");
			float boxOffice = 0;
			for(int j = 0; j < movieList.size();j++) {
				boxOffice += movieList.get(j).getMovie_boxOffice();
			}
			JSONObject typeJson = new JSONObject();
			typeJson.put(type[i], boxOffice);
			typeArr.add(typeJson);
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("count", list.size());
        map.put("data", list);
        map.put("sort", offList);
        map.put("type", typeArr);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
		
	}
	
	public void findMovieById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long movie_id = req.getParameter("movie_id") == null?1:Long.valueOf(req.getParameter("movie_id"));
		Long cinema_id = req.getParameter("cinema_id") == null?1:Long.valueOf(req.getParameter("cinema_id"));
		int date = req.getParameter("date") == null?0:Integer.valueOf(req.getParameter("date").toString());
		int page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		int limit = req.getParameter("limit") == null?5:Integer.valueOf(req.getParameter("limit"));
		Calendar calendar = Calendar.getInstance(); 
		String dateStr = "";
		if(date == 0){
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			dateStr = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}else if(date == 1){
			calendar.add(Calendar.DATE, 1);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			dateStr = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}else if(date == 2){
			calendar.add(Calendar.DATE, 2);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DATE);
			String monthStr = month < 10 ? "0" + month : month + "";
			String dayStr = day < 10 ? "0" + day : day + "";
			dateStr = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr;
		}
		Movie movie = movieService.findMovieById(movie_id);
		List<Cinema> list = cinemaService.findCinemasByMovieIdPage(movie_id,cinema_id,dateStr,page,limit);
		List<Cinema> allCinemaList = cinemaService.findAllCinemas();
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("data", movie);
        map.put("cinemaList",list);
        map.put("cinemaCount",cinemaService.findCinemasByMovieId(movie_id,cinema_id,dateStr).size());
        map.put("allCinemaList",allCinemaList);
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findMoviesByName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String name = req.getParameter("name") == null?"":req.getParameter("name").toString();

		List<Movie> list = movieService.findMoviesLikeName(name);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("data", list);
        map.put("count",list.size());
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void findMoviesByType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer page = req.getParameter("page") == null?1:Integer.valueOf(req.getParameter("page"));
		Integer limit = req.getParameter("limit") == null?10:Integer.valueOf(req.getParameter("limit"));
		String order = req.getParameter("order") == null?"":req.getParameter("order").toString();
		String type = req.getParameter("type") == null?"":req.getParameter("type").toString();
		String area = req.getParameter("area") == null?"":req.getParameter("area").toString();
		String year = req.getParameter("year") == null?"":req.getParameter("year").toString();
		String searchMovie = req.getParameter("searchMovie") == null?"":req.getParameter("searchMovie").toString();
		if("0".equals(type) || "全部".equals(type)){
			type = "";
		}
		if("0".equals(area) || "全部".equals(area)){
			area = "";
		}
		if("0".equals(year) || "全部".equals(year)){
			year = "";
		}
		List<Movie> list = movieService.findMoviesLikeTypePage(page,limit,order,type,area,year,searchMovie);
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("data", list);
        map.put("count",movieService.findMoviesLikeType(order,type,area,year,searchMovie).size());
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void sortAllMovies(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String order = req.getParameter("order") == null?"":req.getParameter("order").toString();

		List<Movie> list = new ArrayList<Movie>();
		switch (order) {
			case "热门":
				list = movieService.sortMovieByCount();
				break;
			case "时间":
				list = movieService.sortMovieByDate();
				break;
			case "评价":
				list = movieService.sortMovieByScore();
				break;
		}
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
        map.put("code", 0);
        map.put("data", list);
        map.put("count",list.size());
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void deleteMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long movie_id = req.getParameter("movie_id") == null?1:Long.valueOf(req.getParameter("movie_id"));

		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();  
		Integer rs = movieService.deleteMovie(movie_id);
		if(rs > 0) {
			map.put("code",0);
			map.put("msg","删除成功~");
		}else {
			map.put("code", 200);
			map.put("msg", "删除失败~");
		}
		
        String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void addMovie(HttpServletRequest req, HttpServletResponse resp) throws Exception  {

		Movie movie = new Movie();
		
		//创建一个解析器工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
		
        //解析请求，将表单中每个输入项封装成一个FileItem对象
        List<FileItem> fileItems = upload.parseRequest(req);
        //所有表单非文件类型的集合
        Map<String,Object> param = new HashMap<String,Object>();  
        // 迭代表单数据
        for (FileItem fileItem : fileItems) {
            //判断输入的类型是 普通输入项 还是文件
            if (!fileItem.isFormField()) {
                //上传的是文件，获得文件上传字段中的文件名
                //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名
                String fileName = fileItem.getName();
                System.out.println(fileName);
                //Substring是字符串截取，返回值是一个截取后的字符串
                //lastIndexOf(".")是从右向左查,获取.之后的字符串
                String ext = fileName.substring(fileName.lastIndexOf("."));
                //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法, UUID的唯一缺陷在于生成的结果串会比较长
                String name = UUID.randomUUID()+ext;
                //将FileItem对象中保存的主体内容保存到某个指定的文件中
    			String path = req.getRealPath("/file")+ "/upload/head/" + name;
                File file = new File(path);
            
                String filePath = "/file/upload/head/" + name;
                movie.setMovie_picture(filePath);
        		movie.setMovie_state(1);
                fileItem.write(file);
            }else{
            	param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
            }
        }
        
        movie.setMovie_cn_name(param.get("movie_cn_name") == null?"":param.get("movie_cn_name").toString());
		movie.setMovie_fg_name(param.get("movie_fg_name") == null?"":param.get("movie_fg_name").toString());
		movie.setMovie_actor(param.get("movie_actor") == null?"":param.get("movie_actor").toString());
		movie.setMovie_director(param.get("movie_director") == null?"":param.get("movie_director").toString());
		movie.setMovie_detail(param.get("movie_detail") == null?"":param.get("movie_detail").toString());
		movie.setMovie_duration(param.get("movie_duration") == null?"":param.get("movie_duration").toString());
		movie.setMovie_type(param.get("movie_type") == null?"":param.get("movie_type").toString());
		movie.setMovie_country(param.get("movie_country") == null?"":param.get("movie_country").toString());
		String movie_releaseDate = param.get("movie_releaseDate") == null?"":param.get("movie_releaseDate").toString();
		if(!"".equals(movie_releaseDate)){
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie_releaseDate);
			movie.setReleaseDate(date);
			
		}
        resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();
        Integer rs = movieService.addMovie(movie);
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "添加成功~");
		}else {
			map.put("code", 200);
			map.put("msg", "添加失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
	public void updateMovie(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		String movie_cn_name = req.getParameter("movie_cn_name") == null?"":req.getParameter("movie_cn_name").toString();
		String movie_fg_name = req.getParameter("movie_fg_name") == null?"":req.getParameter("movie_fg_name").toString();
		String movie_actor = req.getParameter("movie_actor") == null?"":req.getParameter("movie_actor").toString();
		String movie_director = req.getParameter("movie_director") == null?"":req.getParameter("movie_director").toString();
		String movie_detail = req.getParameter("movie_detail") == null?"":req.getParameter("movie_detail").toString();
		String movie_duration = req.getParameter("movie_duration") == null?"":req.getParameter("movie_duration").toString();
		String movie_type = req.getParameter("movie_type") == null?"":req.getParameter("movie_type").toString();
		String movie_releaseDate = req.getParameter("movie_releaseDate") == null?"":req.getParameter("movie_releaseDate").toString();
		String movie_country = req.getParameter("movie_country") == null?"":req.getParameter("movie_country").toString();
		Long movie_id = req.getParameter("movie_id") == null?1:Long.valueOf(req.getParameter("movie_id"));

		Movie movie = new Movie();
		movie.setMovie_id(movie_id);
		movie.setMovie_cn_name(movie_cn_name);
		movie.setMovie_fg_name(movie_fg_name);
		movie.setMovie_actor(movie_actor);
		movie.setMovie_director(movie_director);
		movie.setMovie_detail(movie_detail);
		movie.setMovie_duration(movie_duration);
		movie.setMovie_type(movie_type);
		movie.setMovie_country(movie_country);
		if(!"".equals(movie_releaseDate)){
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie_releaseDate);
			movie.setReleaseDate(date);
			
		}
		
		Boolean isFile = false;
		//创建一个解析器工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
		
        //解析请求，将表单中每个输入项封装成一个FileItem对象
        List<FileItem> fileItems = upload.parseRequest(req);
        // 迭代表单数据
        for (FileItem fileItem : fileItems) {
            //判断输入的类型是 普通输入项 还是文件
            if (!fileItem.isFormField()) {
                //上传的是文件，获得文件上传字段中的文件名
                //注意IE或FireFox中获取的文件名是不一样的，IE中是绝对路径，FireFox中只是文件名
                String fileName = fileItem.getName();
                System.out.println(fileName);
                //Substring是字符串截取，返回值是一个截取后的字符串
                //lastIndexOf(".")是从右向左查,获取.之后的字符串
                String ext = fileName.substring(fileName.lastIndexOf("."));
                //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法, UUID的唯一缺陷在于生成的结果串会比较长
                String name = UUID.randomUUID()+ext;
                //将FileItem对象中保存的主体内容保存到某个指定的文件中
    			String path = req.getRealPath("/file")+ "/upload/head/" + name;
                File file = new File(path);
            
                String filePath = "/file/upload/head/" + name;
                movie.setMovie_picture(filePath);
                fileItem.write(file);
                isFile = true;
            }
        }
        
		if(!isFile){
			Movie oldMovie = this.movieService.findMovieById(movie.getMovie_id());
			movie.setMovie_picture(oldMovie.getMovie_picture());
		}
		
		resp.setContentType("text/json; charset=utf-8");    // 设置response的编码及格式
        PrintWriter out = resp.getWriter();
        Map<String,Object> map = new HashMap<>();
		Integer rs = movieService.updateMovie(movie);
		if(rs > 0) {
			map.put("code", 0);
			map.put("msg", "修改成功~");
		}else {
			map.put("code", 200);
			map.put("msg", "修改失败~");
		}
		String resJSON = JSON.toJSONString(map);
        out.print(resJSON); // 输出
	}
	
}
