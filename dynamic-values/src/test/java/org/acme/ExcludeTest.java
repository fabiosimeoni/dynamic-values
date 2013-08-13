package org.acme;

import static org.dynamicvalues.Dynamic.*;
import static org.dynamicvalues.Excludes.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.dynamicvalues.Exclude;
import org.junit.Test;

@SuppressWarnings("all")
public class ExcludeTest {


	@Test
	public void defaultExclude() throws Exception {

		class Obj {
			
			@Exclude
			int a1 = 10;
			
		}
		
		Map<?,?> map = (Map<?,?>) valueOf(new Obj());
		assertFalse(map.containsKey("a1"));
	}
	
	@Test
	public void excludeType() throws Exception {

		class Obj {
			
			String a1 = "10";
			
		}
	
		Map<?,?> map = (Map<?,?>) valueOf(new Obj(),type(String.class));
		assertFalse(map.containsKey("a1"));
	}
	
	@Test
	public void include() throws Exception {

		class Obj {
			
			int a1 = 10;
			
			String a2 ="10";
			
		}
		
		Map<?,?> map = (Map<?,?>) valueOf(new Obj(),not(type(String.class)));
		assertFalse(map.containsKey("a1"));
		assertTrue(map.containsKey("a2"));
	}
	
	@Test
	public void andTest() throws Exception {

		class Obj {
			
			@Deprecated
			int a1 = 10;
			
			@Deprecated
			String a2 ="10";
			
		}

		Map<?,?> map = (Map<?,?>) valueOf(new Obj(),all(annotation(Deprecated.class),type(String.class)));
		assertFalse(map.containsKey("a2"));
		assertTrue(map.containsKey("a1"));
	}
	
	@Test
	public void objectTest() throws Exception {

		final String mystring = "test";
		
		Object obj = new Object() {
			
			String a1 =mystring;
			
		};
		

		Map<?,?> map = (Map<?,?>) valueOf(obj,object(obj));
		assertFalse(map.containsKey("a1"));
	}
	

	

}
