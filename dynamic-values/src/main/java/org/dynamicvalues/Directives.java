package org.dynamicvalues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * Factory of directives for the value copies.
 * 
 * @author Fabio Simeoni
 *
 */
public class Directives {

	/**
	 * Creates a set of directives.
	 * @return the directives
	 */
	public static Directives by() {
		return new Directives();
	}
	
	private final List<Exclusion> excludes = new ArrayList<Exclusion>();
	private final List<Mapping> mappings = new ArrayList<Mapping>();
	
	private Directives() {}
	
	/**
	 * Adds a set of {@link Exclusion}s to these directives.
	 * @param directives the exclude directives
	 * @return these directives
	 */
	public Directives excluding(Exclusion ... directives) {
		return excluding(Arrays.asList(directives));
	}
	
	/**
	 * Adds a set of {@link Exclusion}s to these directives.
	 * @param excludes the exclude directives
	 * @return these directives
	 */
	public Directives excluding(List<Exclusion> directives) {
		this.excludes.addAll(directives);
		return this;
	}
	
	/**
	 * Adds a set of {@link Mapping}s to these directives.
	 * @param directives the map directives
	 * @return these directives
	 */
	public Directives mapping(List<Mapping> directives) {
		this.mappings.addAll(directives);
		return this;
	}
	
	/**
	 * Adds a set of {@link Mapping}s to these directives.
	 * @param directives the map directives
	 * @return these directives
	 */
	public Directives mapping(Mapping ... directives) {
		return mapping(Arrays.asList(directives));
	}
	
	//use internally for inspection
	List<Exclusion> excludes() {
		return excludes;
	}
	
	List<Mapping> mappings() {
		return mappings;
	}
	
	
	//excludes
	
	
	/**
	 * Return the directives that excludes all fields that do <em>not</em> satisfy a given directive.
	 * 
	 * @param directive the directive
	 * @return the directive
	 */
	public static Exclusion not(final Exclusion directive) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				return !directive.exclude(object, field);
			}
		};
	}

	/**
	 * Return the directives that excludes all fields that satisfy simultaneously two or more directives.
	 * 
	 * @param directives the directives
	 * @return the directive
	 */
	public static Exclusion all(final Exclusion... directives) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				for (Exclusion directive : directives)
					if (!directive.exclude(object, field))
						return false;

				return true;
			}
		};
	}

	/**
	 * Return the directives that excludes all fields that have a given annotation.
	 * 
	 * @param annotation the given expression
	 * @return the directive
	 */
	public static Exclusion annotation(final Class<? extends Annotation> annotation) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) {
				return field.isAnnotationPresent(annotation);
			}
		};
	}

	/**
	 * Return the directives that excludes all fields that have a given type (in the sense of {@link Class#isAssignableFrom(Class)}).
	 * 
	 * @param type the given type
	 * @return the directive
	 */
	public static Exclusion type(final Class<?> type) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) {
				return type.isAssignableFrom(field.getType());
			}
		};
	}

	/**
	 * Return the directives that excludes all fields within a given object.
	 * 
	 * @param type the given type
	 * @return the directive
	 */
	public static Exclusion object(final Object o) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) {
				return object == o;
			}
		};
	}

	/**
	 * Return the directives that excludes all fields with a given value.
	 * 
	 * @param value the given value
	 * @return the directive
	 */
	public static Exclusion value(final Object value) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				field.setAccessible(true);
				Object val = field.get(object);
				return val != null && val == value;
			}
		};
	}

	/**
	 * Return the directives that excludes all fields of an object equivalent to a given object.
	 * 
	 * @param parent the given object
	 * @return the directive
	 */
	public static Exclusion objectLike(final Object parent) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) {
				return object.equals(parent);
			}
		};
	}

	/**
	 * Return the directives that excludes all fields with a value which is equivalent to a given value (in the sense
	 * of {@link Object#equals(Object)}).
	 * 
	 * @param value the given value
	 * @return the directive
	 */
	public static Exclusion valueLike(final Object value) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				field.setAccessible(true);
				Object val = field.get(object);
				return val != null && val.equals(value);
			}
		};
	}

	/**
	 * Return the directives that excludes all fields that have a given name.
	 * 
	 * @param name the name
	 * @return the directive
	 */
	public static Exclusion name(final String name) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				return field.getName().equals(name);
			}
		};
	}

	/**
	 * Return the directives that excludes all fields that match a given regular expression.
	 * 
	 * @param pattern the regular expression
	 * @return the directive
	 */
	public static Exclusion name(final Pattern pattern) {
		return new Exclusion() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				return pattern.matcher(field.getName()).matches();
			}
		};
	}

	//mappings
	
	/**
	 * Return the directives that map alls fields of a given type onto strings.
	 * @param type the given type
	 * @return the directive
	 */
	public static Mapping asString(final Class<?> type) {
		return new Mapping() {
			
			@Override
			Object map(Object parent, Field field, Object value) throws Exception {
				return value.getClass().isAssignableFrom(type)?value.toString():null;
			}
		};
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
