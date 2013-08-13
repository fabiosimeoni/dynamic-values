package org.acme;

import static org.acme.TestModel.*;
import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;

import org.acme.TestModel.Obj;
import org.junit.Test;

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

	

}
