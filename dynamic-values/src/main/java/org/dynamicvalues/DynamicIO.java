package org.dynamicvalues;

import javax.xml.bind.JAXBContext;

import org.dynamicvalues.Externals.ValueList;
import org.dynamicvalues.Externals.ValueMap;

public class DynamicIO {

	static JAXBContext context;
	
	static {
		 try {
			 context = JAXBContext.newInstance(ValueMap.class, ValueList.class);
		 }
		 catch(Throwable t) {
			 throw new RuntimeException("cannot initialise JAXB context",t);
		 }
	}
	
	public static JAXBContext newInstance() {
		return context;
	}
}
