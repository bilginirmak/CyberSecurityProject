package com.prog2.labs.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.prog2.labs.db.entity.Book;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.db.entity.User;
import com.prog2.labs.db.entity.User.Role;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.FormResponse;
import com.prog2.labs.model.ResponseContainer;
import com.prog2.labs.model.Status;
import com.prog2.labs.utils.ISBNValidator;
import com.prog2.labs.utils.PasswordManager;

/**
 * The LibraryService class.
 * 	Implements controller methods for Library UI
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class LibraryService {
	
	private static final int PASSWORD_LENGTH = 6;

	private DBService dbs;

	public LibraryService() {
		dbs = new DBService();
	}

	/**
	 * Method add new Book to Catalog
	 *
	 * @param isbn
	 * @param title
	 * @param author
	 * @param publisher
	 * @param price
	 * @param quantity
	 * @return FormResponse
	 */
	public FormResponse<String> addBook(String isbn, String title, 
			String author, String publisher, String price, String quantity) {

		// [1] Validate input parameters
		if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() 
				|| publisher.isEmpty() || price.isEmpty() || quantity.isEmpty()) {

			return new FormResponse<String>()
						.setStatus(Status.ERROR)
						.setMessage("Fill in all fields");
		}
		
		// [2] Validate ISBN
		if (!ISBNValidator.check(isbn)) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("ISBN is not valid");
		}

		// {3} search ISBN in DB if present
		try {
			Book book = dbs.getByISBN(isbn);
			if (book.getIsbn() != null) {
				return new FormResponse<String>()
						.setStatus(Status.ERROR)
						.setMessage("This ISBN already exist in catalog ");
			}
		} catch (DBConnectionException | SQLException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("DataBase exception");
		}
	
		// [4] create book
		Book book = new Book(isbn, title, author, publisher, Double.valueOf(price), Integer.valueOf(quantity));
		
		// [5] add book into db
		try {
			dbs.addBook(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("Book can't be added - DataBase exception");
		}

		return new FormResponse<String>()
					.setStatus(Status.DONE);
	}
	
	/**
	 * Method adds new User to the catalog
	 * 
	 * @param name
	 * @param role
	 * @param login
	 * @param password
	 * @param contact
	 * @return FormResponse
	 */
	public FormResponse<String> addUser(String name, Role role, String login, String password, String contact) {

		// [1] Validate input parameters
		if (name.isEmpty() || login.isEmpty() || password.isEmpty() || contact.isEmpty() ) {

			return new FormResponse<String>()
						.setStatus(Status.ERROR)
						.setMessage("Fill in all fields"); 
		}

		// [2] search login in DB if present
		try {
			User user = dbs.getUserByLogin(login.toLowerCase());
			if (user.getLogin() != null) {
				return new FormResponse<String>()
						.setStatus(Status.ERROR)
						.setMessage("This login already exist");
			}
		} catch (DBConnectionException | SQLException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("DataBase exception");
		}

		// [3] Check password is valid
		if (password.length() < PASSWORD_LENGTH) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("password lenght is not valid");
		}

		// [4] encrypt password		
		String encPassword = PasswordManager.encryptPassword(password);

		// [4] Create User
		User user = new User(name, role,  login.toLowerCase(),  encPassword,  contact);

		// [4] Add User to Catalog		
		try {
			dbs.addUser(user);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("User can't be added - DataBase exception");
		}
		
		return new FormResponse<String>()
				.setStatus(Status.DONE);
	}

	/**
	 *  Method transforms enum Role to Strin array
	 * @return
	 */
	public String[] enumRoleToStringArray() {
		String[] arrayRole = new String[2];
		int i = 0;

		for(Role ro: Role.values()) {
			arrayRole[i] = ro.toString();
			i++;
		}

		return arrayRole;
	}

	public FormResponse<String[][]> getArrayOfAllBooks(String isbn, String title, String author, String publisher) {

		List<Book> books;

		// getAll Books from db
		try {
			books = dbs.searchBooks(isbn, title, author, publisher);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String[][]>()
						.setStatus(Status.ERROR)
						.setMessage("DataBase exception");
		}

		// transform List<Book> to String[][]		
		return new FormResponse<String[][]>()
				.setStatus(Status.DONE)
				.setValue(listBookToString(books));
	}

	private String[][] listBookToString(List<Book> listCatalog) {

		String[][] arrayBooks = new String[listCatalog.size()][7];
		int i = 0;

		for (Book book : listCatalog) {
			arrayBooks[i][0] = String.valueOf(book.getBook_id());
			arrayBooks[i][1] = book.getIsbn();
			arrayBooks[i][2] = book.getTitle();
			arrayBooks[i][3] = book.getAuthor();
			arrayBooks[i][4] = book.getPublisher();
			arrayBooks[i][5] = String.valueOf(book.getPrice()); 
			arrayBooks[i][6] = String.valueOf(book.getQuantity());

			i++;
		}
		
		return arrayBooks;
	}

	/**
	 * Method updates Book quantity in Books Catalog
	 * @param bookId
	 * @param quantity
	 * @return
	 */
	public FormResponse<String> updateBookQuantity(String bookId, String quantity) {

		try {
			dbs.updateBookQuantityByID(Integer.valueOf(bookId), Integer.valueOf(quantity));
		} catch (DBConnectionException | SQLException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("Book can't be updated - DataBase exception");
		}

		return new FormResponse<String>()
				.setStatus(Status.DONE);
	}

	public FormResponse<String[][]> getArrayToReturnBooks() {
		List<ResponseContainer<Issued, Book, User>> responseContainer;

		// getToReturn Books from db
		try {
			responseContainer = dbs.getIssuedTransactions();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String[][]>()
						.setStatus(Status.ERROR)
						.setMessage("DataBase exception");
		}

		// transform List<Book> to String[][]		
		return new FormResponse<String[][]>()
				.setStatus(Status.DONE)
				.setValue(listToReturnBooksToString(responseContainer));
	}

	public String[][] listToReturnBooksToString(List<ResponseContainer<Issued, Book, User>> responseContainers) {

		String[][] arrayIssued = new String[responseContainers.size()][9];
		int i = 0;

		for (ResponseContainer<Issued, Book, User> responseContainer : responseContainers) {

			arrayIssued[i][0] = String.valueOf(responseContainer.getFirst().getIssued_id());
			arrayIssued[i][1] = responseContainer.getSecond().getIsbn();
			arrayIssued[i][2] = responseContainer.getSecond().getTitle();
			arrayIssued[i][3] = responseContainer.getSecond().getAuthor();
			arrayIssued[i][4] = responseContainer.getSecond().getPublisher();
			arrayIssued[i][5] = String.valueOf(responseContainer.getThird().getUser_id());
			arrayIssued[i][6] = responseContainer.getThird().getName();
			arrayIssued[i][7] = responseContainer.getThird().getContact();
			arrayIssued[i][8] = responseContainer.getFirst().getDate_issued();

			i++;
		}
		
		return arrayIssued;
	}

	public FormResponse<String> updateIssuedToReturned(String issuedId) {
		Issued issued = new Issued();
		issued.setIssued_id(Integer.valueOf(issuedId));

		try {
			dbs.updateIssuedToReturned(issued);
		} catch (DBConnectionException | SQLException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("Database Update exception");
		}
		
		return new FormResponse<String>()
				.setStatus(Status.DONE);
	}

	/**
	 * Method get Array Of Available Books From Catalog
	 * @param isbn
	 * @param title
	 * @param author
	 * @param publisher
	 * @return
	 */
	public FormResponse<String[][]> getArrayOfAvailableBooks(String isbn, String title, String author, String publisher) {

		List<Book> books;

		// getAvailable Books from db
		try {
			books = dbs.getAvailableBooks(isbn, title, author, publisher);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String[][]>()
						.setStatus(Status.ERROR)
						.setMessage("DataBase exception");
		}

		// transform List<Book> to String[][]		
		return new FormResponse<String[][]>()
				.setStatus(Status.DONE)
				.setValue(listBookToString(books));
	}
	
	public FormResponse<String[][]> getArrayOfUsers(String user_id, String name, String contact) {
		List<User> users = null;
		try {
			users = dbs.getUserCatalogWithFilter(user_id, name, contact);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String[][]>()
						.setStatus(Status.ERROR)
						.setMessage("DataBase exception");
		}
		return new FormResponse<String[][]>()
				.setStatus(Status.DONE)
				.setValue(listUsersToString(users));
	}

	public String[][] listUsersToString(List<User> listUser) {

		String[][] arrayUsers = new String[listUser.size()][3];
		int i = 0;
		
		for (User user : listUser) {

			arrayUsers[i][0] = String.valueOf(user.getUser_id());
			arrayUsers[i][1] = user.getName();
			arrayUsers[i][2] = user.getContact();

			i++;
		}
		
		return arrayUsers;
	}
	
	public FormResponse<String> issueBook(String book_id, String user_id) {
		Issued issued = new Issued();
		issued.setBook_id(Integer.valueOf(book_id));
		issued.setUser_id(Integer.valueOf(user_id));
		issued.setStatus(Issued.Status.ISSUED);

		try {
			dbs.issueBook(issued);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String>()
						.setStatus(Status.ERROR)
						.setMessage("DataBase exception");
		}

		return new FormResponse<String>()
					.setStatus(Status.DONE);
	}
}
