package com.example.demo.Service;

import com.example.demo.Dao.ProftitleDao;
import com.example.demo.Daomain.Proftitle;

import java.sql.SQLException;
import java.util.Collection;

public final class ProftitleService {
	private static ProftitleDao profTitleDao= ProftitleDao.getInstance();
	private static ProftitleService profTitleService=new ProftitleService();
	private ProftitleService(){}

	public static ProftitleService getInstance(){
		return profTitleService;
	}

	public Collection<Proftitle> getAll() throws SQLException {
		return profTitleDao.findAll();
	}

	public Proftitle find(Integer id) throws SQLException {
		return profTitleDao.find(id);
	}

	public boolean update(Proftitle profTitle) throws SQLException {
		return profTitleDao.update(profTitle);
	}

	public boolean add(Proftitle profTitle)throws SQLException{
		return profTitleDao.add(profTitle);
	}

	public boolean delete(Integer id) throws SQLException {
		return profTitleDao.delete(id);
	}

    public Collection<Proftitle> findAll() throws SQLException {
		return profTitleDao.findAll();
    }
}

