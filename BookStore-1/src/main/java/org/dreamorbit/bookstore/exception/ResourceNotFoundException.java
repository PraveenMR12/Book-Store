package org.dreamorbit.bookstore.exception;

public class ResourceNotFoundException extends RuntimeException {
	private String entity;
	private String fieldName;
	private String field;
	private String message;
	
	public ResourceNotFoundException(String entity, String fieldName, String field) {
		super(String.format("%s not found of %s: %s", entity, fieldName, field));
	}

	public ResourceNotFoundException(String entity, String fieldName, String field, String message) {
		super(String.format("%s with %s: %s, %s", entity,  fieldName, field,message));
		this.fieldName = fieldName;
		this.field = field;
		this.message = message;
		this.entity = entity;
	}

}
