package webLibrarySOAP;

import java.sql.SQLException;

import com.prog2.labs.db.entity.User;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;

import org.json.JSONObject;
import org.json.XML;

import com.prog2.labs.service.DBService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding (style=Style.RPC)
public class WebLibraryServiceSOAP {
	
	private DBService dbs = new DBService();
	
	
	@WebMethod
	public String displayHello(String name) {
		
		return "Hello my friend " + name;
	}
	
	/**
	 * Method get User by login
	 * @param login
	 * @return
	 */
	@WebMethod
	public User getUser(String login) {

		User user = null;
		try {
			user = dbs.getUserByLogin(login);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {

			e.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * Method shows Books Catalog
	 * @return
	 */
	@WebMethod
	public String getCatalog() {
		
		String catalog = "";
		try {
			catalog = dbs.getBookCatalog().toString();
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {

			e.printStackTrace();
		}
		
		return catalog;
	}
	
	/**
	 * Method transform JSON to XML
	 * @param js
	 * @return
	 */
	private String jsonToXML(String js) {
		
		JSONObject json = new JSONObject(js);
		String xml = XML.toString(json);
		
		return xml;
	}
}
