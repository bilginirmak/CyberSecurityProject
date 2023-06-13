package com.prog2.labs.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.prog2.labs.db.Connector;
import com.prog2.labs.db.entity.Book;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;

/**
 * The BookDAO class 
 * 		extends Connector 
 * 		implements GenericDAO
 * 	Implements CRUD methods for Book DB Entity
 * 	used JDBC
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class BookDAO extends Connector implements GenericDAO<Book> {

	private Connection connection;

	@Override
	public void add(Book book) throws DBConnectionException, SQLException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;

		String sql = "INSERT INTO BOOKS "
				+ "(ISBN, TITLE, AUTHOR, PUBLISHER, PRICE, QUANTITY) "
				+ "VALUES(?,?,?,?,?,?)";

		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, book.getIsbn());
			preparedStatement.setString(2, book.getTitle());
			preparedStatement.setString(3, book.getAuthor());
			preparedStatement.setString(4, book.getPublisher());
			preparedStatement.setDouble(5, book.getPrice());
			preparedStatement.setInt(6, book.getQuantity());


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
	public List<Book> getAll() throws DBConnectionException, SQLException, SQLExecutingException {
		connection = getConnection();
		Statement statement = null;

		List<Book> bookList = new ArrayList<>();

		String sql = "SELECT BOOK_ID,ISBN,TITLE,AUTHOR,PUBLISHER,PRICE,QUANTITY,DATE_ADDED FROM BOOKS";

		try {
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql); 

			while (resultSet.next()) {
				Book book = new Book();
				book.setBook_id(resultSet.getInt("BOOK_ID"));
				book.setIsbn(resultSet.getString("ISBN"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setPublisher(resultSet.getString("PUBLISHER"));
				book.setPrice(resultSet.getDouble("PRICE"));
				book.setQuantity(resultSet.getInt("QUANTITY"));
				book.setDateAdded(resultSet.getString("DATE_ADDED"));

				bookList.add(book);
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

		return bookList;
	}

	@Override
	public Book getById(int id) throws DBConnectionException, SQLException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;

		String sql = "SELECT BOOK_ID,ISBN,TITLE,AUTHOR,PUBLISHER,PRICE,QUANTITY,DATE_ADDED "
				+ "FROM BOOKS WHERE BOOK_ID=?";

		Book book = new Book();

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				book.setBook_id(resultSet.getInt("BOOK_ID"));
				book.setIsbn(resultSet.getString("ISBN"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setPublisher(resultSet.getString("PUBLISHER"));
				book.setPrice(resultSet.getDouble("PRICE"));
				book.setQuantity(resultSet.getInt("QUANTITY"));
				book.setDateAdded(resultSet.getString("DATE_ADDED"));
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

		return book;
	}

	@Override
	public void update(Book book) throws DBConnectionException, SQLException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;

		String sql = "UPDATE BOOKS SET ISBN=?, TITLE=?, "
				+ "AUTHOR=?, PUBLISHER=?, PRICE=? , QUANTITY=?"
				+ "WHERE BOOK_ID=?";
		
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, book.getIsbn());
			preparedStatement.setString(2, book.getTitle());
			preparedStatement.setString(3, book.getAuthor());
			preparedStatement.setString(4, book.getPublisher());
			preparedStatement.setDouble(5, book.getPrice());
			preparedStatement.setInt(6, book.getQuantity());
			preparedStatement.setInt(7, book.getBook_id());

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
