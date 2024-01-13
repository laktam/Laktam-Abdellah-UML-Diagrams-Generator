package org.mql.java.extraction2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class RelationshipDetector {
	public RelationshipDetector() {
		// TODO Auto-generated constructor stub
	}

	// check fields for Objects : agregation or composition
	// it is just an agregation if we can create an object without creating the
	// field object
	// check all methods for return type and parameteres "utilisation"
	// setter methods ? maybe check for there parameters or maybe constructor
	// parameters for
	public static void detectRepationShips(List<Type> types) {
		for (Type type : types) {
			java.lang.Class<?> c = type.getObjectClass();
			Field fields[] = c.getDeclaredFields();
			//agregation : if a field's type is complex 
			for (Field f : fields) {

				// need to check if accessible before making it true and return it to its
				// default value
				f.setAccessible(true);
				try {
					java.lang.Class<?> fieldClass = f.getType();
					if (!isSimple(fieldClass)) {//!fieldClass.isPrimitive() && !fieldClass.equals(String.class)
						type.addRelationship(new Relationship("agregation", type, new Class(fieldClass)));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			//Inheritance and implementation
			java.lang.Class<?> interfaces[] = c.getInterfaces();
			String relationship = "";
			if("class".equals(type.getType())) {
				if(!Object.class.equals(c.getSuperclass())) {
					type.addRelationship(new Relationship("inheritence", type, new Class(c.getSuperclass())));
				}
				relationship = "implementation";
			}else if ("interface".equals(type.getType())) {
				relationship = "inheritence";
			}
			for (java.lang.Class<?>  i : interfaces) {
				type.addRelationship(new Relationship(relationship, type, new Class(i)));
			}
			//check constructor parameters ??
			//dependency : detect it if a method has a return type or a parameter of an external class
			Method methods[]  = c.getDeclaredMethods();
			for (Method m : methods) {
				java.lang.Class<?> parameters[] = m.getParameterTypes();
				for (java.lang.Class<?> p : parameters) {
					if(!isSimple(p)) {
						type.addRelationship(new Relationship("dependency", type, new Class(p)));
					}
				}
				java.lang.Class<?> r = m.getReturnType();
				if(!isSimple(r) && !r.equals(void.class)) {
					type.addRelationship(new Relationship("dependency", type, new Class(r)));
				}
			}
		}
	}
	
	private static boolean isSimple(java.lang.Class<?> c) {
		if(c.isArray()) {
//			if(c.getComponentType().isPrimitive() || c.getComponentType().equals(String.class)) {
//				System.out.println(c.arrayType() + ": array type");
//				return true;
//			}
//			return false;
			return isSimple(c.getComponentType());
		}
		if (c.isPrimitive() || c.equals(String.class)) {
			return true;
		}
		return false;
	}
}
