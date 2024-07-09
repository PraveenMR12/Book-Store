package org.dreamorbit.bookstore.service;

import java.util.List;
import java.util.Map;

import org.dreamorbit.bookstore.util.BookDto;
import org.dreamorbit.bookstore.util.RequestObject;
import org.dreamorbit.bookstore.util.ResetObject;
import org.dreamorbit.bookstore.util.ResponseObject;
import org.dreamorbit.bookstore.util.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService {

	UserDto signInUser(UserDto userdto);
	
	ResponseObject loginUser(RequestObject req);
	
	UserDto updateUser(UserDto userDto);
	
	void sendOtp(String email, String message);
	
	UserDto resetPassword(ResetObject reset);
	
	UserDto findById(int id);

	UserDto findByEmail(String email);

	List<UserDto> findAllUser();

	void deleteByEmail(String token);

	List<BookDto> getAllBooks(String token);

	void removeBookFromUser(String bookName, String token);

	void changeRole(String token, String otp);

	Map<BookDto, Integer> getAllBooksInMap(String token);

	
}
