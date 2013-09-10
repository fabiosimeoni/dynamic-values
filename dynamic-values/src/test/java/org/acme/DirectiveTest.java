package org.acme;

import static org.dynamicvalues.Directives.*;
import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.xml.namespace.QName;

import org.dynamicvalues.Directives;
import org.dynamicvalues.Exclude;
import org.dynamicvalues.Externals.ValueMap;
import org.junit.Test;

@SuppressWarnings("all")
public class DirectiveTest {


	@Test
	public void defaultExclude() throws Exception {

		class Obj {
			
			@Exclude
			int a1 = 10;
			
		}
		
		Map<?,?> value = valueOf(new Obj());
		assertFalse(value.containsKey("a1"));
		
		ValueMap external = externalValueOf(new Obj());
		assertFalse(external.elements.containsKey("a1"));

	}
	
	@Test
	public void excludeType() throws Exception {

		class Obj {
			
			String a1 = "10";
			
		}
	
		Directives directives = by().excluding(type(String.class));
		
		Map<?,?> value = valueOf(new Obj(),directives);
		assertFalse(value.containsKey("a1"));
		
		ValueMap external = externalValueOf(new Obj(),directives);
		assertFalse(external.elements.containsKey("a1"));
	}
	
	@Test
	public void include() throws Exception {

		class Obj {
			
			int a1 = 10;
			
			String a2 ="10";
			
		}
		
		Directives directives = by().excluding(not(type(String.class)));
		
		Map<?,?> value = valueOf(new Obj(),directives);
		assertFalse(value.containsKey("a1"));
		assertTrue(value.containsKey("a2"));
		
		ValueMap external = externalValueOf(new Obj(),directives);
		assertFalse(external.elements.containsKey("a1"));
		assertTrue(external.elements.containsKey("a2"));
	}
	
	@Test
	public void andExclude() throws Exception {

		class Obj {
			
			@Deprecated
			int a1 = 10;
			
			@Deprecated
			String a2 ="10";
			
		}
		
		Directives directives = by().excluding(all(annotation(Deprecated.class),type(String.class)));

		Map<?,?> value = valueOf(new Obj(),directives);
		assertFalse(value.containsKey("a2"));
		assertTrue(value.containsKey("a1"));

		ValueMap external = externalValueOf(new Obj(),directives);
		assertFalse(external.elements.containsKey("a2"));
		assertTrue(external.elements.containsKey("a1"));

	}
	
	@Test
	public void objectExclude() throws Exception {

		final String mystring = "test";
		
		Object obj = new Object() {
			
			String a1 =mystring;
			
		};
		
		Directives directives = by().excluding(object(obj));

		Map<?,?> value = valueOf(obj,directives);
		assertFalse(value.containsKey("a1"));
		
		ValueMap external = externalValueOf(obj,directives);
		assertFalse(external.elements.containsKey("a1"));
	}
	
	@Test
	public void mapToString() throws Exception {

		final String mystring = "test";
		
		Object obj = new Object() {
			
			QName a1 = new QName("test");
			
		};
		

		Map<?,?> value = valueOf(obj,by().mapping(objectsToStringFor(QName.class)));
		assertEquals(value.get("a1"),"test");
		
		ValueMap external = externalValueOf(obj,by().mapping(objectsToStringFor(QName.class)));
		assertEquals(external.elements.get("a1"),"test");
	}
	

}
