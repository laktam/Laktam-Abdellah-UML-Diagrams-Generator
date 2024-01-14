package org.mql.java.extraction;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

public class MethodType {
	private String name;
	private String modifiers;
	private List<ParameterType> parameters;
	private Type returnType;

	MethodType(String name, String modifiers, Parameter parameters[], Type returnType) {
		this.name = name;
		this.modifiers = modifiers;
		this.parameters = new Vector<ParameterType>();
		for (Parameter p : parameters) {
			this.parameters.add(new ParameterType(p));
		}
		this.returnType = returnType;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}

	public List<ParameterType> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterType> parameters) {
		this.parameters = parameters;
	}

	public Type getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	
	public boolean complexReturnTypeAndNotVoid() {
		if(returnType instanceof Class<?>) {
			Class<?> c = (Class<?>) returnType;
			if (c.isArray()) {
				if (c.getComponentType().isPrimitive() || c.getComponentType().equals(String.class)
						|| c.getComponentType().equals(void.class) || c.getComponentType().equals(Object.class)) {
					return false;
				}
				// array of wrapper type
				if (c.getComponentType() == Double.class || c.getComponentType() == Float.class || c.getComponentType() == Long.class
						|| c.getComponentType() == Integer.class || c.getComponentType() == Short.class || c.getComponentType() == Character.class
						|| c.getComponentType() == Byte.class || c.getComponentType() == Boolean.class) {
					return false;
				}
					return true;
			}
			if (c.isPrimitive() || c.equals(String.class) || c.equals(void.class) || c.equals(Object.class)) {
				return false;
			}
			if (returnType == Double.class || returnType == Float.class || returnType == Long.class
					|| returnType == Integer.class || returnType == Short.class || returnType == Character.class
					|| returnType == Byte.class || returnType == Boolean.class) {
				return false;
			}
		}
		return true;
	}

}
