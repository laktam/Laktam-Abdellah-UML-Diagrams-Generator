package org.mql.java.extraction2;

import java.lang.reflect.Field;
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
					if (!fieldClass.isPrimitive() && !fieldClass.equals(String.class)) {
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
			//dependency : detect it if a method has a return type or a parameter of an external class
			
		}
	}
}
