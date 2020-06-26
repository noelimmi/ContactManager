package com.immi;

public class Contact {
	private int id;
	private String name;
	private String email;
	
	public Contact() {
		
	}
	
	public Contact(int id) {
		super();
		this.id = id;
	}
	
	public Contact(int id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getemail() {
		return email;
	}
	
	public void setemail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
