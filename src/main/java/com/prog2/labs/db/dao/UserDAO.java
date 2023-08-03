package com.prog2.labs.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.prog2.labs.db.Connector;
import com.prog2.labs.db.entity.User;
import com.prog2.labs.db.entity.User.Role;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;

/**
 * The UserDAO class 
 * 		extends Connector 
 * 		implements GenericDAO
 * 	Implements CRUD methods for User DB Entity
 * 	used JDBC
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class UserDAO extends Connector implements GenericDAO<User> {
	
	private Connection connection;

	@Override
	public void add(User user) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO USERS "
				+ "(NAME, ROLE, LOGIN, PASSWORD, CONTACT) "
				+ "VALUES(?,?,?,?,?)";

		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getRole().toString());
			preparedStatement.setString(3, user.getLogin());
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.setString(5, user.getContact());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLExecutingException(e.getLocalizedMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	@Override
	public List<User> getAll() 
			throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		Statement statement = null;

		List<User> userList = new ArrayList<>();
		
		String sql = "SELECT USER_ID, NAME, ROLE, LOGIN, PASSWORD, "
				+ "CONTACT, DATE_CREATED FROM USERS";
		
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql); 
			
			while (resultSet.next()) {
				User user = new User();
				
				user.setUser_id(resultSet.getInt("USER_ID"));
				user.setName(resultSet.getString("NAME"));

				user.setRole( (resultSet.getString("ROLE").toUpperCase().equals(Role.STUDENT.toString()) ) 
						? Role.STUDENT : Role.LIBRARIAN);
				
				user.setLogin(resultSet.getString("LOGIN"));
				user.setPassword(resultSet.getString("PASSWORD"));
				user.setContact(resultSet.getString("CONTACT"));
				user.setDate_created(resultSet.getString("DATE_CREATED"));  

				userList.add(user);
				
				// Lambda expression to print a List
				//userList.forEach( (n) -> {System.out.println(n); });

			}
		} catch (SQLException e) {
			throw new SQLExecutingException(e.getLocalizedMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return userList;
	}

	@Override
	public User getById(int id) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "SELECT USER_ID, NAME, ROLE, LOGIN, PASSWORD, CONTACT, DATE_CREATED "
				+ "FROM USERS WHERE USER_ID=?";
		
		User user = new User();
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				user.setUser_id(resultSet.getInt("USER_ID"));
				user.setName(resultSet.getString("NAME"));

				user.setRole( (resultSet.getString("ROLE").toUpperCase().equals(Role.STUDENT.toString()) ) 
						? Role.STUDENT : Role.LIBRARIAN);
				
				user.setLogin(resultSet.getString("LOGIN"));
				user.setPassword(resultSet.getString("PASSWORD"));
				user.setContact(resultSet.getString("CONTACT"));
				user.setDate_created(resultSet.getString("DATE_CREATED"));
			}
		} catch (SQLException e) {
			throw new SQLExecutingException(e.getLocalizedMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return user;
	}

	@Override
	public void update(User user) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "UPDATE USERS SET NAME=?, ROLE=?, "
				+ "LOGIN=?, PASSWORD=?, CONTACT=? WHERE USER_ID=?";
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getRole().toString());
			preparedStatement.setString(3, user.getLogin());
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.setString(5, user.getContact());
			preparedStatement.setInt(6, user.getUser_id());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLExecutingException(e.getLocalizedMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	@Override
	public void deleteById(int id) throws DBConnectionException, SQLException, SQLExecutingException {
		// TODO Auto-generated method stub
		
	}
}
