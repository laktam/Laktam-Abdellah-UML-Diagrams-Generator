package org.mql.java.extraction;

import java.lang.reflect.Parameter;

public class ParameterType {
	private String name;//this the name of the parameter not the type 
	private String simpleTypeName;
	private String fqTypeName;
	
	//must remove and only leave necessary information (so i can easily parse back from xml to memory structure
	private Class<?> type;
	
	ParameterType(String name, Class<?> type){
		this.name = name;
		this.type = type;
	}
	ParameterType(Parameter p){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public void setType(Class<?> type) {
		this.type = type;
	}
	
//	public boolean isSimple() {
//		if (type.isArray()) {
//			if(type.getComponentType().isPrimitive() || type.getComponentType().equals(String.class)) {
//				return true;
//			}
//			return false;
//		}
//		if (type.isPrimitive() || type.equals(String.class)) {
//			return true;
//		}
//		return false;
//	}
	
	public boolean isSimple() {
		if (type.isArray()) {
			if (type.getComponentType().isPrimitive() || type.getComponentType().equals(String.class) ||type.getComponentType().equals(Object.class)) {
				return true;
			}
			// array of wrapper type
			if (type.getComponentType() == Double.class || type.getComponentType() == Float.class
					|| type.getComponentType() == Long.class || type.getComponentType() == Integer.class
					|| type.getComponentType() == Short.class || type.getComponentType() == Character.class
					|| type.getComponentType() == Byte.class || type.getComponentType() == Boolean.class) {
				return true;
			}
			return false;
		}
		
		//if not an array
		if (type.isPrimitive() || type.equals(String.class) || type.equals(Object.class)) {
			return true;
		}
		// wrapper types
		if (type == Double.class || type == Float.class || type == Long.class || type == Integer.class || type == Short.class
				|| type == Character.class || type == Byte.class || type == Boolean.class) {
			return true;
		}
		return false;
	}
}
