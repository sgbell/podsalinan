/**
 * 
 */
package com.mimpidev.dev.sql.field;


/**
 * @author sbell
 *
 */
public class StringType extends FieldDetails {

	/**
	 * 
	 */
	public StringType() {
		setFieldType(FieldDetails.STRING);
	}

	public StringType(String newValue) {
		this();
		setValue(newValue);
	}
}
