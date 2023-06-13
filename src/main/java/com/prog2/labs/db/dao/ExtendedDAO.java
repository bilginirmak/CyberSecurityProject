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
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.db.entity.Issued.Status;
import com.prog2.labs.db.entity.User.Role;
import com.prog2.labs.db.entity.User;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.ResponseContainer;

/**
 * The ExtendedDAO class 
 * 		extends Connector
 * 	Implements extended methods for all DB Entity
 * 	used JDBC
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class ExtendedDAO extends Connector {
	
	private static final String LIKE_D = "%";
	
	private Connection connection;{}

	/**
	 * Method search by
	 * 
	 * @param isbn
	 * @param title
	 * @param author
	 * @param publisher
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException 
	 * @throws SQLExecutingException 
	 */
	public List<Book> searchBy(String isbn, String title, String author, String publisher) 
			throws SQLException, DBConnectionException, SQLExecutingException {

		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		List<Book> bookList = new ArrayList<>();
		
		String sql = "SELECT BOOK_ID, ISBN, TITLE, AUTHOR, PUBLISHER, PRICE, QUANTITY, DATE_ADDED "
				+ "FROM BOOKS "
				+ "WHERE ISBN LIKE ? AND TITLE LIKE ? AND AUTHOR LIKE ? AND PUBLISHER LIKE ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, LIKE_D + isbn + LIKE_D);
			preparedStatement.setString(2, LIKE_D + title + LIKE_D);
			preparedStatement.setString(3, LIKE_D + author + LIKE_D);
			preparedStatement.setString(4, LIKE_D + publisher + LIKE_D);

			ResultSet resultSet = preparedStatement.executeQuery();
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
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

		return bookList;
	}
	
	/**
	 *  Available books
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException 
	 * @throws SQLExecutingException 
	 */
	public List<Book> getAvailableBooks(String isbn, String title, String author, String publisher) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;

		List<Book> bookList = new ArrayList<>();

		String sql = "SELECT B.BOOK_ID, B.ISBN, B.TITLE, B.AUTHOR, "
				+ "B.PUBLISHER, B.PRICE, "
				+ "(B.QUANTITY - IFNULL(I.ISSUED,0)) AS AVAILABLE "
				+ "FROM BOOKS B LEFT JOIN ( "
				+ "SELECT BOOK_ID, COUNT(BOOK_ID) AS ISSUED "
				+ "FROM ISSUED "
				+ "WHERE STATUS = 'ISSUED' "
				+ "GROUP BY BOOK_ID "
				+ ") AS I "
				+ "ON B.BOOK_ID = I.BOOK_ID "
				+ "HAVING AVAILABLE > 0 "
				+ "AND ISBN LIKE ? AND TITLE LIKE ? AND AUTHOR LIKE ? AND PUBLISHER LIKE ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, LIKE_D + isbn + LIKE_D);
			preparedStatement.setString(2, LIKE_D + title + LIKE_D);
			preparedStatement.setString(3, LIKE_D + author + LIKE_D);
			preparedStatement.setString(4, LIKE_D + publisher + LIKE_D);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Book book = new Book();
				book.setBook_id(resultSet.getInt("BOOK_ID"));
				book.setIsbn(resultSet.getString("ISBN"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setPublisher(resultSet.getString("PUBLISHER"));
				book.setPrice(resultSet.getDouble("PRICE"));
				//book.setQuantity(resultSet.getInt("QUANTITY"));
				//book.setDateAdded(resultSet.getString("DATE_ADDED"));

				bookList.add(book);
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

		return bookList;
	}

	/**
	 * Method get ALL Issued Books
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException 
	 * @throws SQLExecutingException 
	 */
	public List<ResponseContainer<Issued, Book, User>> getIssuedTransactions() 
			throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		Statement statement = null;

		List<ResponseContainer<Issued, Book, User>> issuedBookList = new ArrayList<>();

		String sql = "SELECT I.ISSUED_ID, I.STATUS, I.USER_ID, i.BOOK_ID, U.NAME, U.CONTACT, B.ISBN, B.TITLE, B.AUTHOR, B.PUBLISHER, I.DATE_ISSUED "
				+ "FROM ISSUED I "
				+ "LEFT JOIN BOOKS B ON I.BOOK_ID=B.BOOK_ID "
				+ "LEFT JOIN USERS U ON I.USER_ID=U.USER_ID "
				+ "WHERE I.STATUS='ISSUED'";

		try {
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql); 
			
			while (resultSet.next()) {
				ResponseContainer<Issued, Book, User> rc = new ResponseContainer<>();

				Issued issued = new Issued();
				issued.setIssued_id(resultSet.getInt("ISSUED_ID"));  
				issued.setStatus(resultSet.getString("STATUS").toUpperCase().equals(Status.ISSUED.toString())  
						? Status.ISSUED : Status.RETURNED);
				issued.setUser_id(resultSet.getInt("USER_ID"));
				issued.setBook_id(resultSet.getInt("BOOK_ID"));
				issued.setDate_issued(resultSet.getString("DATE_ISSUED")); 

				Book book = new Book();
				book.setBook_id(resultSet.getInt("BOOK_ID"));
				book.setIsbn(resultSet.getString("ISBN"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setPublisher(resultSet.getString("PUBLISHER"));

				User user = new User();
				user.setUser_id(resultSet.getInt("USER_ID"));
				user.setName(resultSet.getString("NAME"));
				user.setContact(resultSet.getString("CONTACT"));

				rc.setFirst(issued);
				rc.setSecond(book);
				rc.setThird(user);

				issuedBookList.add(rc);
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

		return issuedBookList;
	}
	
	/**
	 * Books issued to one user , search by userID
	 * @param userID
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException 
	 * @throws SQLExecutingException 
	 */
	public List<ResponseContainer<Issued, Book, String>> getIssuedToUser(int userID) 
			throws SQLException, DBConnectionException, SQLExecutingException {

		connection = getConnection();
		PreparedStatement preparedStatement = null;

		List<ResponseContainer<Issued, Book, String>> issuedBookList = new ArrayList<>();
		
		String sql = "SELECT I.ISSUED_ID, I.STATUS, I.USER_ID, I.BOOK_ID,  B.ISBN, B.TITLE, B.AUTHOR, B.PUBLISHER, I.DATE_ISSUED "
				+ "FROM ISSUED I "
				+ "LEFT JOIN BOOKS B ON I.BOOK_ID=B.BOOK_ID "
				+ "WHERE I.USER_ID=? AND I.STATUS='ISSUED'";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userID); 

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				ResponseContainer<Issued, Book, String> rc = new ResponseContainer<>();

				Issued issued = new Issued();				
				issued.setIssued_id(resultSet.getInt("ISSUED_ID"));  
				issued.setStatus(resultSet.getString("STATUS").toUpperCase().equals(Status.ISSUED.toString())
						? Status.ISSUED : Status.RETURNED);
				issued.setUser_id(resultSet.getInt("USER_ID"));
				issued.setBook_id(resultSet.getInt("BOOK_ID"));

				Book book = new Book();
				book.setBook_id(resultSet.getInt("BOOK_ID"));
				book.setIsbn(resultSet.getString("ISBN"));
				book.setTitle(resultSet.getString("TITLE"));
				book.setAuthor(resultSet.getString("AUTHOR"));
				book.setPublisher(resultSet.getString("PUBLISHER"));
				issued.setDate_issued(resultSet.getString("DATE_ISSUED")); 

				rc.setFirst(issued);
				rc.setSecond(book);
				issuedBookList.add(rc);
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

		return issuedBookList;
	}

	/**
	 * Method 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException
	 * @throws SQLExecutingException
	 */
	public User getUserByLogin(String login) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "SELECT USER_ID, NAME, ROLE, LOGIN, PASSWORD, CONTACT, DATE_CREATED "
				+ "FROM USERS WHERE LOGIN=?";
		
		User user = new User();
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, login);
			
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

	public Book getByISBN(String isbn) 
			throws DBConnectionException, SQLException, SQLExecutingException {

		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "SELECT BOOK_ID,ISBN,TITLE,AUTHOR,PUBLISHER,PRICE,QUANTITY,DATE_ADDED "
				+ "FROM BOOKS WHERE ISBN=?";
		
		Book book = new Book();
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, isbn);
			
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
	
	public void updateBookQuantityByID(int bookId, int quantity) 
			throws DBConnectionException, SQLException, SQLExecutingException {

		connection = getConnection();
		PreparedStatement preparedStatement = null;
		
		String sql = "UPDATE BOOKS SET QUANTITY=? "
				   + "WHERE BOOK_ID=?";
		
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, quantity);
			preparedStatement.setInt(2, bookId);
			
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

	public List<User> getUsersWithFilter(String user_id, String name, String contact) 
			throws SQLException, DBConnectionException, SQLExecutingException {

		connection = getConnection();
		PreparedStatement preparedStatement = null;

		List<User> userList = new ArrayList<>();

		String sql = "SELECT USER_ID, NAME, CONTACT "
					+ "FROM USERS "
					+ "WHERE USER_ID LIKE ? AND NAME LIKE ? AND CONTACT LIKE ?";
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, LIKE_D + user_id + LIKE_D);
			preparedStatement.setString(2, LIKE_D + name + LIKE_D);
			preparedStatement.setString(3, LIKE_D + contact + LIKE_D);
			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) {
				User user = new User();
				user.setUser_id(resultSet.getInt("USER_ID"));
				user.setName(resultSet.getString("NAME"));
				user.setContact(resultSet.getString("CONTACT"));

				userList.add(user);
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

		return userList;
	}
}

