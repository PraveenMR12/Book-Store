package org.dreamorbit.bookstore.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.dreamorbit.bookstore.util.ApiResponse;
import org.dreamorbit.bookstore.util.BookDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class NavController {

	//User
	@GetMapping("/user/signup")
	public String signUpPage(@ModelAttribute( name = "msg") String msg, HttpServletResponse respo,Model model, HttpSession session, HttpServletRequest request) {
		
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setPath("/user");
		respo.addCookie(cookie);
		Map<String, String> map = (HashMap<String, String>)session.getAttribute("error");
		session.removeAttribute("error");
		model.addAttribute("msg", map);
		model.addAttribute("error", msg);
		
		return "signuppage";
	}
	
	@GetMapping("/user/login")
	public String loginPage(HttpServletResponse respo, @ModelAttribute( name = "msg") String msg, Model model, HttpSession session, HttpServletRequest request) {
		
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setPath("/user");
		respo.addCookie(cookie);
		model.addAttribute("msg", msg);
		return "loginPage";
	}
	
	@GetMapping("/user/home/updateUser")
	public String updatePage(@ModelAttribute(name = "msg") String msg, Model model, HttpSession session, HttpServletRequest request) {
		String path = request.getRequestURL().toString();
		session.setAttribute("path", path);
		model.addAttribute("msg", msg);
		return "user/home/updatePage";
	}
	
	@GetMapping("/user/home/admin/get")
	public String findUserPage(Model m) {
		return "user/home/getUser";
	}
	
	@GetMapping("/user/home/logoutUser")
	public String  logoutUser(HttpServletResponse respo){
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setPath("/user");
		respo.addCookie(cookie);
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/forgotPassword")
	public String forgotPassword(@ModelAttribute(name = "msg") String msg, 
			Model model, 
			HttpSession session, 
			HttpServletRequest request) {
		
		String path = request.getRequestURL().toString();
		session.setAttribute("path", path);
		model.addAttribute("msg", msg);
		
		return "user/forgotPassword";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(@ModelAttribute(name = "msg") String msg, 
			Model model, 
			HttpSession session, 
			HttpServletRequest request) {
		Map<String, String> map = (HashMap<String, String>)session.getAttribute("error");
		session.removeAttribute("error");
		String path = request.getRequestURL().toString();
		session.setAttribute("path", path);
		model.addAttribute("map", msg);
		model.addAttribute("map", map);

		return "user/resetPassword";
	}
	
	
	
	//Book
	@GetMapping("/user/home/admin/updateBook")
	public String updateBook(@RequestParam String bookName, @RequestParam String author, @RequestParam String rating, @RequestParam String price, 
			RedirectAttributes redirect, HttpSession session) {
		BookDto book = new BookDto();
		book.setBookName(bookName);
		book.setAuthor(author);
		book.setPrice(price);
		book.setRating(rating);
		session.setAttribute("book", book);
		return "redirect:/user/home/admin/updatePage";
	}
	
	@GetMapping("/user/home/admin/updatePage")
	public String updateBook(Model model, 
			HttpSession session, 
			HttpServletRequest request) {
		String path = request.getRequestURL().toString();
		System.out.println("hello");
		session.setAttribute("path", path);
		BookDto book = (BookDto)session.getAttribute("book");
//		session.removeAttribute("book");
		System.out.println(book);
		Map<String, String> map = (HashMap<String, String>)session.getAttribute("error");
		session.removeAttribute("error");
		System.out.println(map);
		model.addAttribute("book",book);
		model.addAttribute("msg", map);
		return "user/book/updateBook";
	}
	
	
	@GetMapping("/user/home/getBook")
	public String findBook(Model m) {
		return "user/book/getBooks";
	}
	
	@GetMapping("/user/home/loadBook")
	public String loadBook(Model m) {
		return "user/book/viewBook";
	}
	
	@GetMapping("/user/home/admin/createBook")
	public String  createBook( @ModelAttribute(name = "msg") String msg, HttpServletRequest request, HttpSession session, Model model){
		String path = request.getRequestURL().toString();
		session.setAttribute("path", path);
		Map<String, String> map = (HashMap<String, String>)session.getAttribute("error");
		session.removeAttribute("error");
		model.addAttribute("msg", map);
		model.addAttribute("text", msg);
		return "user/book/addBook";
	}
	
	
	//User
	@GetMapping("/user/home/otpVerification")
	public String otpVerification(HttpServletRequest req, @ModelAttribute(name = "msg") String msg, 
			Model model, 
			HttpSession session){
		
		String path = req.getRequestURL().toString();
		session.setAttribute("path", path);
		model.addAttribute("msg", msg);
		return "user/home/otpVerification";
	}
	
	//error
	@GetMapping("/user/error")
	public String otpVerification(){
		
		return "user/error";
	}
}
