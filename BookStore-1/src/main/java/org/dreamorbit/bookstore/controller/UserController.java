package org.dreamorbit.bookstore.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dreamorbit.bookstore.entity.Role;
import org.dreamorbit.bookstore.security.JwtHelper;
import org.dreamorbit.bookstore.service.UserService;
import org.dreamorbit.bookstore.util.ApiResponse;
import org.dreamorbit.bookstore.util.BookDto;
import org.dreamorbit.bookstore.util.RequestObject;
import org.dreamorbit.bookstore.util.ResetObject;
import org.dreamorbit.bookstore.util.ResponseObject;
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
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService usrService;
	@Autowired
	private JwtHelper jwtHelper;
	
	@PostMapping("/signup")
	public String createUser(@Valid @ModelAttribute UserDto userDto){
		
		usrService.signInUser(userDto);
		return "loginPage";
	}
	
	
	@PostMapping("/login")
	public String loginUser(@ModelAttribute RequestObject request, Model m, HttpSession session,
			 HttpServletResponse res){
		ResponseObject response = usrService.loginUser(request);
		final String token = response.getToken();
		System.out.println("called formlkdjmsd dsjkldf ");
		Cookie cookie = new Cookie("token",token);
		cookie.setMaxAge(21600);
		res.addCookie(cookie);
		
		return "redirect:/user/home";
	}
	
	@PostMapping("/forgotPassword")
	public String forgotPassword(@ModelAttribute(name = "email") String email){
//		System.out.println("email"+email);
		usrService.sendOtp(email, "Reset Password");
		
		return "redirect:/resetPassword";
	}
	
	
	@PostMapping("/resetPassword")
	public String resetPassword(@Valid @ModelAttribute ResetObject reset){
		UserDto dto = usrService.resetPassword(reset);
		return "redirect:/user/login";
	}
	
	
	
	
	
	@GetMapping("/home")
	public String homePage(Model m, HttpServletRequest request, HttpSession session, @ModelAttribute( name = "msg") String msg) {
		System.out.println("Home");
		
		String token =null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		
		Map<BookDto, Integer> booksCount = usrService.getAllBooksInMap(token);
		
		UserDto userDto= usrService.findByEmail(jwtHelper.extractUserName(token));
		boolean admin = false;
		if (userDto.getRole().equals(Role.ADMIN)) {
			admin = true;
		}
		if(booksCount.size()==0) {
			booksCount=null;;
		}
		System.out.println(booksCount);
		
		m.addAttribute("User", userDto);
		m.addAttribute("books", booksCount);
		m.addAttribute("admin", admin);
		m.addAttribute("msg", msg);
		
		System.out.println("""
				  /\\___/\\
				 ( ' _ ' )
				[|       |]  *****************
				 |       |   *****************
				 [-------]
				""");
		
		String path = request.getRequestURL().toString();
		session.setAttribute("path", path);
		
		return "/user/home/home";
	}
	
	
	
	@PostMapping("/home/updateUser")
	public String updateUser(@ModelAttribute UserDto userDto, HttpServletRequest request){
		
		
		String token =null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		System.out.println(userDto.getFullName());
		userDto.setEmail(jwtHelper.extractUserName(token));
		UserDto dto = usrService.updateUser(userDto);
		
		return "redirect:/user/home";
	}
	
	

	@GetMapping("/home/deleteUser")
	public String deleteUser(HttpServletRequest req, HttpServletResponse respo){
		String token=null;
		for (Cookie cookie : req.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		
		usrService.deleteByEmail(token);
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setPath("/user");
		respo.addCookie(cookie);
		return "redirect:/user/login";
	}
	@PostMapping("/home/getUser")
	public String findUserByEmail(@RequestParam String email, Model m){
		UserDto dto = usrService.findByEmail(email);
		m.addAttribute("users", dto);
		return "user/home/getUser";
	}
	
	
	@GetMapping("/home/getAll")
	public String  findAllUser(Model m){
		List<UserDto> dtos = usrService.findAllUser();
		m.addAttribute("users", dtos);
		return "user/home/getUser";
	}
	
	
	
	@GetMapping("/book/remove")
	public String  removeBookFromUser(@RequestParam String bookName, Model m, HttpServletRequest req){
		String token=null;
		for (Cookie cookie : req.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		usrService.removeBookFromUser(bookName, token);
		
		return "redirect:/user/home";
	}
	
	
	
	@GetMapping("/home/setRole")
	public String setAdmin(HttpServletRequest req){
		
		String email = "praveenghost12@gmail.com";
		usrService.sendOtp(email, "Set Role");
		return "redirect:/user/home/otpVerification";
	}
	
	
	
	@PostMapping("/home/adminOtpVerification")
	public String adminOtpVerification(@RequestParam String otp, HttpServletRequest req){

		String token=null;
		for (Cookie cookie : req.getCookies()) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		usrService.changeRole(token, otp);
		
		return "redirect:/user/home";
	}
	
}
