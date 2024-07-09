package org.dreamorbit.bookstore.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dreamorbit.bookstore.service.CartService;
import org.dreamorbit.bookstore.util.BookDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	
	@GetMapping("/addBook")
	public String addCart(@RequestParam String bookName, HttpServletRequest request){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		cartService.addBooksToCart(bookName, jwtToken);
		
		
		return "redirect:/user/cart/get/all";
	}
	
	@GetMapping("/get/all")
	public String showCart(HttpServletRequest request, Model model){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		Map<BookDto, Integer> booksCount = cartService.getAllBooks(jwtToken);
		System.out.println("true");
		
		System.out.println(booksCount);
		if(booksCount.size()==0) {
			booksCount=null;;
		}
		model.addAttribute("books", booksCount);
		return "/user/home/cart";
	}
	
	@GetMapping("/remove")
	public String removeFromCart(@RequestParam String bookName, HttpServletRequest request){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		cartService.removeFromCart(jwtToken,bookName);
		
		return "redirect:/user/cart/get/all";
	}
	@GetMapping("/purchase")
	public String purchaseBook(HttpServletRequest request){
		Cookie[] cookies =  request.getCookies();
		String jwtToken = null;
		for(Cookie c: cookies) {
			if(c.getName().equals("token")) {
				jwtToken = c.getValue();
			}
		}
		cartService.addToUser(jwtToken);
		return "redirect:/user/home";
	} 
	
}
