package org.mql.java.extraction;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;

public class SuperType {
	protected String simpleName;
	protected String fqName;
	protected String modifiers;
	protected List<FieldType> fields;
	protected List<ConstructorType> constructors;
	protected List<MethodType> methods;
	protected List<Relationship> relationships;
	protected String type;
	
	public SuperType() {
		this.relationships = new Vector<Relationship>();
		this.fields = new Vector<FieldType>();
		this.constructors = new Vector<ConstructorType>();
		this.methods = new Vector<MethodType>();
	}

	@Override
	public String toString() {
		return getSimpleName();
	}

	public void addRelationship(Relationship r) {
		relationships.add(r);
	}

	// default getters need to return an empty list !!
	public List<Relationship> getRelationships() {
		return relationships;
	}

	// this use redefined equals() and hashcode()
	public Set<Relationship> getRelationshipsSet() {
		List<Relationship> relationships = getRelationships();
		Set<Relationship> relationshipsSet = new HashSet<Relationship>();
		for (Relationship r : relationships) {
			relationshipsSet.add(r);
		}
		return relationshipsSet;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getFQName() {
		return fqName;
	}

	public String getModifiers() {
		return modifiers;
	}

	public List<FieldType> getFields() {
		return fields;
	}

	public List<ConstructorType> getConstructors() {
		return constructors;
	}

	public List<MethodType> getMethods() {
		return methods;
	}

	public static boolean isSimple(Class<?> type) {
		if (type.isArray()) {
			if (type.getComponentType().isPrimitive() || type.getComponentType().equals(String.class)
					|| type.getComponentType().equals(Object.class)) {
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

		// if not an array
		if (type.isPrimitive() || type.equals(String.class) || type.equals(Object.class)) {
			return true;
		}
		// wrapper types
		if (type == Double.class || type == Float.class || type == Long.class || type == Integer.class
				|| type == Short.class || type == Character.class || type == Byte.class || type == Boolean.class) {
			return true;
		}
		return false;
	}

//	private static void getAllSuperInterfaces(Class<?> c, List<Class<?>> interfaces) {
//		Class<?> list[] = c.getInterfaces();
//		interfaces.addAll(Arrays.asList(list));
//
//		for (Class<?> i : list) {
//			getAllSuperInterfaces(i, interfaces);
//		}
//	}

//	public static boolean isCollection(Type type) {
//		List<Class<?>> interfaces = new Vector<Class<?>>();
//		interfaces.add(((Class<?>) ((ParameterizedType) type).getRawType()));
//		getAllSuperInterfaces((Class<?>) ((ParameterizedType) type).getRawType(), interfaces);
//		if (interfaces.contains(Iterable.class) || interfaces.contains(Map.class)) {
//			return true;
//		}
//		return false;
//	}
	public void setFields(List<FieldType> fields) {
		this.fields = fields;
	}
	
	public void setMethods(List<MethodType> methods) {
		this.methods = methods;
	}
	
	public void addField(FieldType f) {
		fields.add(f);
	}
	
	

}
