package org.dynamicvalues;

import static org.dynamicvalues.Directives.*;

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
 * {@link #valueOf(Object)} and {@link #externalValueOf(Object)} can be applied to any object, including lists, maps,
 * wrapper types (hence primitive values, through auto-boxing). For this reason, the method signature returns a generic
 * object, and the client is responsible for casting to the appropriate type if needed for further processing. However,
 * a cast is performed implicitly on assignment, e.g.:
 * 
 * <pre>
 * MyObject o = ...
 * Map<String,Object> value = Dynamic.valueOf(o);
 * ...process value...
 * </pre>
 * 
 * 
 * <p>
 * <b>Copy Directives</b>
 * <p>
 * 
 * This factory can follow {@link Exclusion} and {@link Mapping} directives to produce value copies and external value
 * copies. By default, instance fields marked with the {@link Exclude} annotations are excluded from copies. Other
 * directives can be conveniently produced by the {@link Directives} factory> As a simple example:
 * 
 * <pre>
 * 
 * import static ....Directives.*;
 * 
 * MyObject o = ...
 * Map<String,Object> value = Dynamic.valueOf(o,by().excluding(type(MyType.class),annotation(MyAnnotation.class)))
 * 				   .mapping(asString(QName.class));
 * 
 * </pre>
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

	private static Exclusion[] defaultExcludes = new Exclusion[] { annotation(Exclude.class) };
	private static Mapping[] defaultMappings = new Mapping[] {};
	private static Directives defaults = by().excluding(defaultExcludes).mapping(defaultMappings);

	/**
	 * Returns the value copy of an object based on default copy directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T valueOf(Object o) throws Exception {

		return valueOf(o, by());

	}

	/**
	 * Returns the value copy of an object based on given copy directives.
	 * 
	 * @param o the object
	 * @param directives the copy directives
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T valueOf(Object o, Directives directives) throws Exception {

		@SuppressWarnings("all")
		T t = (T) valueOf(o, new HashMap<Integer, Object>(), addDefaults(directives));
		return t;

	}

	/**
	 * Returns the value copy of an object in a form which is suitable for JAXB serialisation, based on default copy
	 * directives.
	 * 
	 * @param o the object
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T externalValueOf(Object o) throws Exception {

		return externalValueOf(o, by());

	}

	/**
	 * Returns the value copy of an object in a form which is suitable for JAXB serialisation, based on given copy
	 * directives.
	 * 
	 * @param o the object
	 * @param directives the copy directives
	 * @return the dynamic value
	 * @throws Exception if the value copy of the object cannot be returned
	 */
	public static <T> T externalValueOf(Object o, Directives directives) throws Exception {

		@SuppressWarnings("all")
		T t = (T) externalValueOf(o, new HashMap<Integer, Object>(), directives);
		return t;

	}

	// used internally to support recursiones
	static Object valueOf(Object o, Map<Integer, Object> state, Directives directives) throws Exception {

		return Type.of(o).toDynamic(o, state, directives);

	}

	// used internally to support recursion
	static Object externalValueOf(Object o, Map<Integer, Object> state, Directives directives) throws Exception {

		return Type.of(o).toExternal(o, state, addDefaults(directives));

	}

	// helper
	private static Directives addDefaults(Directives directives) {

		return directives.excluding(defaults.excludes()).mapping(defaults.mappings());
	}

}
