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

	/**
	 * 
	 */
	private Map<String,FieldDetails> fields;
	protected boolean debug=false;
	/**
	 * 
	 */
	public DataRecord() {
		fields=new HashMap<String,FieldDetails>();
		put("added", new BooleanType(false));
		put("remove", new BooleanType(false));
		put("updated", new BooleanType(false));
	}
	/**
	 * 
	 * @param record
	 */
	public void populateFromRecord(Map<String, String> record){
		Iterator<Entry<String, FieldDetails>> it = getDatabaseRecord().entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, FieldDetails> pairs = it.next();
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
		return (get("added").getValue().equals("TRUE"));
	}
	/**
	 * 
	 * @param added
	 */
	public void setAdded(boolean added){
		get("added").setValue((added?"TRUE":"FALSE"));
	}
	/**
	 * 
	 * @return
	 */
	public boolean isRemoved(){
		return (get("remove").getValue().equals("TRUE"));
	}
	/**
	 * 
	 * @param removed
	 */
	public void setRemoved(boolean removed){
		get("remove").setValue((removed?"TRUE":"FALSE"));
	}
	/**
	 * @return the updated
	 */
	public boolean isUpdated() {
		return (get("updated").getValue().equals("TRUE"));
	}
	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(boolean updated) {
		get("updated").setValue((updated?"TRUE":"FALSE"));;
	}
	/**
	 * 
	 * @return
	 */
	public Map<String,FieldDetails> getDatabaseRecord(){
		Map<String,FieldDetails> databaseRecord = new HashMap<String,FieldDetails>();
		synchronized(fields){
			Iterator<Entry<String, FieldDetails>> it=fields.entrySet().iterator();
			while (it.hasNext()){
				Entry<String, FieldDetails> pairs = it.next();
				if (((FieldDetails)pairs.getValue()).isPersistent())
					databaseRecord.put((String)pairs.getKey(), (FieldDetails)pairs.getValue());
			}
		}
		return databaseRecord;
	}
	
	public FieldDetails get(String key){
		synchronized(fields){
			return fields.get(key);
		}
	}
	
	public FieldDetails put(String key, FieldDetails value){
		synchronized(fields){
			return fields.put(key, value);
		}
	}

	/**
	 * @return the fields
	 */
	public Map<String,FieldDetails> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Map<String,FieldDetails> fields) {
		this.fields = fields;
	}
}
