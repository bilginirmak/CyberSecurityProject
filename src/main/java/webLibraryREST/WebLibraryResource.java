package webLibraryREST;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.prog2.labs.db.entity.Book;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.ResponseContainer;
import com.prog2.labs.service.DBService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("WebLibrary")
public class WebLibraryResource {

	private DBService dbs;

	/**
	 * Book Catalog REST HTML
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getCatalogHTML() {

		String result;
		List<Book> catalog = new ArrayList<>();

		dbs = new DBService();
		try {
			catalog = dbs.getBookCatalog();
			result = composeHTMLResponse(catalog);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error";
			System.out.println("Error DB connection");
		}

		return result;
	}

	/**
	 * Book Info REST HTML
	 * @return
	 */
	@GET
	@Path("/bookInfo/{isbn}")
	@Produces(MediaType.TEXT_HTML)
	public String getBookInfoHTML(@PathParam("isbn") String isbn) {

		String result;
		Book book;

		dbs = new DBService();
		try {
			book = dbs.getByISBN(isbn);
			result = composeBookHTMLResponse(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error";
		}

		return result;
	}

	/**
	 * Method prepares HTML to show Book's details
	 * @param book
	 * @return
	 */
	private String composeBookHTMLResponse(final Book book) {

		String html = "<table border='0'><tr>"
				+ "<td colspan=2> Book </td></tr>"
				+ "<tr><td>Isbn</td><td>"+book.getIsbn()+"</tb></tr>"
				+ "<tr><td>Title</td><td>"+book.getTitle()+"</tb></tr>"
				+ "<tr><td>Author</td><td>"+book.getAuthor()+"</tb></tr>"
				+ "<tr><td>Price</td><td>"+book.getPrice()+"</tb></tr>"
				+ "<tr><td>Quantity</td><td>"+book.getQuantity()+"</tb></tr>"
				+ "<tr><td>Date Added</td><td>"+book.getDateAdded()+"</tb></tr>"
				+ "<tr><td>BookInfo</td><td>"+book.toString()+"</tb></tr>"
				+ "</table>"

				+"<br><br>"
				+"<a href=\"http://localhost:8080/LibraryManagementSystem/rest/WebLibrary\"> < Go back to Catalog </a>";

		return html;
	}

	/**
	 * Book Catalog REST Text
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCatalogText() {

		String result = "";
		List<Book> catalog = new ArrayList<>();

		dbs = new DBService();
		try {
			catalog = dbs.getBookCatalog();
			for (Book book : catalog)
				result += book.toString();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error";
			System.out.println("Error DB connection");
		}

		return result;
	}


	/**
	 * All Issued Books to one User REST JSON
	 * Method shows all Issued Books to ONE user by it's userID
	 * @param user_id
	 * @return
	 */
	@GET
	@Path("/booksToReturn/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResponseContainer<Issued, Book, String>> getCatalogJSON(@PathParam("user") int user_id) {

		List<ResponseContainer<Issued, Book, String>> issuedById = new ArrayList<>();

		dbs = new DBService();
		try {
			issuedById = dbs.getIssuedToUser(user_id);

		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			ResponseContainer<Issued, Book, String> error = new ResponseContainer<>();
			error.setThird("DB Error");
			issuedById.add(error);
		}

		return issuedById;
	}

	/**
	 * Method creates Book Catalog table
	 * @param catalog
	 * @return
	 */
	private String composeHTMLResponse(List<Book> catalog) {

		String html = "<table border='1'><tr>"
				+ "<td colspan=7> Book Catalog </td></tr>"
				+ "<tr><td>Isbn</td>"
				+ "<td>Title</td>"
				+ "<td>Author</td>"
				+ "<td>Publisher</td>"
				+ "</tr>"
				+ composeBookLines(catalog)
				+ "</table>";

		return html;
	}

	/**
	 * Method creates lines for the Book Catalog table
	 * @param catalog
	 * @return
	 */
	private String composeBookLines(List<Book> catalog) {
		String bookLines = "";
		for(Book book: catalog) {
			bookLines += "<tr><td>"
					+ "<a href=\"http://localhost:8080/LibraryManagementSystem/rest/WebLibrary/bookInfo/"
					+book.getIsbn()
					+ "\">"
					+book.getIsbn()+"</a></td>";
			bookLines += "<td>"+book.getTitle()+"</td>";
			bookLines += "<td>"+book.getAuthor()+"</td>";
			bookLines += "<td>"+book.getPublisher()+"</td>";
			bookLines += "</tr>";
		}

		return bookLines;
	}

}
