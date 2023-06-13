package com.prog2.labs.exception;

/**
 * The SQL Executing Exception class
 *   throws for any SQL processing problem
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class SQLExecutingException extends Exception {

	private static final long serialVersionUID = 1L;
	static final String ERROR_MESSAGE = "SQL Executing is failed. ";

	public SQLExecutingException(String message) {
		super(ERROR_MESSAGE + message);
	}
}
