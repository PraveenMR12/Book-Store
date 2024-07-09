package org.dreamorbit.bookstore.service;

import java.util.List;

import org.dreamorbit.bookstore.util.BookDto;

public interface BookService {

	BookDto createBook(BookDto book);

	BookDto updateBook(BookDto book);

	BookDto getById(String bookName);

	List<BookDto> getAllBook();

	void deleteBook(String bookName, String email);

}
