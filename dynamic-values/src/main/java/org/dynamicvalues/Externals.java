package org.dynamicvalues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * List and map wrapper types for JAXB bindings of external value copies.
 * 
 * @author Fabio Simeoni
 *
 */
public class Externals {

	@XmlRootElement(name="map")
	public static class ValueMap {
		
		ValueMap() {
			elements = new LinkedHashMap<Object, Object>();
		}
		
		@XmlJavaTypeAdapter(MapAdapter.class)
		@XmlElement(name="entries")
		public Map<Object,Object> elements;
		
		public ValueMap(Map<Object,Object> elements) {
			this.elements=elements;

		}
		
		@Override
		public String toString() {
			return elements.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((elements == null) ? 0 : elements.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ValueMap other = (ValueMap) obj;
			if (elements == null) {
				if (other.elements != null)
					return false;
			} else if (!elements.equals(other.elements))
				return false;
			return true;
		}
		
		
		
	}
	
	@XmlRootElement(name="list")
	public static class ValueList implements Iterable<Object> {
		
		ValueList(){
			elements = new ArrayList<Object>();
		};
		
		@XmlElementWrapper(name="list")
		@XmlElement(name="element")
		public List<Object> elements;
		
		@SuppressWarnings("all")
		public ValueList(List<Object> elements) {
			this.elements=elements;
		}
				
		@Override
		public String toString() {
			return elements.toString();
		}
		
		@Override
		public Iterator<Object> iterator() {
			return elements.iterator();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((elements == null) ? 0 : elements.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ValueList other = (ValueList) obj;
			if (elements == null) {
				if (other.elements != null)
					return false;
			} else if (!elements.equals(other.elements))
				return false;
			return true;
		}
		
		
	}
	
	
	//helpers
	
	
	@XmlType
	static class Entries {
		
		@XmlElement(name="entry")
		List<Entry> value;
	}
	
	static class Entry {
		
		@XmlElement
		Object key;
		
		@XmlElement
		Object value;

		public Entry() {}
		
		public Entry(Object key, Object value) {
			this.key=key;
			this.value=value;
		}
		
	}
	
	public static class MapAdapter extends XmlAdapter<Entries,Map<Object,Object>>{
		
		@Override
		public Map<Object, Object> unmarshal(Entries entries) throws Exception {
			
			Map<Object,Object> in = new HashMap<Object,Object>();
			
			for (Entry e : entries.value)
				in.put(e.key,e.value);
			
			return in;
		}
		
		@Override
		public Entries marshal(Map<Object, Object> map) throws Exception {
			
			ArrayList<Entry> entries = new ArrayList<Entry>();
			
			for (Map.Entry<Object,Object> e : map.entrySet())
				entries.add(new Entry(e.getKey(),e.getValue()));
			
			Entries out = new Entries();
			out.value=entries;
			
			return out;
		}
	}
}
