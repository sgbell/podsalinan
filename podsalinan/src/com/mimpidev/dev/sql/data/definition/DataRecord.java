/**
 * 
 */
package com.mimpidev.dev.sql.data.definition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mimpidev.dev.sql.data.definition.field.BooleanType;
import com.mimpidev.dev.sql.data.definition.field.FieldDetails;

/**
 * @author bugman
 *
 */
public class DataRecord {

	protected Map<String,FieldDetails> fields;
	/**
	 * 
	 */
	public DataRecord() {
		fields=new HashMap<String,FieldDetails>();
		fields.put("added", new BooleanType(false));
		fields.put("remove", new BooleanType(false));
		fields.put("updated", new BooleanType(false));
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAdded(){
		return (fields.get("added").getValue().equals("TRUE"));
	}
	/**
	 * 
	 * @param added
	 */
	public void setAdded(boolean added){
		fields.get("added").setValue((added?"TRUE":"FALSE"));
	}
	/**
	 * 
	 * @return
	 */
	public boolean isRemoved(){
		return (fields.get("remove").getValue().equals("TRUE"));
	}
	/**
	 * 
	 * @param removed
	 */
	public void setRemoved(boolean removed){
		fields.get("remove").setValue((removed?"TRUE":"FALSE"));
	}
	/**
	 * @return the updated
	 */
	public boolean isUpdated() {
		return (fields.get("updated").getValue().equals("TRUE"));
	}
	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(boolean updated) {
		fields.get("updated").setValue((updated?"TRUE":"FALSE"));;
	}
	/**
	 * 
	 * @return
	 */
	public Map<String,FieldDetails> getDatabaseRecord(){
		Map<String,FieldDetails> databaseRecord = new HashMap<String,FieldDetails>();
		Iterator<Entry<String, FieldDetails>> it=fields.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			if (((FieldDetails)pairs.getValue()).isPersistent())
				databaseRecord.put((String)pairs.getKey(), (FieldDetails)pairs.getValue());
		}
		return databaseRecord;
	}
}