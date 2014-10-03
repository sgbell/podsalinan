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

	public boolean getValue(){
		return (field.getValue().equals("TRUE"));
	}
	/**
	 * 
	 * @param newValue
	 */
	public void setValue(boolean newValue){
		if (newValue)
			field.setValue("TRUE");
		else
			field.setValue("FALSE");
	}
}
