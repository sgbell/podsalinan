/**
 * 
 */
package com.mimpidev.podsalinan.data.fields;


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
