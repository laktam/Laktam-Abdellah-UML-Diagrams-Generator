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

public class ClassType extends Type {
//	private String name;
//	private String modifiers;
//	private List<FieldType> fields;
//	private List<ConstructorType> constructors;
//	private List<MethodType> methods;
//	private List<InterfaceType> implementedInterfaces;
//	private ClassType parent;

	public ClassType(Class<?> c) {
		Class<?> interfaces[] = c.getInterfaces();
//		this.implementedInterfaces = new Vector<InterfaceType>();
		for (Class<?> i : interfaces) {
//			this.implementedInterfaces.add(new InterfaceType(i));
			this.relationships.add(new Relationship("implementation", this, new InterfaceType(i)));
		}

		//
		if (!Object.class.equals(c.getSuperclass()) && c.getSuperclass() != null) {
//			this.parent = new ClassType(c.getSuperclass());
			this.relationships.add(new Relationship("extension", this, new ClassType(c.getSuperclass())));

		}

		this.name = c.getSimpleName();
		this.modifiers = Modifier.toString(c.getModifiers());
//		this.fields = new Vector<FieldType>();
		Field fields[] = c.getDeclaredFields();

		for (Field f : fields) {
			// i used f.getGenericType because i need to detect aggregation like when i use
			// List<Type> 
			this.fields.add(new FieldType(Modifier.toString(f.getModifiers()), f.getName(), f.getGenericType()));
		}
		//
//		this.constructors = new Vector<ConstructorType>();
		Constructor<?> constructors[] = c.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			Parameter parameters[] = constructor.getParameters();
			if (parameters.length != 0) {
				this.constructors.add(new ConstructorType(this.name, parameters));
			}
		}
		//
//		this.methods = new Vector<MethodType>();
		Method methods[] = c.getDeclaredMethods();
		for (Method m : methods) {
			this.methods.add(new MethodType(m.getName(), Modifier.toString(m.getModifiers()), m.getParameters(),
					m.getReturnType()));
		}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
}
