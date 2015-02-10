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
	}

	/**
	 * @param message
	 */
	public SqliteException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SqliteException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SqliteException(String message, Throwable cause) {
		super(message, cause);
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
