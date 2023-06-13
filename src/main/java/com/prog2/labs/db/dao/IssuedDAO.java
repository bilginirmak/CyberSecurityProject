package com.prog2.labs.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.prog2.labs.db.Connector;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.db.entity.Issued.Status;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;

/**
 * The IssuedDAO class 
 * 		extends Connector 
 * 		implements GenericDAO
 * 	Implements CRUD methods for Issued DB Entity
 * 	used JDBC
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class IssuedDAO extends Connector implements GenericDAO<Issued> {
	
	private Connection connection;

	@Override
	public void add(Issued issued) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "INSERT INTO ISSUED "
				+ "(BOOK_ID, USER_ID, STATUS) "
				+ "VALUES(?,?,?)";

		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, issued.getBook_id());
			preparedStatement.setInt(2, issued.getUser_id());
			preparedStatement.setString(3, issued.getStatus().toString());

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
	public List<Issued> getAll() throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		
		Statement statement = null;
		List<Issued> issuedList = new ArrayList<>();		
		String sql = "SELECT ISSUED_ID, BOOK_ID, USER_ID, STATUS, DATE_ISSUED, DATE_RETURNED FROM ISSUED";
		
		try {
			statement = connection.createStatement();			
			ResultSet resultSet = statement.executeQuery(sql); 			
			while (resultSet.next()) {
				Issued issued = new Issued();

				issued.setIssued_id((resultSet.getInt("ISSUED_ID")));
				issued.setBook_id((resultSet.getInt("BOOK_ID")));
				issued.setUser_id((resultSet.getInt("USER_ID")));
				issued.setStatus( (resultSet.getString("STATUS").toUpperCase().equals(Status.ISSUED.toString()) ) 
						? Status.ISSUED : Status.RETURNED);				
				issued.setDate_issued(resultSet.getString("DATE_ISSUED"));
				issued.setDate_returned(resultSet.getString("DATE_RETURNED"));

				issuedList.add(issued);
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

		return issuedList;
	}

	@Override
	public Issued getById(int id) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		
		PreparedStatement preparedStatement = null;
		String sql = "SELECT ISSUED_ID, BOOK_ID, USER_ID, STATUS, DATE_ISSUED, DATE_RETURNED "
				+ "FROM ISSUED WHERE ISSUED_ID=?";
		Issued issued = new Issued();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				issued.setIssued_id((resultSet.getInt("ISSUED_ID")));
				issued.setBook_id((resultSet.getInt("BOOK_ID")));
				issued.setUser_id((resultSet.getInt("USER_ID")));
				issued.setStatus( (resultSet.getString("STATUS").toUpperCase().equals(Status.ISSUED.toString()) ) 
					? Status.ISSUED : Status.RETURNED);				
				issued.setDate_issued(resultSet.getString("DATE_ISSUED"));
				issued.setDate_returned(resultSet.getString("DATE_RETURNED")); 
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

		return issued;
	}

	@Override
	public void update(Issued issued) throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "UPDATE ISSUED SET STATUS='returned' "
				+ "WHERE ISSUED_ID=?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, issued.getIssued_id());			
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

}
