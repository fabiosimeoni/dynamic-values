package org.dynamicvalues;

import java.lang.reflect.Field;

/**
 * Directs the value copy to exclude object fields that match given criteria.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class ExcludeDirective {

	/**
	 * Returns <code>true</code> if a given field inside a given object should be excluded from the value copy.
	 * @param object the object
	 * @param field the field
	 * @return  <code>true</code> if a given field inside a given object should be excluded from the value copy
	 * @throws Exception if the exclusion cannot be applied
	 */
	abstract boolean exclude(Object object,Field field) throws Exception;
}
