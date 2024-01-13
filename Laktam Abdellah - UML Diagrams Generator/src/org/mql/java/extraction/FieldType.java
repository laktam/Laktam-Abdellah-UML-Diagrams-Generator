package org.mql.java.extraction;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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
		this.type = type;// maybe a simple type or parameterized type
	}

	public String getTypeName() {
		if (type instanceof Class<?>) {
			return ((Class<?>) type).getSimpleName();
		}
		if (isParameterized()) {
			// cast the result of .getRawType() to get simplename (will return List if the
			// field is List<Test>)
			String t = ((Class<?>) ((ParameterizedType) type).getRawType()).getSimpleName();
			// add arguments
			java.lang.reflect.Type typeArguments[] = getTypeArguments();
			if (typeArguments != null) {
//				t += "<";//escaped in xml
				t += "(";
				for (int i = 0; i < typeArguments.length; i++) {
					t += ((Class<?>) typeArguments[i]).getSimpleName();
					if (i != typeArguments.length - 1) {
						t += ", ";
					}

				}
				t += ")";
//				t+=">";
			}
			return t;
		}

		return "";
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

	public Class<?> getFieldClass() {
		if (!isParameterized()) {
			return (Class<?>) type;
		}
		return null;
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
		List<Class<?>> interfaces = new Vector<Class<?>>();
		interfaces.add(((Class<?>) ((ParameterizedType) type).getRawType()));
		getAllSuperInterfaces((Class<?>) ((ParameterizedType) type).getRawType(), interfaces);
		if (interfaces.contains(Iterable.class) || interfaces.contains(Map.class)) {
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
			if (c.getComponentType().isPrimitive() || c.getComponentType().equals(String.class) ||c.getComponentType().equals(Object.class)) {
				return true;
			}
			// array of wrapper type
			if (c.getComponentType() == Double.class || c.getComponentType() == Float.class
					|| c.getComponentType() == Long.class || c.getComponentType() == Integer.class
					|| c.getComponentType() == Short.class || c.getComponentType() == Character.class
					|| c.getComponentType() == Byte.class || c.getComponentType() == Boolean.class) {
				return true;
			}
			return false;
		}
		
		//if not an array
		if (c.isPrimitive() || c.equals(String.class) || c.equals(Object.class)) {
			return true;
		}
		// wrapper types
		if (c == Double.class || c == Float.class || c == Long.class || c == Integer.class || c == Short.class
				|| c == Character.class || c == Byte.class || c == Boolean.class) {
			return true;
		}
		return false;
	}

}
