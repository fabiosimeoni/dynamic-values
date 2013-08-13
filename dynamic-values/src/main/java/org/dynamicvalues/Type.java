package org.dynamicvalues;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dynamicvalues.Externals.ValueList;
import org.dynamicvalues.Externals.ValueMap;

/**
 * Enumeration-based engine for recursive type analysis.
 * @author Fabio Simeoni
 *
 */
enum Type {

	valuemap {

		@Override
		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {
			Map<Object, Object> objects = new LinkedHashMap<Object, Object>();
			for (Map.Entry<Object, Object> el : ValueMap.class.cast(o).elements.entrySet())
				objects.put(el.getKey(), Dynamic.valueOf(el.getValue(),directives));
			return objects;
		}
	},

	valuelist {

		@Override
		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {
			List<Object> objects = new ArrayList<Object>();
			for (Object el : ValueList.class.cast(o).elements)
				objects.add(Dynamic.valueOf(el,directives));
			return objects;
		}
	},

	voidtype,

	atomic,

	collection {
		ValueList toExternal(Object o,ExcludeDirective ... directives) throws Exception {
			List<Object> elements = new ArrayList<Object>();
			for (Object element : Iterable.class.cast(o))
				elements.add(Dynamic.externalValueOf(element,directives));
			return new ValueList(elements);
		}

		@Override
		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {
			List<Object> elements = new ArrayList<Object>();
			for (Object element : Iterable.class.cast(o))
				elements.add(Dynamic.valueOf(element,directives));
			return elements;
		}
	},

	array {
		Iterable<?> toExternal(Object o,ExcludeDirective ... directives) throws Exception {
			List<Object> elements = new ArrayList<Object>();
			for (int i = 0; i < Array.getLength(o); i++)
				elements.add(Dynamic.externalValueOf(Array.get(o, i),directives));
			return new ValueList(elements);
		}

		@Override
		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {
			List<Object> elements = new ArrayList<Object>();
			for (int i = 0; i < Array.getLength(o); i++)
				elements.add(Dynamic.valueOf(Array.get(o, i),directives));
			return elements;
		}
	},

	map {
		@Override
		Object toExternal(Object o,ExcludeDirective ... directives) throws Exception {
			Map<Object, Object> elements = new LinkedHashMap<Object, Object>();
			for (Map.Entry<?, ?> e : ((Map<?,?>) o).entrySet())
				elements.put(Dynamic.externalValueOf(e.getKey(),directives), Dynamic.externalValueOf(e.getValue(),directives));
			return new ValueMap(elements);
		}

		@Override
		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {
			Map<Object, Object> map = new LinkedHashMap<Object, Object>();
			for (Map.Entry<?, ?> e : ((Map<?,?>) o).entrySet())
				map.put(Dynamic.valueOf(e.getKey(),directives), Dynamic.valueOf(e.getValue(),directives));
			return map;
		}
	},

	object {
		Object toExternal(Object o,ExcludeDirective ... directives) throws Exception {
			Map<Object, Object> made = new HashMap<Object, Object>();
			Class<?> clazz = o.getClass();
			List<Field> fields = valueFieldsOf(o,clazz,directives);
			for (Field field : fields) {
				field.setAccessible(true);
				Object val = Dynamic.externalValueOf(field.get(o),directives);
				if (val != null)
					made.put(field.getName(), val);
			}
			return new ValueMap(made);
		}

		Object toDynamic(Object o,ExcludeDirective ... directives) throws Exception {

			Map<Object, Object> made = new HashMap<Object, Object>();

			Class<?> clazz = o.getClass();

			List<Field> fields = valueFieldsOf(o,clazz,directives);
			for (Field field : fields) {
				field.setAccessible(true);
				Object val = Dynamic.valueOf(field.get(o),directives);
				if (val != null)
					made.put(field.getName(), val);
			}
			return made;
		}
	};

	Object toExternal(Object o, ExcludeDirective ... directives) throws Exception {
		return o; // default
	}

	//from static to dynamic
	Object toDynamic(Object o, ExcludeDirective ... directives) throws Exception {
		return o; // default
	}

	@SuppressWarnings("all")
	private static final List<Class<?>> atomics = Arrays.asList(String.class, Boolean.class, Character.class,
			Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class);

	
	public static Type of(Object o) {

		if (o == null)
			return voidtype;

		if (o instanceof ValueMap)
			return valuemap;

		if (o instanceof ValueList)
			return valuelist;

		if (atomics.contains(o.getClass()))
			return atomic;

		if (o.getClass().isArray())
			return array;

		if (o instanceof Iterable<?>)
			return collection;

		if (o instanceof Map<?, ?>)
			return map;

		return object;
	}
	
	private static List<Field> valueFieldsOf(Object o,Class<?> clazz,ExcludeDirective ... directives) throws Exception {

		List<Field> fields = new ArrayList<Field>();

		Class<?> superclass = clazz.getSuperclass();

		if (superclass != null)
			fields.addAll(valueFieldsOf(o,superclass,directives));
		
		field: for (Field field : clazz.getDeclaredFields())
			for (ExcludeDirective directive : directives)
				if (!directive.exclude(o,field)) {
					fields.add(field);
					continue field;
				}

		return fields;
	}

}
