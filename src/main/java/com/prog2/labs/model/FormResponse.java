package com.prog2.labs.model;

/**
 * The FormResponse Container
 * 	Implements universal Response Container
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class FormResponse<T> {

	private Status status;
	private T value;
	private String message;

	public Status getStatus() {
		return status;
	}

	public FormResponse<T> setStatus(Status status) {
		this.status = status;
		return this;
	}

	public T getValue() {
		return value;
	}

	public FormResponse<T> setValue(T value) {
		this.value = value;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public FormResponse<T> setMessage(String message) {
		this.message = message;
		return this;
	}
}
