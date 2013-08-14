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
	 * Returns the value that should be copied for a given field inside a given object.
	 * @param object the object
	 * @param field the field
	 * @return  the value that should be copied for a given field inside a given object
	 * @throws Exception if the exclusion cannot be applied
	 */
	abstract Object map(Object parent,Field field, Object value) throws Exception;
}
