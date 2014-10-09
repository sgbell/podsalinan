/**
 * 
 */
package com.mimpidev.dev.sql.data.definition.field;


/**
 * @author sbell
 *
 */
public class BooleanType extends FieldDetails{
	
	/**
	 * 
	 */
	public BooleanType() {
		super();
		setFieldType(FieldDetails.BOOLEAN);
	}
	public BooleanType(boolean persistance){
		this();
		setPersistent(persistance);
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
