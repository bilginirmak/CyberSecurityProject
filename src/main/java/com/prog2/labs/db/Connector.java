package com.prog2.labs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.prog2.labs.exception.DBConnectionException;

import static com.prog2.labs.db.DBConstants.*;

/**
 * The Connector class.
 * 	implements JDBC connection
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class Connector {

	public Connection getConnection() throws DBConnectionException {
		Connection connection = null;
		try {
			Class.forName(MYSQL_JDBC_DRIVER);
			connection = DriverManager.getConnection(MYSQL_DB_URL, MYSQL_USER_LOGIN, MYSQL_USER_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
				throw new DBConnectionException(e.getLocalizedMessage());
		}

		return connection;
	}
}
