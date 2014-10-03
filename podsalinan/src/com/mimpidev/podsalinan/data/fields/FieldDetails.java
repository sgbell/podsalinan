/**
 * 
 */
package com.mimpidev.podsalinan.data.fields;

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
	public static final int NULL = 0;
	public static final int STRING = 1;
	public static final int URL = 2;
	public static final int BOOLEAN = 3;
	/**
	 * 
	 */
	public FieldDetails(){
		value=new String();
		fieldType=0;
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
}
