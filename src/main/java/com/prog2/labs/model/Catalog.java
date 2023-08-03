package com.prog2.labs.model;

import java.util.List;

import com.prog2.labs.db.entity.Book;

public class Catalog {
	
	private List<Book> books;

	public Catalog(List<Book> books) {
		super();
		this.books = books;
	}

	@Override
	public String toString() {
		return "Catalog [books=" + books + "]";
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
