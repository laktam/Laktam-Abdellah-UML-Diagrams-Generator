package org.mql.java.extraction;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ParameterType {
	private String name;// this the name of the parameter not the type
	private String typeSimpleName;
	private String typeFQName;
	private boolean isSimple;
	private List<String> simpleTypeArguments;
	private List<String> fqTypeArguments;
	private Type type;

	ParameterType(Parameter p) {
		this.name = p.getName();
		this.simpleTypeArguments = new Vector<String>();
		this.fqTypeArguments = new Vector<String>();
		this.typeSimpleName = p.getType().getSimpleName();
		this.typeFQName = p.getType().getName();
		this.isSimple = SuperType.isSimple(p.getType());
		// if it's not parameterized it return same as get Type
		this.type = p.getParameterizedType();
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			this.typeSimpleName = createSimpleTypeNameAndSimpleTypeArgument(pType, "(", ")");
			this.typeFQName = createFQTypeNameAndFQTypeArguments(pType, "(", ")");

		} else {
			this.typeSimpleName = ((Class<?>) type).getSimpleName();
			this.typeFQName = ((Class<?>) type).getName();
		}
	}

	public String getName() {
		return name;
	}

	public String getTypeFQName() {
		return typeFQName;
	}

	public String getTypeSimpleName() {
		return typeSimpleName;
	}

	public boolean isSimple() {
		if (type instanceof Class<?>) {
			Class<?> c = (Class<?>) type;
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
		}
		// if instance of ParameterizedType or not simple
		return false;
	}

	public boolean isCollection() {
		if (type instanceof ParameterizedType) {
			List<Class<?>> interfaces = new Vector<Class<?>>();
			interfaces.add(((Class<?>) ((ParameterizedType) type).getRawType()));
			getAllSuperInterfaces((Class<?>) ((ParameterizedType) type).getRawType(), interfaces);
			if (interfaces.contains(Iterable.class) || interfaces.contains(Map.class)) {
				return true;
			}
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

	private String createSimpleTypeNameAndSimpleTypeArgument(ParameterizedType ptype, String open, String end) {
		// cast the result of .getRawType() to get simplename (will return List if the
		// field is List<Test>)
		String t = ((Class<?>) ptype.getRawType()).getSimpleName();
		// add arguments
		java.lang.reflect.Type typeArguments[] = getTypeArguments();
		if (typeArguments != null) {
//				t += "<";//escaped in xml
			t += open;
			for (int i = 0; i < typeArguments.length; i++) {
				String simpleArgumentName = ((Class<?>) typeArguments[i]).getSimpleName();
				simpleTypeArguments.add(simpleArgumentName);
				t += simpleArgumentName;
				if (i != typeArguments.length - 1) {
					t += ", ";
				}

			}
			t += end;
		}
		return t;

	}

	private String createFQTypeNameAndFQTypeArguments(ParameterizedType ptype, String open, String end) {
		// (will return List if the field is List<Test>)
		String t = ((Class<?>) ptype.getRawType()).getName();
		// add arguments
		Type typeArguments[] = getTypeArguments();
		if (typeArguments != null) {
			t += open;
			for (int i = 0; i < typeArguments.length; i++) {
				String fqTypeArgument = ((Class<?>) typeArguments[i]).getSimpleName();
				fqTypeArguments.add(fqTypeArgument);
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

	public Type[] getTypeArguments() {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return null;
	}
}
