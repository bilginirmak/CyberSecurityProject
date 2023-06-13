package com.prog2.labs.db.entity;

import java.util.Objects;

/**
 * The Book class
 *   Entity presents DB table structure
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class Book {
	
	private int book_id;
	private String isbn;
	private String title;
	private String author;
	private String publisher;
	private double price;
	private int quantity;
	private String dateAdded;
	
	public Book() {};

	public Book(String isbn, String title, String author, String publisher, double price, int quantity) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * Getters and setters
	 * @return
	 */

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, book_id, dateAdded, isbn, price, publisher, quantity, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(author, other.author) && book_id == other.book_id
				&& Objects.equals(dateAdded, other.dateAdded) && Objects.equals(isbn, other.isbn)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(publisher, other.publisher) && quantity == other.quantity
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "\nBook [book_id=" + book_id + ", isbn=" + isbn + ", title=" + title + ", author=" + author
				+ ", publisher=" + publisher + ", price=" + price + ", quantity=" + quantity + ", dateAdded="
				+ dateAdded + "]";
	}
}
