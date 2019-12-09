package com.example.demo.Daomain;

import java.io.Serializable;

public class User  implements Comparable<User>,Serializable{
	private Integer id;
	private String username;
	private String password;
	private Teacher teacher;

	public User(Integer id,
				   String username,
				   String password,
				   Teacher teacher) {
		this(username, password, teacher);
		this.id = id;

	}
	public User(String username, String password,
			Teacher teacher) {
		super();
		this.username = username;
		this.password = password;
		this.teacher = teacher;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}

}
