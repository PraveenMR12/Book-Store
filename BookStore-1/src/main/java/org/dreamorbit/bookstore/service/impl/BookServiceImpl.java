package org.dreamorbit.bookstore.service.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.dreamorbit.bookstore.entity.Book;
import org.dreamorbit.bookstore.exception.InvalidCredentialsException;
import org.dreamorbit.bookstore.exception.ResourceNotFoundException;
import org.dreamorbit.bookstore.repository.BookRepository;
import org.dreamorbit.bookstore.service.BookService;
import org.dreamorbit.bookstore.util.BookDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookRepository bookRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public BookDto createBook(BookDto bookDto) {
		Book book = bookRepo.findByBookName(bookDto.getBookName()).orElse(null);
		if(book!=null) {
			throw new InvalidCredentialsException("Book Name", "Book with name: "+bookDto.getBookName()+" is already present.");
		}
		Book savedBook = this.bookRepo.save(modelMapper.map(bookDto, Book.class));
		return this.modelMapper.map(savedBook, BookDto.class);
	}

	@Override
	public BookDto updateBook(BookDto bookDto) {
		Book existingBook = bookRepo.findByBookName(bookDto.getBookName()).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookDto.getBookName()));
		existingBook.setAuthor(bookDto.getAuthor());
		existingBook.setPrice(Double.valueOf(bookDto.getPrice()));
		existingBook.setRating(Double.valueOf(bookDto.getRating()));
		Book updateBook = this.bookRepo.save(existingBook);
		return modelMapper.map(updateBook, BookDto.class);
	}

	@Override
	public BookDto getById(String bookName) {
		Book book = bookRepo.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookName));
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public List<BookDto> getAllBook() {
		List<Book> books = bookRepo.findAll();
		List<BookDto> bookDtos = books.stream().map(e-> modelMapper.map(e, BookDto.class)).collect(Collectors.toList());
		return bookDtos;
	}

	@Override
	public void deleteBook(String bookName, String email) {
		Book book = bookRepo.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookName));
		book.getUsers().forEach(user->user.getBooks().removeIf(e->e.equals(book)));
		book.getCarts().forEach(cart->cart.getBooks().removeIf(e->e.equals(book)));
		book.getWishlists().forEach(wishlist->wishlist.getBooks().removeIf(e->e.equals(book)));
		bookRepo.delete(book);
		
	}

}
