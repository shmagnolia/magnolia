package com.magnolia.exception;

public class GeneratorException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2796682368906708180L;

	
	public GeneratorException(String msg){
		super(msg);
	}
	
	public GeneratorException(Exception e){
		super(e);
	}
}
