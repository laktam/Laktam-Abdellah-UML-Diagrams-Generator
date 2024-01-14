package org.mql.java.extraction;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;

public class FieldType {
	private String modifiers;
	private String fieldName;
	private String typeSimpleName;
	private String typeFQName;
	private List<String> simpleTypeArguments;
	private List<String> fqTypeArguments;
	private boolean isParameterized = false;
	private Type type;

	FieldType(Field f) {
		// maybe a simple type or parameterized type
		this.modifiers = Modifier.toString(f.getModifiers());
		this.fieldName = f.getName();
		this.simpleTypeArguments = new Vector<String>();
		this.fqTypeArguments = new Vector<String>();
		
		this.type = f.getGenericType();
		if (type instanceof ParameterizedType) {
			this.isParameterized = true;
			ParameterizedType pType = (ParameterizedType) type;
			this.typeSimpleName = createSimpleTypeName(pType, "(", ")");
			this.typeFQName = createFQTypeName(pType, "(", ")");
//			this.typeFQName = pType.getTypeName();
			fillSimpleTypeArguments(pType);
			fillFQTypeArguments(pType);
		} else {
			this.typeSimpleName = ((Class<?>) type).getSimpleName();
			this.typeFQName = ((Class<?>) type).getName();
		}
	}

	public static String createSimpleTypeName(ParameterizedType pType, String open, String end) {
		// cast the result of .getRawType() to get simplename (will return List if the
		// field is List<Test>)
		String t = ((Class<?>) pType.getRawType()).getSimpleName();
		// add arguments
		java.lang.reflect.Type typeArguments[] = pType.getActualTypeArguments();
		if (typeArguments != null) {
//				t += "<";//escaped in xml
			t += open;
			for (int i = 0; i < typeArguments.length; i++) {
				String simpleArgumentName = ((Class<?>) typeArguments[i]).getSimpleName();
				t += simpleArgumentName;
				if (i != typeArguments.length - 1) {
					t += ", ";
				}

			}
			t += end;
		}
		return t;

	}
	private void fillSimpleTypeArguments(ParameterizedType pType) {
		Type typeArguments[] = pType.getActualTypeArguments();
		for (Type type : typeArguments) {
			String simpleArgumentName = ((Class<?>) type).getSimpleName();
			this.simpleTypeArguments.add(simpleArgumentName);
		}
	}
	// = ParameterizedType.getTypeName();
	public static String createFQTypeName(ParameterizedType pType, String open, String end) {
		// (will return List if the field is List<Test>)
		String t = ((Class<?>) pType.getRawType()).getName();
		// add arguments
		Type typeArguments[] = pType.getActualTypeArguments();
		if (typeArguments != null) {
			t += open;
			for (int i = 0; i < typeArguments.length; i++) {
				String fqTypeArgument = ((Class<?>) typeArguments[i]).getSimpleName();
				t += fqTypeArgument;
				if (i != typeArguments.length - 1) {
					t += ", ";
				}

			}
			t += end;
//				t+=">";
		}
		return t;
	}
	
	private void fillFQTypeArguments(ParameterizedType pType) {
		Type typeArguments[] = pType.getActualTypeArguments();
		for (Type type : typeArguments) {
			String simpleArgumentName = ((Class<?>) type).getName();
			this.simpleTypeArguments.add(simpleArgumentName);
		}
	}

	public Class<?> getFieldType() {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		}
		return null;
	}

	public String getModifiers() {
		return modifiers;
	}

	public Type[] getTypeArguments() {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return null;
	}

	public boolean isCollection() {
		List<Class<?>> interfaces = new Vector<Class<?>>();
		interfaces.add(((Class<?>) ((ParameterizedType) type).getRawType()));// Map declaration
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

	public String getTypeSimpleName() {
		return typeSimpleName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public boolean isParameterized() {
		return isParameterized;
	}

	public List<String> getFQTypeArguments() {
		return fqTypeArguments;
	}

	public List<String> getSimpleTypeArguments() {
		return simpleTypeArguments;
	}

	public String getTypeFQName() {
		return typeFQName;
	}

	public boolean isArray() {
		if (((Class<?>) type).isArray()) {
			return true;
		}
		return false;
	}

	public String getArrayTypeSimpleName() {
		if (isArray()) {
			return ((Class<?>) type).componentType().getSimpleName();
		}
		return null;
	}

	public String getArrayTypeFQName() {
		if (isArray()) {
			return ((Class<?>) type).componentType().getName();
		}
		return null;
	}

	public boolean isSimple() {
		Class<?> c = getFieldType();
		if (c.isArray()) {
			if (c.getComponentType().isPrimitive() || c.getComponentType().equals(String.class)
					|| c.getComponentType().equals(Object.class)) {
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

		// if not an array
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
