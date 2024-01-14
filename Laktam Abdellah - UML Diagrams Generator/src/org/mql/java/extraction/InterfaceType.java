package org.mql.java.extraction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;

public class InterfaceType extends SuperType{
//	private String name;
//	private List<MethodType> methods;
//	private List<FieldType> fields;
	private List<InterfaceType> extendedInterfaces;
	InterfaceType(Class<?> c){
		Class<?> interfaces[] = c.getInterfaces();
		this.extendedInterfaces = new Vector<InterfaceType>();
		for (Class<?> i : interfaces) {
			this.extendedInterfaces.add(new InterfaceType(i));
		}
		
		this.simpleName = c.getSimpleName();
		this.fqName = c.getName();
		//
		this.fields = new Vector<FieldType>();
		Field fields[] = c.getDeclaredFields();
		for (Field f : fields) {	
			this.fields.add(new FieldType(f));
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

	
	
}
