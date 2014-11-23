/**
 * 
 */
package com.mimpidev.dev.sql.field.condition;

public class SqlCondition extends BaseCondition{
	private BaseCondition conditionList;
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
				(conditions.charAt(stringPosition)=='"')){
				System.out.println("Current Text:"+currentText);
			} else if (conditions.charAt(stringPosition)=='=') {
				key=currentText;
				System.out.println("key: "+key);
				currentText="";
			} else if (conditions.charAt(stringPosition)==' '){
				value=currentText;
				System.out.println("value: "+value);
				if (key.length()>0){
					//conditionMap.put(key,value);
					if (conditionList == null)
						conditionList = new FieldCondition(key,value);
					else
						conditionList.add(new FieldCondition(key,value));
					key = "";
				} else if (key.length()==0){
					System.out.println(value);
				}
				value="";
				currentText="";
			} else if (conditions.charAt(stringPosition)!='\''){
				currentText+=conditions.charAt(stringPosition);
			}
		}
	}
}
