package org.dynamicvalues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Excludes {


	public static ExcludeDirective not(final ExcludeDirective directive) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) throws Exception {
				return !directive.exclude(object,field);
			}
		};
	}
	
	public static ExcludeDirective all(final ExcludeDirective ... directives) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) throws Exception {
				for (ExcludeDirective directive : directives)
					if (!directive.exclude(object,field))
						return false;
				
				return true;
			}
		};
	}
	public static ExcludeDirective annotation(final Class<? extends Annotation> annotation) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) {
				return field.isAnnotationPresent(annotation);
			}
		};
	}

	
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
				return object==o;
			}
		};
	}
	
	public static ExcludeDirective value(final Object o) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) throws Exception {
				field.setAccessible(true);
				Object val = field.get(object); 
				return val!=null && val==o;
			}
		};
	}
	
	public static ExcludeDirective objectLike(final Object o) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) {
				return object.equals(o);
			}
		};
	}
	
	public static ExcludeDirective valueLike(final Object o) {
		return new ExcludeDirective() {
			
			@Override
			boolean exclude(Object object, Field field) throws Exception {
				field.setAccessible(true);
				Object val = field.get(object); 
				return val!=null && val.equals(o);
			}
		};
	}
	
	
}
