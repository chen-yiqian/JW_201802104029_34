package com.example.demo.Dao;

import com.example.demo.Daomain.Degree;
import com.example.demo.Daomain.Department;
import com.example.demo.Daomain.Proftitle;
import com.example.demo.Daomain.Teacher;
import com.example.demo.Service.DegreeService;
import com.example.demo.Service.DepartmentService;
import com.example.demo.Service.ProftitleService;
import com.example.demo.Util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
//git@github.com:chen-yiqian/JW_201802104029_31.git
//https://github.com/chen-yiqian/JW_201802104029_31.git
public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		//创建一个HashSet对象，并把它加到set
		Set teachers=new HashSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet= statement.executeQuery("select * from teacher");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()){
			Proftitle proftitle= ProftitleDao.getInstance().find(resultSet.getInt("proftitle_id"));
			Degree degree=DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
			Department department=DepartmentDao.getInstance().find(resultSet.getInt("department_id"));
			//获得数据库的信息，并用于在网页中显示
			Teacher teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					proftitle,
					degree,
					department);
			//添加到集合中
			teachers.add(teacher);
		}
		return teachers;
	}
	
	public Teacher find(Integer id)throws SQLException{
		Teacher teacher = null;
		//获得连接对象
        Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String findteacher="select * from teacher where id=?";
		//在该连接上创建预编译语句对象
        PreparedStatement preparedStatement=connection.prepareStatement(findteacher);
		//为预编译语句赋值
        preparedStatement.setInt(1,id);
		//创建ResultSet对象，执行预编译语句
        ResultSet resultSet=preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
        if(resultSet.next()){
            Proftitle proftitle= ProftitleService.getInstance().find(resultSet.getInt("proftitle_id"));
            Degree degree= DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department= DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//获得数据库的信息
            teacher=new Teacher(
                    resultSet.getInt("id"),
                    resultSet.getString("no"),
                    resultSet.getString("name"),
                    proftitle,
                    degree,
                    department
            );
        }
		//关闭
        JdbcHelper.close(preparedStatement,connection);
		return teacher;
	}


	public boolean update(Teacher teacher)throws SQLException{
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String updateteacher="update teacher set no=?,name=?,proftitle_id=?,degree_id=?,department_id=? where id=?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement preparedStatement=connection.prepareStatement(updateteacher);
		//为预编译的语句参数赋值
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getProftitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		//执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affected=preparedStatement.executeUpdate();
        //关闭
		JdbcHelper.close(preparedStatement,connection);
		return affected>0;
	}
	
	public boolean add(Teacher teacher)throws SQLException{
		//声明变量
		PreparedStatement pstmt=null;
		Connection connection=null;
		Boolean affected = false;
        int  teacher_id = 0;
        try{
			//获得连接对象
			connection=JdbcHelper.getConn();
			//关闭自动提交
			connection.setAutoCommit(false);
			//创建sql语句
			String addTeacher_sql = "insert into teacher(no,name,proftitle_id,degree_id,department_id) values " + "(?,?,?,?,?)";
			//在该连接上创建预编译语句对象
			pstmt = connection.prepareStatement(addTeacher_sql);
			//为预编译参数赋值
			pstmt.setString(1,teacher.getNo());
			pstmt.setString(2,teacher.getName());
			pstmt.setInt(3,teacher.getProftitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());
			//执行预编译对象的executeUpdate方法，获取添加的记录行数
			int affectedRowNum=pstmt.executeUpdate();
			System.out.println("teacher添加了"+affectedRowNum+"行");
			//在该连接上执行sql语句
			pstmt =connection.prepareStatement("select * from teacher where id=(select max(id) from teacher)");
			//创建ResultSet对象，执行预编译语句
			ResultSet resultSet=pstmt.executeQuery();
			if (resultSet.next()){
                teacher_id = resultSet.getInt("id");
            }
            pstmt = connection.prepareStatement("insert into user(username,password,teacher_id) values " + "(?,?,?)");
            //为预编译参数赋值
            pstmt.setString(1,teacher.getNo());
            pstmt.setString(2,teacher.getNo());
            pstmt.setInt(3,teacher_id);
            //执行预编译对象的executeUpdate方法，获取添加的记录行数
            int affectedRowNum2 =pstmt.executeUpdate();
            //关闭
            connection.commit();
			affected=affectedRowNum2>0;
		}catch (Exception e){
			try{
				//回滚当前连接所做的操作
				if(connection!=null){
					//事务以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try{
				//恢复自动提交
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}
		return affected;
	}

	public boolean delete(Integer id) throws SQLException {
		//获得连接对象
		Connection connection=null;
		PreparedStatement preparedStatement = null;
		Boolean affected=false;
		try{
			//获得连接对象
			connection=JdbcHelper.getConn();
			//关闭自动提交
			connection.setAutoCommit(false);
			//创建语句
			preparedStatement=connection.prepareStatement("SELECT * FROM user where teacher_id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				UserDao.getInstance().delete(resultSet.getInt("id"));
			}

			//创建sql语句
			String deleteTeacher_sql="DELETE FROM teacher WHERE id=?";
			//在该连接上创建预编译语句对象
			preparedStatement=connection.prepareStatement(deleteTeacher_sql);
			//为预编译参数赋值
			preparedStatement.setInt(1,id);
			//执行预编译对象的executeUpdate()方法，获取删除记录的行数
			int affectedRowNum=preparedStatement.executeUpdate();
			//执行当前连接所做的操作
			connection.commit();
			affected=affectedRowNum>0;
		} catch (SQLException e) {
			System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
			try{
				//回滚当前连接所做的操作
				if(connection!=null){
					//事务以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try{
				//恢复自动提交
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
		}
		return affected;
	}

}

