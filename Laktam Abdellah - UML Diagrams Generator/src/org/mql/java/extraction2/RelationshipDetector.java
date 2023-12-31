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
			Field fields[] = type.getObjectClass().getDeclaredFields();
			for (Field f : fields) {
				// f.get(type).getClass().isPrimitive() : get the class of the field's object
				
				//need to redo this       !!!!!!!!! !!!!!!!!!!!!!!!!!!!!        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				f.setAccessible(true);
				try {
					java.lang.Class<?> fieldClass = f.getType();
					if (!fieldClass.isPrimitive() && !fieldClass.equals(String.class)) {
						type.addRelationship(new Relationship("agregation", (Class) type, new Class(fieldClass)));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
