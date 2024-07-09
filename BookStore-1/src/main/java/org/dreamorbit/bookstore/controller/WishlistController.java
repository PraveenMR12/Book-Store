package org.dreamorbit.bookstore.controller;

import java.util.List;

import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.WishlistService;
import org.dreamorbit.bookstore.util.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/wishlist")
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;
	
	@GetMapping("/addBook")
	public String addBookToWishlist(@RequestParam String bookName, HttpServletRequest request){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		wishlistService.addBookToWishlist(bookName, jwtToken);
		return "redirect:/user/book/get/all";
		
	}
	
	@GetMapping("/get/all")
	public String getAllBooksInWishlist(HttpServletRequest request, Model model){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		List<BookDto> books = wishlistService.getAllBooksInWishlist(jwtToken);
		if(books.size()==0) {
			books=null;
		}
		model.addAttribute("books", books);
		return "user/home/wishlist";
		
	}
	
	@GetMapping("/remove")
	public String removeBooksInWishlist(@RequestParam String bookName, HttpServletRequest request){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		wishlistService.removeBooksInWishlist(bookName, jwtToken);
		return "redirect:/user/wishlist/get/all";
		
	}
	
}
