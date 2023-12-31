package org.mql.java.extraction2;


public class Interface {
	private java.lang.Class<?> c;
	
	Interface(java.lang.Class<?> c){
		this.c = c;
	}
	
	public java.lang.Class<?>[] getExtendedInterfaces(){
		return c.getInterfaces();
	}
	
	
	
}
