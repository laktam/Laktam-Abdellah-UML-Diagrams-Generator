package org.mql.java.extraction.relationships;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.mql.java.extraction.ClassType;
import org.mql.java.extraction.FieldType;
import org.mql.java.extraction.MethodType;
import org.mql.java.extraction.ParameterType;
import org.mql.java.extraction.Type;

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
			List<FieldType> fields = type.getFields();
			// agregation : if a field's type is complex
			for (FieldType f : fields) {

				Class<?> fieldClass = f.getType();
				if (!isSimple(fieldClass)) {// !fieldClass.isPrimitive() && !fieldClass.equals(String.class)
					type.addRelationship(new Relationship("agregation", type, new ClassType(fieldClass)));
				}
			}

			// Inheritance and implementation
			// done in InterfaceType and ClassType constructors

			// check constructor parameters ??
			// dependency : detect it if a method has a return type or a parameter of an
			// external class
			List<MethodType> methods = type.getMethods();
			for (MethodType m : methods) {
				List<ParameterType> parameters = m.getParameters();
				for (ParameterType p : parameters) {
					if (!isSimple(p.getType())) {
						type.addRelationship(new Relationship("dependency", type, new ClassType(p.getType())));
					}
				}
				Class<?> r = m.getReturnType();
				if (!isSimple(r) && !r.equals(void.class)) {
					type.addRelationship(new Relationship("dependency", type, new ClassType(r)));
				}
			}
		}

	}

	private static boolean isSimple(java.lang.Class<?> c) {
		if (c.isArray()) {
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
