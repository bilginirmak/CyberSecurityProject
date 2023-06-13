package com.prog2.labs.exception;

/**
 * The DB Connection Exception class
 *   throws for any connection problem
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class DBConnectionException extends Exception {

	private static final long serialVersionUID = 1L;
	static final String ERROR_MESSAGE = "DataBase Connection is failed. ";

	public DBConnectionException(String message) {
		super(ERROR_MESSAGE + message);
	}

}
