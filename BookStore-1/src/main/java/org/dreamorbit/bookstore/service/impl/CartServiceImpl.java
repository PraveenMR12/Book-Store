package org.dreamorbit.bookstore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.dreamorbit.bookstore.entity.Book;
import org.dreamorbit.bookstore.entity.Cart;
import org.dreamorbit.bookstore.entity.User;
import org.dreamorbit.bookstore.exception.ResourceNotFoundException;
import org.dreamorbit.bookstore.repository.BookRepository;
import org.dreamorbit.bookstore.repository.CartRepository;
import org.dreamorbit.bookstore.repository.UserRepository;
import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.CartService;
import org.dreamorbit.bookstore.util.BookDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BookRepository bookRepository;

	
	@Override
	public List<BookDto> addBooksToCart(String bookName, String jwtToken) {
		String email = jwtHelper.extractUserName(jwtToken);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", email));
		Book book1 = bookRepository.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookName));
//		Book book2 = user.getCart().getBooks().get(0);
//		if(book1==book2) {
//			System.out.println("true book");
//		}else {
//			System.out.println("false");
//		}
		
		Cart cart = user.getCart();
		List<Book> books = cart.getBooks();
		
		books.add(book1);
		cart.setBooks(books);
		cart.setTotalItems(books.size());
		
		cartRepository.save(cart);
		
		user.setCart(cart);
		userRepo.save(user);
		
		List<BookDto> dtos = books.stream()
				.map(e->modelMapper.map(e, BookDto.class)).toList();
		
		return dtos;
	}
	
	@Override
	public Map<BookDto, Integer> getAllBooks(String token) {
		
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", email));
		Cart cart = user.getCart();
		List<Book> books = cart.getBooks();
		HashMap<Book, Integer> booksCount = new HashMap<>();
		for (Book dto: books) {
			System.out.println(booksCount.put(dto, booksCount.getOrDefault(dto, 0)+1));
		}

		
		Map<BookDto, Integer> dtoCount = booksCount.entrySet().stream().collect(Collectors.toMap(e->modelMapper.map(e.getKey(), BookDto.class), Map.Entry::getValue));
	
		return dtoCount;
	}


	

	@Override
	public List<BookDto> removeFromCart(String jwtToken, String bookName) {
		String email = jwtHelper.extractUserName(jwtToken);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", email));
		Cart cart = user.getCart();
		List<Book> books = cart.getBooks();
		
		Book book = bookRepository.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("User", "BookName", bookName));
		books.remove(book);
		
		cart.setBooks(books);
		cart.setTotalItems(books.size());
		cartRepository.save(cart);
		
		List<BookDto> dtos = books.stream()
				.map(e->modelMapper.map(e, BookDto.class)).toList();
		
		return dtos;
	}

	
	@Override
	public List<BookDto> addToUser(String token) {
		System.out.println("Purchase in impl");

		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", email));
		
		List<Book> books = user.getCart().getBooks().stream()
				.map(e->bookRepository.findByBookName(e.getBookName()).orElseThrow(()->new ResourceNotFoundException("Book", "BookName", e.getBookName())))
				.collect(Collectors.toList());
		user.getBooks().addAll(books);

		user.getCart().getBooks().clear();
		user.getCart().setTotalItems(user.getCart().getBooks().size());
		
//		System.out.println(books);
		userRepo.save(user);
		
		return user.getBooks().stream()
				.map(e->modelMapper.map(e, BookDto.class)).toList();
	}

}
