/**
 * 
 */
package com.mimpidev.dev.sql.field.condition;

/**
 * @author sbell
 *
 */
public class SqlCondition extends BaseCondition {

	/**
	 * 
	 */
	public SqlCondition() {
		super();
	}

	public SqlCondition(String conditions) {
		String key="";
		String value="";
		String currentText="";
		
		for (int stringPosition=0; stringPosition<conditions.length(); stringPosition++){
			if ((conditions.charAt(stringPosition)=='(')||
				(conditions.charAt(stringPosition)==')')||
				(conditions.charAt(stringPosition)=='\'')||
				(conditions.charAt(stringPosition)=='"')){
				System.out.println("Current Text:"+currentText);
			} else if (conditions.charAt(stringPosition)=='=') {
				key=currentText;
				System.out.println("key: "+key);
				currentText="";
			} else if (conditions.charAt(stringPosition)==' '){
				value=currentText;
				System.out.println("value: "+value);
				currentText="";
			} else {
				currentText+=conditions.charAt(stringPosition);
			}
		}
	}

}
