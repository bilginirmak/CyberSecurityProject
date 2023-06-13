package com.prog2.labs.service;

import java.sql.SQLException;

import com.prog2.labs.db.entity.User;
import com.prog2.labs.db.entity.User.Role;
import com.prog2.labs.exception.DBConnectionException;
import com.prog2.labs.exception.SQLExecutingException;
import com.prog2.labs.model.FormResponse;
import com.prog2.labs.model.SessionType;
import com.prog2.labs.model.Status;
import com.prog2.labs.utils.PasswordManager;

/**
 * The LoginService class.
 * 	Implements controller methods for Login UI
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class LoginService {
	
	private static final String ERROR_WRONG_PASSWORD = "Wrong password format";
	private static final String ERROR_DB = "DataBase Error";
	private static final String ERROR_LOGIN = "User doesn'r exist";
	private static final String ERROR_PASSWORD = "Incorrect password";
	private static final int PASSWORD_SIZE = 6;
	
	private DBService dbService;

	/**
	 * Constructor LoginService
	 *
	 * @param manager
	 */
	public LoginService() {
		dbService = new DBService();
	}

	/**
	 * Methos login
	 *   implements all steps to login into LIBRARY
	 *
	 * @param login
	 * @param password
	 * @return FormResponse
	 */
	public FormResponse<String> login(String login, String password) {
		
		// [1] Verify Login/password format
		if (!isPasswordFormatValid(password)) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage(ERROR_WRONG_PASSWORD);
		}

		// [2] get User by Login from DB
		User user = new User();
		try {
			user = dbService.getUserByLogin(login);
		} catch (SQLException | DBConnectionException | SQLExecutingException e) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage(ERROR_DB);
		}

		// [3] check if User exists
		if (user.getLogin() == null) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage(ERROR_LOGIN);
		}

		// [4] check Password
		if(!PasswordManager.checkPassword(password, user.getPassword())) {
			return new FormResponse<String>()
					.setStatus(Status.ERROR)
					.setMessage(ERROR_PASSWORD);
		}
		
		return new FormResponse<String>()
					.setStatus(Status.DONE);
	}

	/**
	 * Method isPasswordFormatValid
	 *   validate password formap
	 *
	 * @param password
	 * @return boolean : valid | not valid
	 */
	private boolean isPasswordFormatValid(String password) {		
		if (password.length() < PASSWORD_SIZE)
			return false;
		else
			return true; 
	}

	/**
	 * Method roleToSessionType
	 *   map Role to SessionType
	 *
	 * @param role
	 * @return SessionType
	 */
	private SessionType roleToSessionType(Role role) {
		switch(role) {
			case STUDENT : return SessionType.STUDENT;
			case LIBRARIAN : return SessionType.LIBRARY;
		}

		return SessionType.LOGIN;
	}
}
