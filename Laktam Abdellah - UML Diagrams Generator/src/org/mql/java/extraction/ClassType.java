package org.mql.java.extraction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;
import org.mql.java.extraction.relationships.RelationshipEnd;

public class ClassType extends SuperType {

	public ClassType(Class<?> c) {
		this.simpleName = c.getSimpleName();
		this.fqName = c.getName();
		this.modifiers = Modifier.toString(c.getModifiers());
		Class<?> interfaces[] = c.getInterfaces();
//		this.implementedInterfaces = new Vector<InterfaceType>();
		for (Class<?> i : interfaces) {
//			this.implementedInterfaces.add(new InterfaceType(i));
			this.relationships.add(new Relationship("implementation", new RelationshipEnd(simpleName, fqName, "from"), 
					new RelationshipEnd(i.getSimpleName(), i.getName(), "to")));
		}

		//
		if (!Object.class.equals(c.getSuperclass()) && c.getSuperclass() != null) {
//			this.parent = new ClassType(c.getSuperclass());
			this.relationships.add(new Relationship("extension", new RelationshipEnd(simpleName, fqName, "from"),
					new RelationshipEnd(c.getSuperclass().getSimpleName(), c.getSuperclass().getName(), "to")));

		}

		
//		this.fields = new Vector<FieldType>();
		Field fields[] = c.getDeclaredFields();

		for (Field f : fields) {
			// i used f.getGenericType because i need to detect aggregation like when i use
			// List<Type> 
			this.fields.add(new FieldType(f));
		}
		//
//		this.constructors = new Vector<ConstructorType>();
		Constructor<?> constructors[] = c.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			Parameter parameters[] = constructor.getParameters();
			if (parameters.length != 0) {
				this.constructors.add(new ConstructorType(this.simpleName,this.fqName, parameters));
			}
		}
		//
//		this.methods = new Vector<MethodType>();
		Method methods[] = c.getDeclaredMethods();
		for (Method m : methods) {
			
			this.methods.add(new MethodType(m.getName(), Modifier.toString(m.getModifiers()), m.getParameters(),
					m.getGenericReturnType()));
		}
	}

	
}
