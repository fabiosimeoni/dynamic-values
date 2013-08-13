package org.acme;

import static org.dynamicvalues.Dynamic.*;

import java.util.Map;

import org.acme.TestModel.Obj;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;

public class UseCases {

	private static final JexlEngine jexl = new JexlEngine();
	
	@Test
	public void jexlClient() throws Exception {

		
		Map<String,Object> value = valueOf(new Obj());
		
	
		MapContext context = new MapContext(value);
		
		System.out.println(jexl.createExpression("a1.1").evaluate(context));
		System.out.println(jexl.createExpression("map.two[0][1]").evaluate(context));
	}

}
