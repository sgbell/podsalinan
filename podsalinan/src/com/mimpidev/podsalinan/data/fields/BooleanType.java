/**
 * 
 */
package com.mimpidev.podsalinan.data.fields;


/**
 * @author sbell
 *
 */
public class BooleanType extends FieldDetails{
	
	/**
	 * 
	 */
	public BooleanType() {
		setFieldType(FieldDetails.BOOLEAN);
	}

	/**
	 * 
	 * @param newValue
	 */
	public void setValue(boolean newValue){
		if (newValue)
			setValue("TRUE");
		else
			setValue("FALSE");
	}
}
