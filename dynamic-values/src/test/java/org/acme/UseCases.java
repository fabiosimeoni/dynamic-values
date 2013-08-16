package org.acme;

import static org.dynamicvalues.Dynamic.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.acme.Fixture.Obj;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class UseCases {

	private static final JexlEngine jexl = new JexlEngine();
	
	@Test
	public void dynamicValuesCanBeSerialisedWithJsonflex() throws Exception {

		
		Map<String,Object> value = valueOf(new Obj());
	
		String externed = new JSONSerializer().deepSerialize(value);
		
		Map<String,Object> interned = new JSONDeserializer<Map<String,Object>>().
					deserialize(externed);
		
		
		System.out.println(value);
		System.out.println(externed);
		System.out.println(interned);
		
		assertEquals(interned,value);
		
	
	}
	
	@Test
	public void dynamicValuesCanBeConsumedWithJexl() throws Exception {

		
		Map<String,Object> value = valueOf(new Obj());
		
	
		MapContext context = new MapContext(value);
		
		System.out.println(jexl.createExpression("a1.1").evaluate(context));
		System.out.println(jexl.createExpression("map.two[0][1]").evaluate(context));
	}
	
	

}
