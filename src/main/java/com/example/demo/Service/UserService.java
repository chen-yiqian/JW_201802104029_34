package com.example.demo.Service;

import com.example.demo.Dao.UserDao;
import com.example.demo.Daomain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll() throws SQLException {
		return userDao.findAll();
	}
	
	public User find(Integer id)throws SQLException {
		return userDao.find(id);
	}
	public User findByUsername(String username)throws SQLException {
		return userDao.findByUsername(username);
	}
	public boolean update(User user) throws SQLException {
		return userDao.update(user);
	}

	public boolean deleteUser(Integer id) throws SQLException {
		return userDao.delete(id);
	}
	
	public User login(String username, String password) throws SQLException {
		return userDao.login(username,password);
	}	
}
