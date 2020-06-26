package com.immi;

public class Error {
	private String type;
	private String message;
	
	public Error() {
		
	}
	
	public Error(String type) {
		super();
		this.type = type;
	}
	
	public Error(String type, String message) {
		super();
		this.message = message;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
