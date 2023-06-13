package com.prog2.labs.service;

import java.sql.SQLException;
import java.util.List;

import com.prog2.labs.db.dao.BookDAO;
import com.prog2.labs.db.dao.ExtendedDAO;
import com.prog2.labs.db.dao.GenericDAO;
import com.prog2.labs.db.dao.IssuedDAO;
import com.prog2.labs.db.dao.UserDAO;
import com.prog2.labs.db.entity.Book;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.db.entity.User;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.ResponseContainer;

/**
 * The DBService class.
 * 	Wrapper for all DAO classes
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class DBService {

	private GenericDAO<Book> bookDAO = new BookDAO();
	private GenericDAO<User> userDAO = new UserDAO();
	private GenericDAO<Issued>issuedDAO = new IssuedDAO();
	private ExtendedDAO extendedDAO = new ExtendedDAO();

	/**
	 * Method adds book with quantity to Book Catalog
	 * @param newBook
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public void addBook(Book newBook) throws SQLException, DBConnectionException, SQLExecutingException {
		bookDAO.add(newBook);
	}

	/**
	 * Method issues book to ActiveUser
	 * @param book
	 * @param studId
	 * @return
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public void issueBook(Issued issuedBook) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		issuedDAO.add(issuedBook);
	}

	/** 
	 * Method return book
	 * 
	 * @param book
	 * @param studId
	 * @return
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public void returnBook(Issued issued) throws SQLException, DBConnectionException, SQLExecutingException {
		issuedDAO.update(issued);
	}

	/**
	 * Method filter books in BookCatalog  by isbn/title/author/publisher 
	 * 
	 * @param isbn
	 * @param title
	 * @param author
	 * @param publisher
	 * @return List<Book>
	 * @throws SQLException
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<Book> searchBooks(String isbn, String title, String author, String publisher) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.searchBy(isbn, title, author, publisher);
	}

	/**
	 * Method shows Books Catalog
	 * @return
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<Book> getBookCatalog() 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return bookDAO.getAll();
	}
	
	/**
	 * Method shows a List of Available Books
	 * @return
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<Book> getAvailableBooks(String isbn, String title, String author, String publisher) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.getAvailableBooks(isbn,  title, author, publisher);
	}

	/**
	 * Method get All issued books
	 * @return List<Issued>
	 * @throws SQLException
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<ResponseContainer<Issued, Book, User>> getIssuedTransactions() 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.getIssuedTransactions();
	}
	
	/**
	 * Method get Issued To ActiveUser
	 *  retrieve issued transactions for user by userID
	 * 
	 * @param userID
	 * @return List<Issued>
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<ResponseContainer<Issued, Book, String>> getIssuedToUser(int userID) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.getIssuedToUser(userID);
	}
		
	/**
	 * Method updates Book information and quantity
	 * @param newBook
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public void updateBook(Book newBook) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		bookDAO.update(newBook);
	}
	
	/**
	 * Method adds new user
	 * @param newUser
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 * @throws SQLException 
	 */
	public void addUser(User newUser) 
			throws DBConnectionException, SQLExecutingException, SQLException {
		userDAO.add(newUser);
	}
	
	/** 
	 * Method  shows all users
	 * @return
	 * @throws SQLException 
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 */
	public List<User> getUserCatalogWithFilter(String user_id, String name, String contact) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.getUsersWithFilter(user_id, name, contact);
	}
	
	/**
	 * Method get ActiveUser by ID
	 * @param user_id
	 * @return
	 * @throws SQLExecutingException 
	 * @throws DBConnectionException 
	 * @throws SQLException 
	 */
		public User searchUserByID(int user_id) 
				throws DBConnectionException, SQLExecutingException, SQLException {
			return userDAO.getById(user_id);
		}
	/**
	 * Method get User by login
	 * 
	 * @param login
	 * @return
	 * @throws SQLException
	 * @throws DBConnectionException
	 * @throws SQLExecutingException
	 */
	public User getUserByLogin(String login) 
			throws SQLException, DBConnectionException, SQLExecutingException {
		return extendedDAO.getUserByLogin(login);
	}

	public Book getByISBN(String isbn) throws DBConnectionException, SQLException, SQLExecutingException {
		return extendedDAO.getByISBN(isbn);
	}
	
	public void updateBookQuantityByID(int bookId, int quantity) 
			throws DBConnectionException, SQLException, SQLExecutingException {
		extendedDAO.updateBookQuantityByID(bookId, quantity);
	}
	
	public void updateIssuedToReturned(Issued issued) 
			throws DBConnectionException, SQLException, SQLExecutingException {
		issuedDAO.update(issued);
	}

	public User getUserById(int userId) 
			throws DBConnectionException, SQLException, SQLExecutingException {
		return userDAO.getById(userId);
	}
}
