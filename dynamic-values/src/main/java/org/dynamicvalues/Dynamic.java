package org.dynamicvalues;

import static org.dynamicvalues.Excludes.*;

/**
 * Dynamic value factory.
 * <p>
 * An object is a <em>dynamic value</em> if its graph contains only:
 * 
 * <li>primitives, primitive wrappers, or strings;
 * <li>lists of dynamic values (<em>dynamic lists</em>);
 * <li>maps whose keys and values are dynamic (<em>dynamic maps</em>);
 * 
 * This factory takes an arbitrary object graph and returns its <am>value copy</em>, i.e. a "copy" in which:
 * 
 * <li>maps and instances of user-defined types are replaced with dynamic maps;
 * <li>arrays and instances of <code>Iterable</code> types are replaced with dynamic lists;
 * 
 * @author Fabio Simeoni
 * 
 */
public class Dynamic {

	private static ExcludeDirective[] defaultDirectives = {annotation(Exclude.class)};
	
	/**
	 * Returns the value copy of an object in a form which is suitable for JAXB serialisation, based on default copy
	 * directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static Object externalValueOf(Object o) throws Exception {

		return externalValueOf(o,defaultDirectives);

	}

	/**
	 * Returns the value copy of an object in a form which is suitable for JAXB serialisation, based on given copy
	 * directives.
	 * 
	 * @param o the object
	 * @param the copy directives
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static Object externalValueOf(Object o, ExcludeDirective... directives) throws Exception {

		return Type.of(o).toExternal(o,directives);

	}

	/**
	 * Returns the value copy of an object based on default copy directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static Object valueOf(Object o) throws Exception {

		return valueOf(o,defaultDirectives);

	}

	/**
	 * Returns the value copy of an object based on given copy directives.
	 * 
	 * @param o the object
	 * @param directives the copy directives
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static Object valueOf(Object o, ExcludeDirective... directives) throws Exception {

		return Type.of(o).toDynamic(o,directives);

	}
}
