package com.prog2.labs.db.entity;

import java.util.Objects;

/**
 * The Issued class
 *   Entity presents DB table structure
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class Issued {
	private int issued_id;
	private int book_id;
	private int user_id;
	private Status status;
	private String date_issued;
	private String date_returned;

	public Issued() {}

	public Issued(int book_id, int user_id, Status status) {
		this.book_id = book_id;
		this.user_id = user_id;
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		ISSUED, RETURNED;
	}

	public int getIssued_id() {
		return issued_id;
	}

	public void setIssued_id(int issued_id) {
		this.issued_id = issued_id;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getDate_issued() {
		return date_issued;
	}

	public void setDate_issued(String date_issued) {
		this.date_issued = date_issued;
	}

	public String getDate_returned() {
		return date_returned;
	}

	public void setDate_returned(String date_returned) {
		this.date_returned = date_returned;
	}

	@Override
	public int hashCode() {
		return Objects.hash(book_id, date_issued, date_returned, issued_id, status, user_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issued other = (Issued) obj;
		return book_id == other.book_id && Objects.equals(date_issued, other.date_issued)
				&& Objects.equals(date_returned, other.date_returned) && issued_id == other.issued_id
				&& status == other.status && user_id == other.user_id;
	}

	@Override
	public String toString() {
		return "\nIssued [issued_id=" + issued_id + ", book_id=" + book_id + ", user_id=" + user_id + ", status="
				+ status + ", date_issued=" + date_issued + ", date_returned=" + date_returned + "]";
	}
	
	
}
