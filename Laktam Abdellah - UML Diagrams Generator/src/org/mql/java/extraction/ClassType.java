package org.mql.java.extraction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

public class ClassType {
	private String name;
	private String modifiers;
	private List<FieldType> fields;
	private List<ConstructorType> constructors;
	private List<MethodType> methods;

	public ClassType(Class<?> c) {
		this.name = c.getSimpleName();
		this.modifiers = Modifier.toString(c.getModifiers()) ;
		this.fields = new Vector<FieldType>();
		Field fields[] = c.getDeclaredFields();
		for (Field f : fields) {	
			this.fields.add(new FieldType(Modifier.toString(f.getModifiers()), f.getName(), f.getType()));
		}
		//
		this.constructors = new Vector<ConstructorType>();
		Constructor<?> constructors[] = c.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?> parameters[]= constructor.getParameterTypes();
			this.constructors.add(new ConstructorType(constructor.getName(), parameters));
		}
		//
		this.methods = new Vector<MethodType>();
		Method methods[] = c.getDeclaredMethods();
		for (Method m : methods) {
			this.methods.add(new MethodType(m.getName(), Modifier.toString(m.getModifiers()), m.getParameterTypes(), m.getReturnType()));
		}
	}

	
	public List<FieldType> getFields(){
		return fields;
	}

	
	public List<ConstructorType> getConstructors(){
		return constructors;
	}

	
	public List<MethodType> getMethods(){
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
