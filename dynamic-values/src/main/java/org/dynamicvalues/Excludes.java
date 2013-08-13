package org.dynamicvalues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Excludes {

	/**
	 * Return the directives that excludes all fields that do <em>not</em> satisfy a given directive.
	 * 
	 * @param directive the directive
	 * @return the directive
	 */
	public static ExcludeDirective not(final ExcludeDirective directive) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective all(final ExcludeDirective... directives) {
		return new ExcludeDirective() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				for (ExcludeDirective directive : directives)
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
	public static ExcludeDirective annotation(final Class<? extends Annotation> annotation) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective type(final Class<?> type) {
		return new ExcludeDirective() {

			@Override
			boolean exclude(Object object, Field field) {
				return type.isAssignableFrom(field.getType());
			}
		};
	}

	public static ExcludeDirective object(final Object o) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective value(final Object value) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective objectLike(final Object parent) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective valueLike(final Object value) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective name(final String name) {
		return new ExcludeDirective() {

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
	public static ExcludeDirective name(final Pattern pattern) {
		return new ExcludeDirective() {

			@Override
			boolean exclude(Object object, Field field) throws Exception {
				return pattern.matcher(field.getName()).matches();
			}
		};
	}

}
