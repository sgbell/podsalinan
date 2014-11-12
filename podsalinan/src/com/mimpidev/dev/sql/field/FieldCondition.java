/**
 * 
 */
package com.mimpidev.dev.sql.field;

/**
 * @author sbell
 *
 */
public class FieldCondition {
	   /** System will default to AND, so AND_NOT is not required*/
	
	   /** AND */
	   public final int CONDITION_AND = 0;
	   /** OR */
	   public final int CONDITION_OR  = 1;
	   /** NOT */
	   public final int CONDITION_NOT = 2;
	   /** OR NOT */
	   public final int CONDITION_OR_NOT = 3;
	   /** the value we are looking for in the database */
	   private String value;
	   /** the condition, as defined above */
	   private int condition = 0;
	
	
	/**
	 * 
	 */
	public FieldCondition() {
		
	}
    /**
     * 
     * @param newCondition
     * @param newValue
     */
	public FieldCondition(int newCondition, String newValue){
		setValue(newValue);
		setCondition(newCondition);
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
	 * @return the condition
	 */
	public int getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(int condition) {
		this.condition = condition;
	}
}
