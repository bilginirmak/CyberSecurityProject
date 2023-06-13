package com.prog2.labs.db.entity;

import java.util.Objects;

/**
 * The User class
 *   Entity presents DB table structure
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class User {

	private int user_id;
	private String name;
	private Role role;
	private String login;
	private String password;
	private String contact;
	private String date_created;

	public User() {};

	public User(String name, Role role, String login, String password, String contact) {
		this.name = name;
		this.role = role;
		this.login = login;
		this.password = password;
		this.contact = contact;
	}

	public enum Role {
		STUDENT, LIBRARIAN;
	}

	/**
	 * Getters and setters
	 * @return user_id
	 */
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date_created, login, name, password, role, user_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(date_created, other.date_created) && Objects.equals(login, other.login)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password) && role == other.role
				&& user_id == other.user_id;
	}

	@Override
	public String toString() {
		return "\nUser [user_id=" + user_id + ", name=" + name + ", role=" + role + ", "
				+ "login=" + login + ", password=" + password + ", "
						+ "contact=" + contact + ", date_created=" + date_created + "]";
	}
}
