/**
 * 
 */
package com.mimpidev.dev.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mimpidev.dev.sql.field.BooleanType;
import com.mimpidev.dev.sql.field.FieldDetails;

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
	 * @param record
	 */
	public void populateFromRecord(Map<String, String> record){
		Iterator<Entry<String, FieldDetails>> it = getDatabaseRecord().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, FieldDetails> pairs = (Map.Entry<String,FieldDetails>)it.next();
			if (record.containsKey(pairs.getKey())){
				if (pairs.getValue().getFieldType()==FieldDetails.BOOLEAN)
					if (record.get(pairs.getKey())!=null)
						((BooleanType)pairs.getValue()).setValue(record.get(pairs.getKey()).equalsIgnoreCase("1"));
					else
						((BooleanType)pairs.getValue()).setValue(false);
				else
					if (record.get(pairs.getKey())!=null)
						pairs.getValue().setValue(record.get(pairs.getKey()).replaceAll("&apos;", "\'"));
			}
		}
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
