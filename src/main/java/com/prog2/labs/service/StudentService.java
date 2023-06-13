package com.prog2.labs.service;

import java.sql.SQLException;
import java.util.List;

import com.prog2.labs.db.entity.Book;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.FormResponse;
import com.prog2.labs.model.ResponseContainer;
import com.prog2.labs.model.Status;

/**
 * The StudentService class.
 * 	Implements controller methods for Student UI
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class StudentService {
	
	private DBService dbs;
	
	public StudentService() {
		dbs = new DBService();
	}

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
	
	public FormResponse<String> borrowBook(String book_id, String user_id) {
		Issued issued = new Issued(Integer.valueOf(book_id), Integer.valueOf(user_id), Issued.Status.ISSUED);
		
		try {
			dbs.issueBook(issued);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage("Database exeption");
		}

		return new FormResponse<String>()
				.setStatus(Status.DONE);
	}
	
	public FormResponse<String[][]> getArrayOfBooksUserToReturn(String userId) {
		
		List<ResponseContainer<Issued, Book, String>> responseContainers;
		try {
			responseContainers = dbs.getIssuedToUser(Integer.valueOf(userId));
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String[][]>()
					.setStatus(Status.ERROR)
					.setMessage("Database exeption");
		}
		
		return new FormResponse<String[][]>()
				.setStatus(Status.DONE)
				.setValue(listToReturnBooksToString(responseContainers));
	}

	public String[][] listToReturnBooksToString(List<ResponseContainer<Issued, Book, String>> responseContainers) {

		String[][] arrayIssued = new String[responseContainers.size()][5];
		int i = 0;
		
		for (ResponseContainer<Issued, Book, String> responseContainer: responseContainers) {

			arrayIssued[i][0] = String.valueOf(responseContainer.getFirst().getIssued_id());
			arrayIssued[i][1] = responseContainer.getSecond().getIsbn();
			arrayIssued[i][2] = responseContainer.getSecond().getTitle();
			arrayIssued[i][3] = responseContainer.getSecond().getAuthor();
			arrayIssued[i][4] = responseContainer.getFirst().getDate_issued();

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
}
