package org.acme;

import static org.dynamicvalues.Dynamic.*;
import static org.dynamicvalues.DynamicIO.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dynamicvalues.Exclude;

public class TestModel {

	static int[] array = new int[] { 1, 2, 3 };
	static int[][] arrayOfArrays = new int[][] { array, array };
	static List<Object> list = Arrays.<Object> asList(1, 2, 3);
	static List<Object> listOfLists = Arrays.<Object> asList(list, list);

	static List<Object> atomics = Arrays.<Object> asList(1, new Integer(1), "one", true, 3L, 3.5d, 3.5f);

	static List<Object> arrays = Arrays.<Object> asList(array, arrayOfArrays);

	static List<Object> collections = Arrays.<Object> asList(list, listOfLists, map(), mapOfCollections(), mapOfNumericKeys());

	
	static class Member {
		
		boolean b = false;
		
	}
	
	public static class Super {
		
		int i = 10;
		
		String j = "test";
		
		
	}
	public static class Obj extends Super {
	
		Member m = new Member();
		
		@Exclude
		String unmapped = "unmapped";
		
		int[] a1 = array;
		
		int[][] a2 = arrayOfArrays;
		
		List<Object> l = list;
		
		Map<String,Object> map = mapOfCollections();
		
	}
	
	
	
	static Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("one", 1);
		map.put("two", 2);
		return map;
	}
	
	static Map<Integer, Object> mapOfNumericKeys() {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, 1);
		map.put(2, 2);
		return map;
	}

	static Map<String, Object> mapOfCollections() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("one", list);
		map.put("two", listOfLists);
		map.put("three", map());
		return map;
	}
	
	static Object xmlRoundTripOf(Object o) throws Exception {

		JAXBContext ctx = newInstance();
		
		StringWriter writer = new StringWriter();
		Marshaller m =ctx.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		
		m.marshal(externalValueOf(o), writer);

		System.out.println(writer.toString());

		StringReader reader = new StringReader(writer.toString());
		Object read = ctx.createUnmarshaller().unmarshal(reader);

		return valueOf(read);
	}
}
