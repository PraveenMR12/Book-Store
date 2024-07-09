package org.dreamorbit.bookstore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.dreamorbit.bookstore.entity.Book;
import org.dreamorbit.bookstore.entity.Cart;
import org.dreamorbit.bookstore.entity.Role;
import org.dreamorbit.bookstore.entity.User;
import org.dreamorbit.bookstore.entity.Wishlist;
import org.dreamorbit.bookstore.exception.InvalidCredentialsException;
import org.dreamorbit.bookstore.exception.ResourceNotFoundException;
import org.dreamorbit.bookstore.repository.BookRepository;
import org.dreamorbit.bookstore.repository.CartRepository;
import org.dreamorbit.bookstore.repository.UserRepository;
import org.dreamorbit.bookstore.repository.WishlistRepository;
import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.UserService;
import org.dreamorbit.bookstore.util.BookDto;
import org.dreamorbit.bookstore.util.RequestObject;
import org.dreamorbit.bookstore.util.ResetObject;
import org.dreamorbit.bookstore.util.ResponseObject;
import org.dreamorbit.bookstore.util.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private WishlistRepository wishlistRepository;
	@Autowired
	private BookRepository bookRepo;
	
	private String mail = "ghost.ps.2010@gmail.com";
	private int otp;
	private String resetEmail;
	
	@Override
	public UserDto signInUser(UserDto userdto) {
		User userPresent = userRepo.findByEmail(userdto.getEmail()).orElse(null);
		if(userPresent!=null) {
			throw new InvalidCredentialsException("Email", "Email already present!");
		}
		User user = this.modelMapper.map(userdto, User.class);
		user.setCreatedDate(new Date());
		user.setModifiedDate(new Date());
		user.setPassword(passwordEncoder.encode(userdto.getPassword()));
		user.setRole(Role.USER);
		
		Cart cart = new Cart();
		cart.setBooks(new ArrayList<>());
		
		Wishlist wishlist = new Wishlist();
		wishlist.setBooks(new ArrayList<>());
		
		user.setCart(cart);
		user.setWishlist(wishlist);
		
		wishlistRepository.save(wishlist);
		cartRepository.save(cart);
		
		user.setBooks(new ArrayList<>());
		
		User savedUser = userRepo.save(user);
//		System.out.println(userdto.getPassword());

	
		
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public ResponseObject loginUser(RequestObject req) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
		}catch (Exception e) {
			throw new InvalidCredentialsException("Incorrect", "Email & Password");
		}
		User user = userRepo.findByEmail(req.getEmail()).orElseThrow(()->new ResourceNotFoundException("User", "email", req.getEmail()));
		ResponseObject response = new ResponseObject();
		response.setToken(jwtHelper.generateToken(user));
		response.setEmail(user.getEmail());
		return response;
	}

	@Override
	public UserDto updateUser(UserDto userDto) {
		User user = userRepo.findByEmail(userDto.getEmail()).orElseThrow(()->new ResourceNotFoundException("User", "Email", userDto.getEmail()));
		if (!userDto.getFullName().equals("")) {
			user.setFullName(userDto.getFullName());
		}
		if (!userDto.getPassword().equals("")) {
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}
		if (!userDto.getPhoneNumber().equals("")) {
			user.setPhoneNumber(Long.valueOf(userDto.getPhoneNumber()));
		}
		if(userDto.getFullName().equals("") && userDto.getPassword().equals("") && userDto.getPhoneNumber().equals("")) {
			throw new InvalidCredentialsException("Fields are Empty", "Enter atleast one");
		}
		user.setModifiedDate(new Date());
		User updatedUser = userRepo.save(user);
		return this.modelMapper.map(updatedUser , UserDto.class);
	}

	@Override
	public void sendOtp(String email, String subject) {
		this.otp = new Random().nextInt(999999);
		SimpleMailMessage message = new SimpleMailMessage();
		if(subject.equalsIgnoreCase("Reset Password")) {
			User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
			this.resetEmail = user.getEmail();
		}
		message.setTo(email);
		message.setFrom(this.mail);
		message.setText(String.format("The otp for %s is: %06d",subject, otp));
		message.setSubject(subject);
		
		mailSender.send(message);

	}

	@Override
	public UserDto resetPassword(ResetObject reset) {
		try {
		if(this.otp !=Integer.valueOf(reset.getOtp()) || this.otp == 0) {
			throw new InvalidCredentialsException("invalid otp", reset.getOtp());
		}
		}catch(NumberFormatException e) {
			throw new InvalidCredentialsException("Invalid otp", "Enter numbers only");
		}
		
		User user = userRepo.findByEmail(resetEmail).orElseThrow(()-> new ResourceNotFoundException("User", "email", resetEmail));
		user.setPassword(passwordEncoder.encode(reset.getNewPassword()));
		user.setModifiedDate(new Date());
		
		User updatedUser = userRepo.save(user);
		this.resetEmail =null;
		this.otp = 0;
		return modelMapper.map(updatedUser, UserDto.class);

	}

	@Override
	public UserDto findById(int id) {
		User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User", "id", Integer.toString(id)));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public void deleteByEmail(String token) {
		String email = this.jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		userRepo.delete(user);

	}

	@Override
	public List<UserDto> findAllUser() {
		List<User> users = userRepo.findAll();
		List<UserDto> dtos = users.stream()
				.map(user->modelMapper.map(user, UserDto.class))
				.toList();
		return dtos;
	}

	@Override
	public UserDto findByEmail(String email) {
	
//		String email = this.jwtHelper.extractUserName(token);
		
		return modelMapper.map(this.userRepo.findByEmail(email), UserDto.class);
	}

	@Override
	public List<BookDto> getAllBooks(String token) {
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		List<Book> books = user.getBooks();
		
		return books.stream().map(e->modelMapper.map(e, BookDto.class)).toList();
	}
	
	@Override
	public Map<BookDto, Integer> getAllBooksInMap(String token) {
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		List<Book> books = user.getBooks();
		HashMap<Book, Integer> booksCount = new HashMap<>();
		for (Book dto: books) {
			System.out.println(booksCount.put(dto, booksCount.getOrDefault(dto, 0)+1));
		}
		Map<BookDto, Integer> dtoCount = booksCount.entrySet().stream().collect(Collectors.toMap(e->modelMapper.map(e.getKey(), BookDto.class), Map.Entry::getValue));
		
		return dtoCount;
	}

	@Override
	public void removeBookFromUser(String bookName, String token) {
		
		String email = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		List<Book> books = user.getBooks();
		books.remove(bookRepo.findByBookName(bookName).orElseThrow(()->new ResourceNotFoundException("Book", "Book Name", bookName)));
		user.setBooks(books);
		userRepo.save(user);
		
		
	}

	@Override
	public void changeRole(String token, String otp) {
		String email  = jwtHelper.extractUserName(token);
		User user = userRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "Email", email));
		if(!otp.equals("") && Integer.valueOf(otp) == this.otp) {
			if(user.getRole().equals(Role.USER))
				user.setRole(Role.ADMIN);
			else
				user.setRole(Role.USER);
		}else {
			throw new InvalidCredentialsException("invalid otp", otp);
		}
		userRepo.save(user);
		this.otp = 0;
	}
	
	

}
