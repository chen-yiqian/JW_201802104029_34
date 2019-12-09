package com.example.demo.Dao;

import com.example.demo.Daomain.Teacher;
import com.example.demo.Daomain.User;
import com.example.demo.Service.TeacherService;
import com.example.demo.Util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;
	public Collection<User> findAll()throws SQLException{
		//创建一个HashSet对象，并把它加到set
		Set users=new HashSet<User>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet= statement.executeQuery("select * from user");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()){
			Teacher teacher=TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			//获得数据库的信息，并用于在网页中显示
			User user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					teacher);
			//添加到集合中
			users.add(user);
		}
		return users;
	}
	
	public User find(Integer id)throws SQLException{
		User user = null;
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String findUser="select * from user where id=?";
		//在该连接上创建预编译语句
		PreparedStatement preparedStatement=connection.prepareStatement(findUser);
		//为预编译语句赋值
		preparedStatement.setInt(1,id);
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet=preparedStatement.executeQuery();
		//若结果集仍有下一条记录，则执行循环体
		if(resultSet.next()){
			Teacher teacher= TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
			//获得数据库的信息，并用于在网页中显示
			user=new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					teacher);
		}
		//关闭
		JdbcHelper.close(preparedStatement,connection);
		return user;
	}

	public User findByUsername(String username)throws SQLException{
		User user = null;
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String findUser="select * from user where username=?";
		//在该连接上创建预编译语句
		PreparedStatement preparedStatement=connection.prepareStatement(findUser);
		preparedStatement.setString(1,username);
		ResultSet resultSet=preparedStatement.executeQuery();
		if(resultSet.next()){
			Teacher teacher= TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
			user=new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					teacher);
		}
		JdbcHelper.close(preparedStatement,connection);
		return user;
	}
	public boolean update(User user)throws SQLException {
		//获得连接对象
		Connection connection= JdbcHelper.getConn();
		//创建sql语句
		String updateuser="update user set password=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement preparedStatement=connection.prepareStatement(updateuser);
		//为预编译的语句参数赋值
		preparedStatement.setString(1,user.getPassword());
		preparedStatement.setInt(2,user.getId());
		//执行预编译对象的executeUpdate()方法，获取修改记录的行数
		int affected=preparedStatement.executeUpdate();
		//关闭
		JdbcHelper.close(preparedStatement,connection);
		return affected>0;
	}

	public User login(String username, String password) throws SQLException {
	    //获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String loginsql="select * from user where username=? and password=?";
		//在该连接上创建预编译语句
		PreparedStatement preparedStatement=connection.prepareStatement(loginsql);
		//为预编译语句赋值
		preparedStatement.setString(1,username);
		preparedStatement.setString(2,password);
		ResultSet resultSet=preparedStatement.executeQuery();
		User user=null;
		if(resultSet.next()){
			Teacher teacher=TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
			user=new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					teacher);
		}
		JdbcHelper.close(preparedStatement,connection);
		return user;
	}

	public boolean delete(Integer id)throws SQLException{
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String deleteUser_sql="DELETE FROM user WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt=connection.prepareStatement(deleteUser_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum=pstmt.executeUpdate();
		//关闭
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
	
	
}
