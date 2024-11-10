package com.movie.service;

import java.util.ArrayList;
import java.util.List;

import com.movie.bean.User;
import com.movie.dao.UserMapper;

public class UserService{
	
	private UserMapper usermapper = new UserMapper();
	
	public User login(String user_name, String user_pwd) {
		List<User> userList = usermapper.findUserByName(user_name);
		for(User user : userList) {
			if(user.getUser_pwd().equals(user_pwd)) {
				return user;
			}
		}
		return null;
	}
	
	public Integer updateUserInfo(User user) {
		return this.usermapper.updateUser(user);
	}
	
	public User findUserById(long user_id) {
		return this.usermapper.findUserById(user_id);
	}
	
	public List<User> findUserByName(String name) {	
		return this.usermapper.findUserByName(name);
	}
	
	public Integer addUser(User user) {
		return this.usermapper.addUser(user);
	}

	public Integer deleteUser(long user_id) {
		return this.usermapper.deleteUser(user_id);
	}
	
	public List<User> findAllUserBySplitPage(Integer page, Integer limit, String keyword) {
		int startPage = (page - 1) * limit;
		int endPage = limit;
		List<User> list = new ArrayList<User>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.usermapper.findUserLikeNamePage(startPage,endPage,keyword);
		}else {
			list = this.usermapper.findAllUserPage(startPage,endPage);
		}
		return list;
	}
	
	public int findAllUserBySplitPageCount(String keyword) {
		List<User> list = new ArrayList<User>();
		if(keyword != null && !keyword.trim().equals("")) {
			list = this.usermapper.findUserLikeName(keyword);
		}else {
			list = this.usermapper.findAllUser();
		}
		return list.size();
	}

	public List<User> findAllUserInfos() {
		return this.usermapper.findAllUser();
	}

	public List<User> findUserLikeName(String name) {
		return this.usermapper.findUserLikeName(name);
	}
	
}
