package org.acme;

import static org.acme.Fixture.*;
import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.acme.Fixture.Obj;
import org.junit.Test;


@SuppressWarnings("all")
public class DynamicTest {

	@Test
	public void atomicValuesAreDynamic() throws Exception {

		assertEquals(null, valueOf(null));

		for (Object o : atomics)
			assertSame(o, valueOf(o));
	}

	@Test
	public void atomicValuesAreExternal() throws Exception {

		assertEquals(null, valueOf(null));

		for (Object o : atomics)
			assertSame(o, externalValueOf(o));
	}

	@Test
	public void arrayValues() throws Exception {

		//external "round-trip"s
		assertEquals(list, valueOf(array));
		assertEquals(listOfLists, valueOf(arrayOfArrays));
		
		assertEquals(list, xmlRoundTripOf(array));
		assertEquals(listOfLists, xmlRoundTripOf(arrayOfArrays));
	}

	@Test
	public void collectionValues() throws Exception {

		for (Object o : collections) {
			System.out.println(valueOf(o));
			assertEquals(o, valueOf(o));
			assertEquals(o,xmlRoundTripOf(o));
		}
	}

	@Test
	public void objectValues() throws Exception {
		
		Obj o = new Obj();
		System.out.println(valueOf(o));
		xmlRoundTripOf(o);
	}
	
	@Test
	public void sharing() throws Exception {
		
		class A {
			Object o1 = new Object();
			Object o2 = o1;
		}
		
		
		A a = new A();

		Map<?,?> map = valueOf(a);
		
		assertSame(map.get("o1"),map.get("o2"));
		
		xmlRoundTripOf(a);
	}
	
	@Test
	public void cycle() throws Exception {
		
		class A {
			A a = this;
			int v=10;
		}
		
		class B {
			
		}
		
		
		A a = new A();

		Map<?,?> map = valueOf(a);
		
		System.out.println(map);
		
		assertSame(map,map.get("a"));
		
	}

	
	@Test
	public void innerClasses() throws Exception {
		
		Object o = new Object() {
			int a=10;	
			Object inner = new Object() {};
		};
		
		Map<?,?> map = valueOf(o);
		
		assertTrue(map.size()==1);
		
		
		
	}
	
	@Test
	public void emptyFields() throws Exception {
		
		Object o = new Object() {
			int a=10;
			String[] bs = new String[0];
			Object inner = new Object(){
				Map<String,String> map = new HashMap<String, String>();
			};
			
		};
		
		Map<?,?> map = valueOf(o);
		
		assertFalse(map.containsKey("bs"));
		assertFalse(map.containsKey("inner"));
		
		
	}
}
