package org.mql.java.extraction.relationships;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

				java.lang.reflect.Type fieldType = f.getType();
//				if (!isSimple((Class<?>) fieldType)) {
				// test if it's a collection
				if (fieldType instanceof ParameterizedType) {
					java.lang.reflect.Type typeArguments[] = getCollectionTypeArguments((ParameterizedType) fieldType);
					if (typeArguments != null) {
						for (java.lang.reflect.Type typeArgument : typeArguments) {
							System.out.println(typeArgument);
							type.addRelationship(
									new Relationship("agregation", type, new ClassType((Class<?>) typeArgument)));
						}
					}
				} else {
					if (!isSimple((Class<?>) fieldType)) {
						// if is an array
						if (((Class<?>) fieldType).isArray()) {
							type.addRelationship(
									new Relationship("agregation", type, new ClassType(((Class<?>) fieldType).componentType())));
						} else {
							type.addRelationship(
									new Relationship("agregation", type, new ClassType((Class<?>) fieldType)));

						}

					}
				}

//				}
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

	private static java.lang.reflect.Type[] getCollectionTypeArguments(ParameterizedType t) {
//		java.lang.reflect.Type[] typeParameters = t.getActualTypeArguments();

		List<Class<?>> superInterfaces = new Vector<Class<?>>();
		getAllSuperInterfaces((Class<?>) t.getRawType(), superInterfaces);
		// implements iterable or Map ?
		if (superInterfaces.contains(Iterable.class) || superInterfaces.contains(Map.class)) {
//			System.out.println(t.getActualTypeArguments());
			return t.getActualTypeArguments();
		}
		return null;
	}

	private static void getAllSuperInterfaces(Class<?> c, List<Class<?>> interfaces) {
		Class<?> list[] = c.getInterfaces();
		interfaces.addAll(List.of(list));
		for (Class<?> i : list) {
			getAllSuperInterfaces(i, interfaces);
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
