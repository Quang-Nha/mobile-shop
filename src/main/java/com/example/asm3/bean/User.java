package com.example.asm3.bean;

public class User {
	private String email = "";
	private String password = "";
	private int role = 0;
	private String username = "";
	private String address = "";
	private String phone = "";

	private String message = "";

	public String getMessage() {
		return message;
	}

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String email, String password, int role, String username, String address, String phone) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.username = username;
		this.address = address;
		this.phone = phone;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// hàm xác thực email và password
	public boolean validate() {

		if (email == null) {
			message = "Invalid email address";
			return false;
		}

		if (password == null) {
			message = "Invalid password";
			return false;
		}

		if (!email.matches("^[A-Z0-9_a-z]+@[A-Z0-9\\.a-z]+\\.[A-Za-z]{2,6}$")) {
			message = "Invalid email address";
			return false;
		}

		else if (!password.matches("[a-zA-Z0-9_!@#$%^&*]+")) {
			message = "Password cannot contain space.";
			return false;
		}

		return true;
	}

}
