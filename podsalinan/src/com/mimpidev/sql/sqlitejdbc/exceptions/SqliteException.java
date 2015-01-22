/**
 * 
 */
package com.mimpidev.sql.sqlitejdbc.exceptions;

/**
 * @author sbell
 *
 */
public class SqliteException extends Exception {

	private String sqlStatement; 
	/**
	 * 
	 */
	public SqliteException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SqliteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SqliteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SqliteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SqliteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SqliteException(String message, String sqlStatement) {
		super(message);
		setStatement(sqlStatement);
	}

	/**
	 * @return the sqlStatement
	 */
	public String getStatement() {
		return sqlStatement;
	}

	/**
	 * @param sqlStatement the sqlStatement to set
	 */
	public void setStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

}
