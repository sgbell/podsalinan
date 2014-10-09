/**
 * 
 */
package com.mimpidev.dev.sql.data.definition.field;


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
