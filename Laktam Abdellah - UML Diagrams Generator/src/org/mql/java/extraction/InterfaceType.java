package org.mql.java.extraction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;

public class InterfaceType {
	private String name;
	private List<MethodType> methods;
	private List<FieldType> fields;
	
	InterfaceType(Class<?> c){
		this.name = c.getSimpleName();
		//
		this.fields = new Vector<FieldType>();
		Field fields[] = c.getDeclaredFields();
		for (Field f : fields) {	
			this.fields.add(new FieldType(Modifier.toString(f.getModifiers()), f.getName(), f.getType()));
		}
		//
		this.methods = new Vector<MethodType>();
		Method methods[] = c.getDeclaredMethods();
		for (Method m : methods) {
			this.methods.add(new MethodType(m.getName(), Modifier.toString(m.getModifiers()), m.getParameters(), m.getReturnType()));
		}
	}
	
	public void addField(FieldType field) {
		fields.add(field);
//		fields.add(new Field("public static final", name, type));
	}
	
	public List<FieldType> getFields() {
		return fields;
	}
	
	public List<MethodType> getMethods(){
		return methods;
	}
	public void addMethod(MethodType method) {
		methods.add(method);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
