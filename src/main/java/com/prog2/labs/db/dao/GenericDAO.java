package com.prog2.labs.db.dao;

import java.sql.SQLException;
import java.util.List;

import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;

/**
 * The GenericDAO interface 
 * 	parameterized T
 * 	Declares CRUD methods for <T> DB Entity
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public interface GenericDAO<T> {
	//create
	public void add(T unit) throws DBConnectionException, SQLException, SQLExecutingException;

	//read
	public List<T> getAll() throws DBConnectionException, SQLException, SQLExecutingException;

	//read by id
	public T getById(int id) throws DBConnectionException, SQLException, SQLExecutingException;

	//update
	public void update(T unit) throws DBConnectionException, SQLException, SQLExecutingException;

}
