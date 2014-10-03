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

	public String getValue(){
		return super.getValue();
	}
	
	public void setValue(String newValue){
		super.setValue(newValue);
	}
}
