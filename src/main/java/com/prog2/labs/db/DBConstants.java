package com.prog2.labs.db;

/**
 * The DBConstants class.
 * 	JDBC connection parameters
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class DBConstants {

	public static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  
	public static final String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/library_db?autoReconnect=true&useSSL=false"; //verifyServerCertificate=false&useSSL=true&requireSSL=true";

	public static final String MYSQL_USER_LOGIN = "root";

	public static final String MYSQL_USER_PASSWORD = "JESUSchrist3!";
}
