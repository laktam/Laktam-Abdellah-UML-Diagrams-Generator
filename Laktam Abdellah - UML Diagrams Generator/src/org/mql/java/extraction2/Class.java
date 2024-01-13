package org.mql.java.extraction2;
	
public class Class extends Type{
	
	public Class(java.lang.Class<?> c) {
		super(c, "class");
	}
	//private Class parent;//it it's null then parent is Object.class
	
//	public Class(java.lang.Class<?> c) {
//		this.c = c;
//	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return c.getTypeName();
	}

}
