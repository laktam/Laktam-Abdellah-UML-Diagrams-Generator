package org.mql.java.extraction2;
	
public class Class extends Type{
	//private Class parent;//it it's null then parent is Object.class
	
	public Class(java.lang.Class<?> c) {
		this.c = c;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return c.getTypeName();
	}
	
	public Class getParent(){
		if(!c.getSuperclass().equals(Object.class)) {
			return new Class(c.getSuperclass());
		}
		return null;//object is the parent
	}

	
	
}
