package org.mql.java.extraction;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

public class MethodType {
	private String name;
	private String modifiers;
	private List<ParameterType> parameters;
	private Class<?> returnType;

	MethodType(String name, String modifiers, Parameter parameters[], Class<?> returnType) {
		this.name = name;
		this.modifiers = modifiers;
		this.parameters = new Vector<ParameterType>();
		for (Parameter p : parameters) {
			this.parameters.add(new ParameterType(p.getName(), p.getType()));
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

	public Class<?> getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean complexReturnTypeAndNotVoid() {
		if (returnType.isArray()) {
			if (returnType.getComponentType().isPrimitive() || returnType.getComponentType().equals(String.class)
					|| returnType.getComponentType().equals(void.class) || returnType.getComponentType().equals(Object.class)) {
				return false;
			}
			// array of wrapper type
			if (returnType.getComponentType() == Double.class || returnType.getComponentType() == Float.class || returnType.getComponentType() == Long.class
					|| returnType.getComponentType() == Integer.class || returnType.getComponentType() == Short.class || returnType.getComponentType() == Character.class
					|| returnType.getComponentType() == Byte.class || returnType.getComponentType() == Boolean.class) {
				return false;
			}
				return true;
		}
		if (returnType.isPrimitive() || returnType.equals(String.class) || returnType.equals(void.class) || returnType.equals(Object.class)) {
			return false;
		}
		if (returnType == Double.class || returnType == Float.class || returnType == Long.class
				|| returnType == Integer.class || returnType == Short.class || returnType == Character.class
				|| returnType == Byte.class || returnType == Boolean.class) {
			return false;
		}
		return true;
	}

}
