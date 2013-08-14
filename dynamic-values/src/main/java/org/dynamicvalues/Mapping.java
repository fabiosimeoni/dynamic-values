package org.dynamicvalues;

import java.lang.reflect.Field;

/**
 * Directs the value copy to adapt values of object fields that match given criteria.
 * 
 * @author Fabio Simeoni
 * 
 */
public abstract class Mapping {

	/**
	 * Returns the value that should be copied for a given field of a given object in place of the current field value.
	 * <p>
	 * Returns <code>null</code> if the original field value is to be copied instead.
	 * 
	 * @param object the object
	 * @param field the field
	 * @param fieldValue the field value
	 * @return the value that should be copied for a given field of a given object, or <code>null</code> if the original field value is to be copied instead
	 * @throws Exception if the mapping cannot be applied
	 */
	abstract Object map(Object object, Field field, Object fieldValue) throws Exception;
}
