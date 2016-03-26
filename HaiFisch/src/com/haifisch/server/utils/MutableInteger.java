package com.haifisch.server.utils;

/**
 * A mutable integer wrapper class of int 
 */

public class MutableInteger implements Comparable<MutableInteger> {
	
	private int integer;
	
	public MutableInteger(int integer){
		this.integer = integer;
	}
	
	public void setInteger(int integer){
		this.integer = integer;
	}
	
	public int getInteger(){
		return this.integer;
	}
	
	public void increment(){
		this.integer++;
	}
	
	public void incrementBy(int add){
		this.integer += add;
	}

	@Override
	public int compareTo(MutableInteger o) {
		Integer.compare(this.getInteger(), o.getInteger());
		return 0;
	}
	
}
