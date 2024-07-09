package org.dreamorbit.bookstore.service;

import java.util.List;
import java.util.Map;

import org.dreamorbit.bookstore.util.BookDto;

public interface CartService {

	Map<BookDto, Integer> getAllBooks(String jwtToken);

	List<BookDto> addBooksToCart(String bookName, String jwtToken);

	List<BookDto> addToUser(String jwtToken);

	List<BookDto> removeFromCart(String jwtToken, String bookName);

}
