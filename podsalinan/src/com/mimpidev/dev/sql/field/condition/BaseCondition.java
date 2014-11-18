/**
 * 
 */
package com.mimpidev.dev.sql.field.condition;

/**
 * @author sbell
 *
 */
public class BaseCondition {

    private BaseCondition next;
    private BaseCondition child;
	/**
	 * 
	 */
	public BaseCondition() {
	}
	/**
	 * @return the next
	 */
	public BaseCondition getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(BaseCondition next) {
		this.next = next;
	}
	/**
	 * @return the child
	 */
	public BaseCondition getChild() {
		return child;
	}
	/**
	 * @param child the child to set
	 */
	public void setChild(BaseCondition child) {
		this.child = child;
	}

}
