package webLibraryREST;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.prog2.labs.model.Catalog;
import com.prog2.labs.db.entity.Book;
import com.prog2.labs.db.entity.User;
import com.prog2.labs.db.entity.Issued;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.ResponseContainer;
import com.prog2.labs.service.DBService;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.FormParam;

@Path("WebLibraryGet")
public class WebLibraryResourceGET {

	private DBService dbs = new DBService();
	
	/**
	 * Book Catalog REST Text
	 * @return
	 */
	@GET
	@Path("/getCatalogText")
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
	 * Book Catalog REST HTML
	 * @return
	 */
	@GET
	@Path("/getCatalogHTML")
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
	 * Book Catalog REST JSON
	 * @return
	 */
	@GET
	@Path("/getCatalogJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public Catalog getCatalogJSON() {
		
		List<Book> books = new ArrayList<>();
		dbs = new DBService();
		
		try {
			books = dbs.getBookCatalog();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			e.printStackTrace();
		}
		
		return new Catalog(books);
	}

	/**
	 * All Issued Books to one User REST JSON
	 * Method shows all Issued Books to ONE user by it's userID
	 * @param user_id
	 * @return
	 */
	@GET
	@Path("/booksToReturnPP/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResponseContainer<Issued, Book, String>> toReturnByIdJSONPP(
			@PathParam("user") int user_id
			) 
	{
	
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


	@POST
	@Path("/test")
	@Produces(MediaType.TEXT_HTML)
	public String test(
			@FormParam("isbn") String isbn,
			@FormParam("test") String test,
			@FormParam("test1") String test1) 
	{
	
		String result;
		result = test;
		//if test fixed
		if(!test1.isEmpty())
		{
			String[] values = test1.split(" ");
	        int length = values.length;
	        String[] escapseValues = new String[length];
	        for(int i = 0;i<length;i++){
	            
escapseValues[i] = Jsoup.clean(values[i], Whitelist.relaxed()).trim();

	            if(!StringUtils.equals(escapseValues[i],values[i])){
System.out.println("Input："+values[i]+"\t"+"Output："+escapseValues[i]);
	            }
	        }

	        StringBuilder stringBuilder = new StringBuilder();

	        for (String str : escapseValues) {
	            stringBuilder.append(str);
	        }
	        
	        result = stringBuilder.toString();
		}
	
		return result;
	}

	
	@GET
	@Path("/toReturnQP/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResponseContainer<Issued, Book, String>> toReturnJSONQP(
			@QueryParam("user") int user_id
			) 
	{
	
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
	 * Book Info REST HTML by ISBN
	 * @return
	 */
	@GET
	@Path("/bookInfo/{isbn}")
	@Produces(MediaType.TEXT_HTML)
	public String getBookInfoHTMLPP(
			@PathParam("isbn") String isbn
			) 
	{
	
		String result;
		Book book;
	
		//dbs = new DBService();
		try {
			book = dbs.getByISBN(isbn);
			result = composeBookHTMLResponse(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error";
		}
	
		return result;
	}

	/**
	 *Method get book by ISBN  JSON. PathParam
	 */
	@GET
	@Path("/bookInfoJSONPP/{isbn}")
	@Produces(MediaType.APPLICATION_JSON)
	private Book getBookByISBNJSONPP(    // ....
			@PathParam("isbn") String isbn) { 
		//valid ISBN  0711264228
		List<Book> catalog = getCatalog();
		Book bookResult = null;
		
		for(Book book: catalog) {
			if (book.getIsbn().equals(isbn)) { 
				bookResult = book;	
				break;
			}
		}
	
		return bookResult;
	}

	/**
	 * Method get book by ISBN  JSON. QueryParam
	 * @param book_ISBN
	 * @return
	 */
	@GET
	@Path("/bookInfoQP")
	@Produces(MediaType.APPLICATION_JSON)
	private String getBookInfoJSONQP( //.....
			@QueryParam("isbn") String isbn
			) { 
		
		//valid ISBN  0711264228
		List<Book> catalog = getCatalog();
		Book bookResult = null;
		
		for(Book book: catalog) {
			if (book.getIsbn().equals(isbn)) { 
				bookResult = book;				
			}
		}
	
		return "GO!"; // bookResult;
	}
	
	/**
	 * Method returns All Available Books Catalog REST JSON
	 * @return
	 */
	@GET
	@Path("/searchBook/")
	@Produces(MediaType.APPLICATION_JSON)
	public List <Book>  searchBookJSONQP(  
			@QueryParam ("isbn") String isbn,
			@QueryParam ("title") String title,
			@QueryParam ("author") String author,
			@QueryParam ("publisher") String publisher
			) 
	{
		
		List <Book> catalog = new ArrayList<>();
		dbs = new DBService();
		
		try {
			catalog = dbs.getAvailableBooks(isbn, title, author, publisher);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
	
			e.printStackTrace(); 
		}
		
		return catalog;
	}

	@GET
	@Path("/getUserByLogin")
	@Produces(MediaType.APPLICATION_JSON)
	private User getUserByLoginQP(
			@QueryParam("login") String login
			) { 
		User user = new User();
		try {
			user = dbs.getUserByLogin(login);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			String str = "Database Error " + e.getLocalizedMessage();
		}
	
		return user;
	}

	/**
	 * Method add New Book GET
	 * @return
	 */
	@GET 
	@Path("/addBook")
	@Produces(MediaType.TEXT_HTML)
	public String addBookQP (
			@QueryParam("isbn") String isbn,
			@QueryParam("title") String title,
			@QueryParam("author") String author,
			@QueryParam("price") double price,
			@QueryParam("quantity") int quantity
			)
	{
		String result;
		Book book = new Book();
		book.setIsbn(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setPrice(price);
		book.setQuantity(quantity);
		book.setPublisher("");

		dbs = new DBService();
		try {
			dbs.addBook(book);
			result = composeBookHTMLResponse(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error " + e.getLocalizedMessage() + " ";
		}
		getCatalogHTML();
		
		return result;
	}
	
	/**
	 * Method update New Book GET
	 * @return
	 */
	@PUT 
	@Path("/updateBook")
	@Produces(MediaType.TEXT_HTML)
	public String updateBookQP (
			@QueryParam("isbn") String isbn,
			@QueryParam("title") String title,
			@QueryParam("author") String author,
			@QueryParam("price") int price,
			@QueryParam("quantity") int quantity,
			@QueryParam("publisher") String publisher
			)
	{
		String result;
		
		Book book = new Book();
		book.setIsbn(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setPrice(price);
		book.setQuantity(quantity);
		book.setPublisher(publisher);

		dbs = new DBService();
		try {
			dbs.updateBookByISBN(book);
			result = composeBookHTMLResponse(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error " + e.getMessage();
		}

		String html = getCatalogHTML();
		
		return html; //"<html><body> Isbn "  + isbn + " Title " 
		//+ title + "Author " + author + " price " 
		//+ price+ " quantity " + quantity + "</body></html>";
	}
	
	
	/**
	 * Method delete Book
	 * @return
	 */
	@DELETE 
	@Path("/deleteBook")
	@Produces(MediaType.TEXT_HTML)
	public String deleteBookQP (
			@QueryParam("isbn") String isbn
			)
	{
		String result = "";

		try {
			dbs.deleteBookByISBN(isbn);
			//result = composeBookHTMLResponse(book);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			result = "Database Error";
		}
		result = getCatalogHTML();
		
		return result;
	}
	
	@GET
	@Path("/booksToReturnQP")
	@Produces(MediaType.APPLICATION_JSON)
	public String toReturnByIdJSONQP(
			@QueryParam("user") int user_id
			) 
	{

		List<ResponseContainer<Issued, Book, String>> issuedById = new ArrayList<>();

		dbs = new DBService();
		try {
			issuedById = dbs.getIssuedToUser(user_id);

		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			ResponseContainer<Issued, Book, String> error = new ResponseContainer<>();
			error.setThird("DB Error");
			issuedById.add(error);
		}

		return issuedById.toString();
	}
	
	
	/**
	 * Method prepares HTML to show Book info from the link
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
	
	private List<Book> getCatalog() {
		List<Book> catalog = new ArrayList<>();
		//dbs = new DBService();
		
		try {
			catalog = dbs.getBookCatalog();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			//result = "Database Error";
			System.out.println("Error DB connection");
		}
		
		return catalog;
	}

	/*
	private List<User> getUserCatalog() {
		List<User> users = new ArrayList<>();
		dbs = new DBService();
		
		try {
			users = dbs.get.getBookCatalog();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			//result = "Database Error";
			System.out.println("Error DB connection");
		}
		
		return users;
	}
	*/
}
