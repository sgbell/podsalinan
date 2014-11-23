/**
 * 
 */
package com.mimpidev.dev.sql.field.condition;

/**
 * @author sbell
 *
 */
public class FieldCondition extends BaseCondition {

	String field;
	String value;
	
	/**
	 * 
	 */
	public FieldCondition() {
		super();
	}
	
	public FieldCondition(String key, String newValue) {
		this();
		field = key;
		value = newValue;
	}
}
