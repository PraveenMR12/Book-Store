package org.dreamorbit.bookstore.controller;

import java.util.List;

import org.dreamorbit.bookstore.entity.Role;
import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.BookService;
import org.dreamorbit.bookstore.service.UserService;
import org.dreamorbit.bookstore.util.ApiResponse;
import org.dreamorbit.bookstore.util.BookDto;
import org.dreamorbit.bookstore.util.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/book")
public class BookController {
	
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtHelper jwtHelper;
	
	@PostMapping("/create")
	public String createBook(@Valid @ModelAttribute BookDto book){
		bookService.createBook(book);
		return "redirect:/user/home";
	}
	@PostMapping("/update")
	public String updateBook(@Valid @ModelAttribute BookDto book, HttpServletRequest request){
		System.out.println(book.getBookName());
		System.out.println(book.getAuthor());
		System.out.println(book.getPrice());
		System.out.println(book.getRating());
		bookService.updateBook(book);
		return "redirect:/user/book/get/all";
	}
	
	
	
	@GetMapping("/get/byName")
	public String getByBookName(@RequestParam String bookName, Model model, HttpServletRequest request){
		String token=null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		String email = jwtHelper.extractUserName(token);
		UserDto userDto = this.userService.findByEmail(email);
		
		BookDto book = bookService.getById(bookName);
		boolean admin = false;
		if (userDto.getRole().equals(Role.ADMIN)) {
			admin = true;
		}
		model.addAttribute("admin", admin);
		
		model.addAttribute("books",book);
		return "/user/book/getBooks";
	}
	
	
	
	@GetMapping("/get/all")
	public String getAllBooks(Model model, HttpServletRequest request, HttpServletRequest req, @ModelAttribute(name = "msg") String msg, 
			HttpSession session){
		String token=null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		
		String path = req.getRequestURL().toString();
		session.setAttribute("path", path);
		model.addAttribute("msg", msg);
		
		String email = jwtHelper.extractUserName(token);
		UserDto userDto = this.userService.findByEmail(email);
		List<BookDto> books = bookService.getAllBook();
		boolean admin = false;
		if (userDto.getRole().equals(Role.ADMIN)) {
			admin = true;
		}
		model.addAttribute("admin", admin);
		model.addAttribute("books", books);
		
		
		return "/user/book/getBooks";
	}
	@GetMapping("/delete")
	public String deleteBook(@RequestParam String bookName, HttpServletRequest request){
		String token=null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		String email = jwtHelper.extractUserName(token);
		bookService.deleteBook(bookName, email);
		return "redirect:/user/book/get/all";
	}

}
