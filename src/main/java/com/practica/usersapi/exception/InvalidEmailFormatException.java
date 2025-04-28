package com.practica.usersapi.exception;

public class InvalidEmailFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailFormatException(String message) {
        super(message);
    }
}