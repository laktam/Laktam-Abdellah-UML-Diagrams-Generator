package org.mql.java.extraction.relationships;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.extraction.ClassType;
import org.mql.java.extraction.FieldType;
import org.mql.java.extraction.MethodType;
import org.mql.java.extraction.ParameterType;
import org.mql.java.extraction.SuperType;

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
	public static void detectRelationShips(List<SuperType> types) {
		for (SuperType type : types) {
			RelationshipEnd thisEnd = new RelationshipEnd(type.getSimpleName(), type.getFQName(), "from");

			List<FieldType> fields = type.getFields();
			// agregation : if a field's type is complex
			for (FieldType f : fields) {

				// if it's a collection we need to add the Types in <__>
				if (f.isParameterized() && f.isCollection()) {
					Type typeArguments[] = f.getTypeArguments();
					if (typeArguments != null) {
						for (Type typeArgument : typeArguments) {
							Class<?> c = (Class<?>) typeArgument;
							// we need to test if the argument is not of type wrapper class
							if (!isWrapper(c) && !c.equals(String.class)) {
								type.addRelationship(new Relationship("agregation", thisEnd,
										new RelationshipEnd(c.getSimpleName(), c.getName(), "to")));
							}
						}
					}
				} else if (!f.isSimple()) {
					// if is an array
					if (f.isArray()) {
						type.addRelationship(new Relationship("agregation", thisEnd,
								new RelationshipEnd(f.getArrayTypeSimpleName(), f.getArrayTypeFQName(), "to")));
					} else {

						type.addRelationship(new Relationship("agregation", thisEnd,
								new RelationshipEnd(f.getTypeSimpleName(), f.getTypeFQName(), "to")));

					}

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
					// if we have List<Type> then the dependence must be to List and Type
					if (p.isCollection()) {
						// add raw type
						type.addRelationship(new Relationship("agregation", thisEnd,
								new RelationshipEnd(p.getTypeSimpleName(), p.getTypeFQName(), "to")));
						// p.getTypeSimpleName(), p.getTypeFQName() need to return raw type name
						// add Type arguments
						Type typeArguments[] = p.getTypeArguments();
						if (typeArguments != null) {
							for (Type typeArgument : typeArguments) {
								Class<?> c = (Class<?>) typeArgument;
								type.addRelationship(new Relationship("agregation", thisEnd,
										new RelationshipEnd(c.getSimpleName(), c.getName(), "to")));
							}
						}
					} else if (!p.isSimple()) {
						type.addRelationship(new Relationship("dependency", thisEnd,
								new RelationshipEnd(p.getTypeSimpleName(), p.getTypeFQName(), "to")));
					}
				}
				// Parameterized return Type
				Type returnType = m.getReturnType();
				if (returnType instanceof ParameterizedType) {
					ParameterizedType pReturnType = (ParameterizedType) returnType;
					Type typeArguments[] = pReturnType.getActualTypeArguments();
					if (typeArguments != null) {
						for (Type typeArgument : typeArguments) {
							Class<?> c = (Class<?>) typeArgument;
							type.addRelationship(new Relationship("dependency", thisEnd,
									new RelationshipEnd(c.getSimpleName(), c.getName(), "to")));
						}
					}
				} else if (returnType instanceof Class<?>) {
					Class<?> cReturnType = (Class<?>) returnType;
					if (m.complexReturnTypeAndNotVoid()) {
						type.addRelationship(new Relationship("dependency", thisEnd, new RelationshipEnd(
								cReturnType.getSimpleName(), cReturnType.getName(), "to")));
					}
				}

			}
		}

	}

	private static boolean isWrapper(Class<?> c) {
		if (c.isArray()) {
			// array of wrapper type
			if (c.getComponentType() == Double.class || c.getComponentType() == Float.class
					|| c.getComponentType() == Long.class || c.getComponentType() == Integer.class
					|| c.getComponentType() == Short.class || c.getComponentType() == Character.class
					|| c.getComponentType() == Byte.class || c.getComponentType() == Boolean.class) {
				return true;
			}
			return false;
		}
		
		if (c == Double.class || c == Float.class || c == Long.class || c == Integer.class || c == Short.class
				|| c == Character.class || c == Byte.class || c == Boolean.class) {
			return true;
		}
		return false;
	}
}
