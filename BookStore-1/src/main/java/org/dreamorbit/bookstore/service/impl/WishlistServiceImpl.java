package org.dreamorbit.bookstore.service.impl;

import java.util.List;

import org.dreamorbit.bookstore.entity.Book;
import org.dreamorbit.bookstore.entity.User;
import org.dreamorbit.bookstore.exception.ResourceNotFoundException;
import org.dreamorbit.bookstore.repository.BookRepository;
import org.dreamorbit.bookstore.repository.UserRepository;
import org.dreamorbit.bookstore.repository.WishlistRepository;
import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.WishlistService;
import org.dreamorbit.bookstore.util.BookDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {

	@Autowired
	private WishlistRepository wishlistRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private JwtHelper jwtHelper;
	
	
	@Override
	public BookDto addBookToWishlist(String bookName, String token) {
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","Email", email));
		Book book = bookRepository.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book","Book Name", bookName));
		if(user.getWishlist().getBooks().stream().anyMatch(e->e.equals(book))) {
			throw new ResourceNotFoundException("Book", "BookName", book.getBookName(), "Already present in the Wishlist");
		}
		user.getWishlist().getBooks().add(book);
		user.getWishlist().setNoOfBooks(user.getWishlist().getBooks().size());
		userRepo.save(user);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public List<BookDto> getAllBooksInWishlist(String token) {
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		List<BookDto> bookDtos = user.getWishlist().getBooks()
				.stream().map(e->modelMapper.map(e, BookDto.class)).toList();
		return bookDtos;
	}

	@Override
	public List<BookDto> removeBooksInWishlist(String bookName, String token) {
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		user.getWishlist().getBooks().remove(bookRepository.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookName)));
		user.getWishlist().setNoOfBooks(user.getWishlist().getBooks().size());
		userRepo.save(user);
		List<BookDto> bookDtos = user.getWishlist().getBooks()
				.stream().map(e->modelMapper.map(e, BookDto.class)).toList();
		
		return bookDtos;
	}


}
