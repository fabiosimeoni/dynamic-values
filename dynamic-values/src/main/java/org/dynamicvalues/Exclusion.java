package org.dynamicvalues;

import java.lang.reflect.Field;

/**
 * Directs the value copy to exclude object fields that match given criteria.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class Exclusion {

	/**
	 * Returns <code>true</code> to exclude a given field of a given object from the value copy of the object.
	 * @param object the object
	 * @param field the field
	 * @return  <code>true</code> to exclude a given field of a given object from the value copy of the object
	 * @throws Exception if the exclusion cannot be applied
	 */
	abstract boolean exclude(Object object,Field field) throws Exception;
}
