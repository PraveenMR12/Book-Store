package org.dreamorbit.bookstore.service;

import java.util.List;

import org.dreamorbit.bookstore.util.BookDto;
import org.springframework.http.HttpStatusCode;

public interface WishlistService {

	BookDto addBookToWishlist(String bookName, String token);

	List<BookDto> getAllBooksInWishlist(String token);

	List<BookDto> removeBooksInWishlist(String bookName, String token);

	

}
