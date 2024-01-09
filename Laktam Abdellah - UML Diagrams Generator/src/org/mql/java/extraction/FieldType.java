package org.mql.java.extraction;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;

public class FieldType {
	private String modifiers;
	private String name;
	private Type type;

	FieldType(String modifiers, String name, Type type) {
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
	}
	
	public String getModifiers() {
		return modifiers;
	}

//	public void setModifiers(String modifiers) {
//		this.modifiers = modifiers;
//	}

	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}

	public Type getType() {
		return type;
	}

	public Class<?> getFieldClass(){
		return (Class<?>) type;
	}
	
	public boolean isParameterized() {
		if (type instanceof ParameterizedType) {
			return true;
		}
		return false;
	}

	public java.lang.reflect.Type[] getTypeArguments() {
		if (isParameterized())
//			List<?> types = List.of(((ParameterizedType) type).getActualTypeArguments());
			return ((ParameterizedType) type).getActualTypeArguments();
		return null;
	}

	public boolean isCollection() {
		List<Class<?>> superInterfaces = new Vector<Class<?>>();
		getAllSuperInterfaces((Class<?>) ((ParameterizedType) type).getRawType(), superInterfaces);
		if (superInterfaces.contains(Iterable.class) || superInterfaces.contains(Map.class)) {
			return true;
		}
		return false;
	}

	private void getAllSuperInterfaces(Class<?> c, List<Class<?>> interfaces) {
		Class<?> list[] = c.getInterfaces();
		interfaces.addAll(List.of(list));
		for (Class<?> i : list) {
			getAllSuperInterfaces(i, interfaces);
		}
	}

	public boolean isArray() {
		if (((Class<?>) type).isArray()) {
			return true;
		}
		return false;
	}
	
	public boolean isSimple() {
		Class<?> c = getFieldClass();
		if (c.isArray()) {
			if(c.getComponentType().isPrimitive() || c.getComponentType().equals(String.class)) {
				return true;
			}
			return false;
		}
		if (c.isPrimitive() || c.equals(String.class)) {
			return true;
		}
		return false;
	}

}
