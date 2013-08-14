package org.dynamicvalues;

import static org.dynamicvalues.Excludes.*;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

/**
 * Dynamic value factory.
 * 
 * <p>
 * <b>Dynamic Values</b>
 * <p>
 * An object is a <em>dynamic value</em> if its graph contains only:
 * <p>
 * <ul>
 * <li>primitives, primitive wrappers, or strings;
 * <li>lists of dynamic values (<em>dynamic lists</em>);
 * <li>maps whose keys and values are dynamic values (<em>dynamic maps</em>);
 * </ul>
 * 
 * Dynamic values are suitable for generic manipulation with expression language or for generic object serialisations.
 * 
 * 
 * <p>
 * <b>Value Copies</b>
 * <p>
 * 
 * This factory takes an arbitrary object graph and returns its <em>value copy</em> ({@link #valueOf(Object)}), i.e. a
 * "copy" in which:
 * <p>
 * <ul>
 * <li>maps and instances of user-defined types are replaced with dynamic maps;
 * <li>arrays and instances of <code>Iterable</code> types are replaced with dynamic lists;
 * </ul>
 * 
 * <p>
 * <b>External Value Copies</b>
 * <p>
 * 
 * This factory can also produce <em>external value copies</em> ({@link #externalValueOf(Object)}, where dynamic lists
 * and dynamic values are appropriately wrapped exclusively for JAXB serialisation. For this purpose, {@link DynamicIO}
 * offers a {@link JAXBContext} pre-configured with wrapper classes:
 * 
 * <pre>
 * MyObject o = ...
 * JAXBContext context = DynamicIO.newInstance();
 * context.createMarshaller().marshal(Dynamic.externalValueOf(o),...sink...);
 * </pre>
 * 
 * Deserialisation proceeds similarly, and {@link #valueOf(Object)} can be invoked on the deserialised object to unwrap
 * the external value copy into a normal value copy for further processing:
 * 
 * <pre>
 * JAXBContext context = DynamicIO.newInstance();
 * Object externalValue = context.createUnmarshaller().unmarshal(...source...);
 * Object value = valueOf(externalvalue);
 *  ...process value...
 * </pre>
 * 
 * <p>
 * <b>Assignments and Casts</b>
 * <p>
 * 
 * {@link #valueOf(Object)} can be applied to any object, including lists, maps, wrapper types (hence primitive values,
 * through auto-boxing). The returned copy will have the same type as the input, except for instances of user-defined
 * types, which will be dynamic maps.
 * 
 * <p>
 * 
 * The method signature returns a generic object, and the client is responsible for casting to the appropriate type if
 * needed for further processing. However, a cast is performed implicitly on assignment, e.g.:
 * 
 * <pre>
 * MyObject o = ...
 * Map<String,Object> value = Dynamic.valueOf(o);
 * ...process value...
 * </pre>
 * 
 * This facility is not available for external value copies, which are intended for immediate JAXB serialisation.
 * 
 * <p>
 * <b>Exclude Directives</b>
 * <p>
 * 
 * This factory can follow {@link ExcludeDirective}s to produce value copies and external value copies of user-defined
 * instances. By default, instance fields marked with the {@link Exclude} annotations are excluded from copies. Other
 * exclusions can be conveniently produced by the {@link Excludes} factory (e.g. based on other annotation types, field
 * types, field names or values, etc).
 * 
 * <p>
 * <b>Cycles and Sharing</b>
 * <p>
 * 
 * Both value copies and external value copies preserve sharing and cycles. Note however that JAXB serialisation of
 * external value copies does not support sharing and will fail in the presence of cycles. Similar restrictions may
 * apply to other serialisation mechanisms, and more generally to other forms of later processing.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class Dynamic {

	private static ExcludeDirective[] defaultDirectives = { annotation(Exclude.class) };

	/**
	 * Returns the value copy of an object in a form which is suitable for JAXB serialisation, based on default copy
	 * directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static Object externalValueOf(Object o) throws Exception {

		return externalValueOf(o, defaultDirectives);

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

		return externalValueOf(o, new HashMap<Integer, Object>(), directives);

	}

	// used internally to support recursion
	static Object externalValueOf(Object o, Map<Integer, Object> state, ExcludeDirective... directives)
			throws Exception {

		return Type.of(o).toExternal(o, state, directives);

	}

	/**
	 * Returns the value copy of an object based on default copy directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T valueOf(Object o) throws Exception {

		return valueOf(o, defaultDirectives);

	}

	/**
	 * Returns the value copy of an object based on given copy directives.
	 * 
	 * @param o the object
	 * @param directives the copy directives
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T valueOf(Object o, ExcludeDirective... directives) throws Exception {

		@SuppressWarnings("all")
		T t = (T) valueOf(o, new HashMap<Integer, Object>(), directives);
		return t;

	}

	// used internally to support recursiones
	static Object valueOf(Object o, Map<Integer, Object> state, ExcludeDirective... directives) throws Exception {

		return Type.of(o).toDynamic(o, state, directives);

	}

}
