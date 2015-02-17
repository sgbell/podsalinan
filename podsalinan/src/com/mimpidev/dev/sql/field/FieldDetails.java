/**
 * 
 */
package com.mimpidev.dev.sql.field;

/**
 * @author sbell
 *
 */
public class FieldDetails {

	/**
	 * 
	 */
	private String value;
	/**
	 * 
	 */
	private int fieldType;
	/**
	 * 
	 */
	public static final int NULL = 0;
	/**
	 * 
	 */
	public static final int STRING = 1;
	/**
	 * 
	 */
	public static final int URL = 2;
	/**
	 * 
	 */
	public static final int BOOLEAN = 3;
	/**
	 * 
	 */
	public static final int INTEGER = 4;
	/**
	 * This variable defines if it will be stored in the database
	 */
	private boolean persistant=true;
	
	private boolean isPrimaryKey=false;
	/**
	 * 
	 */
	public FieldDetails(){
		value=new String();
		fieldType=0;
	}
	/**
	 * 
	 * @param persistantState
	 */
	public FieldDetails(boolean persistantState){
		this();
		setPersistent(persistantState);
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the fieldType
	 */
	public int getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isPersistent(){
		return persistant;
	}
	/**
	 * 
	 * @param persistantState
	 */
	public void setPersistent(boolean persistantState){
		persistant = persistantState;
	}
	/**
	 * @return the isprimaryKey
	 */
	public boolean isIsprimaryKey() {
		return isPrimaryKey;
	}
	/**
	 * @param isprimaryKey the isprimaryKey to set
	 */
	public void setPrimaryKey(boolean primaryKey) {
		isPrimaryKey = primaryKey;
	}
	/**
	 * @return the dbFieldType
	 */
	public String getDbFieldType() {
		switch (fieldType){
		    case STRING:
		    case URL:
		    	return "TEXT";
		    case BOOLEAN:
		    case INTEGER:
		    	return "INTEGER";
		    case NULL:
		    default:
		    	return null;
		}
	}
	
	public String toString(){
		return value;
	}
}
