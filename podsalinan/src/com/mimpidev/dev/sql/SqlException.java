/**
 * 
 */
package com.mimpidev.dev.sql;

/**
 * @author sbell
 *
 */
public class SqlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final long CREATE_TABLE_FAILED = 1;
	public static final long ERROR_SET_TRANSACTION_MODE = 2;
	public static final long ERROR_READING_TABLE_SCHEMA = 3;
	public static final long FAILED_ADDING_NEW_COLUMN = 4;
	public static final long FAILED_READING_RECORDS = 5;
	public static final long FAILED_SET_TABLE = 6;
	public static final long FAILED_INSERT_RECORD = 7;
	/**
	 * 
	 */
	private long errorCode=-1;

	public SqlException(){
		super();
	}
	
	public SqlException(String message) {
		super(message);
	}
	
	public SqlException(String message, Throwable cause){
		super(message, cause);
	}
	
	public SqlException(Throwable cause) {
		super(cause);
	}
	
	public SqlException(long sqlError){
		super();
		setErrorCode(sqlError);
	}

	/**
	 * @return the errorCode
	 */
	public long getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
}
