package org.dreamorbit.bookstore.exception;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dreamorbit.bookstore.util.ApiResponse;
import org.dreamorbit.bookstore.util.BookDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class MainExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public String userNotFoundExceptionHandler(ResourceNotFoundException ue, HttpSession session, RedirectAttributes redirect){
		
		System.out.println("ResourceException");
		String path=null;
		try {
			path = session.getAttribute("path").toString();
		} catch (NullPointerException e) {
			return exceptionHandler("Session is Expired", redirect, session, "/user/home");
		}
		session.setAttribute("path", null);
		System.out.println(path);
		path = path.replace("http://localhost:9091", "");
		redirect.addAttribute("msg", ue.getMessage());
		System.out.println("userNot found");
		
		return "redirect:"+path;
	}
	
	@ExceptionHandler(value = InvalidCredentialsException.class)
	public String invalidCredentialsExceptionHandler(InvalidCredentialsException ie, Model model, RedirectAttributes redirect, HttpSession session, HttpServletRequest request){
	
		System.out.println(session.getMaxInactiveInterval());
		System.out.println("InvalidCredentialsException");
		String path=null;
		if(session.getAttribute("path")!=null) {
			try {
				path = session.getAttribute("path").toString();
			} catch (NullPointerException e) {
				return exceptionHandler("", redirect, session, "/user/login");
			}
		}else {
			path = request.getRequestURL().toString();
		}
		
		
		session.setAttribute("path", null);
		System.out.println(path);
		path = path.replace("http://localhost:9091", "");
		System.out.println(path);
		redirect.addAttribute("msg", ie.getMessage());
		
		return "redirect:"+path;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me, Model model, RedirectAttributes redirect, HttpSession session, HttpServletRequest request){
		System.out.println("MethodArgumentException");
		Map<String, String> map = new HashMap<>();
		me.getBindingResult().getAllErrors().forEach(error-> {
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			map.put(fieldName, message);
		});
		String path=null;
		try {
			if(session.getAttribute("path")!=null) {
					path = session.getAttribute("path").toString();
				
			}else {
				System.out.println("hii");
				path = request.getRequestURL().toString();
				System.out.println(path);
			}
		} catch(SessionException e) {
			return exceptionHandler("", redirect, session, "/user/login");
		}
		session.setAttribute("path", null);
		System.out.println(path);
		path = path.replace("http://localhost:9091", "");
		session.setAttribute("error", map);
		return "redirect:"+path;
	}
	
	public String exceptionHandler(String msg, RedirectAttributes redirect, HttpSession session, String path){
		System.out.println("session");
		
		
		redirect.addAttribute("msg", msg);
		return "redirect:"+path;
	}
	
	@ExceptionHandler(value = Exception.class)
	public String defaultExceptionHandler(Exception e, Model model, RedirectAttributes redirect, HttpSession session, HttpServletResponse response, HttpServletRequest request){
		
		System.out.println("Exception"+e);

		String path=null;
		try {
			path = session.getAttribute("path").toString();
			path = path.replace("http://localhost:9091", "");
		} catch (Exception ee) {
			
		}		
		session.setAttribute("path", null);
		System.out.println(path);
		

			
		System.out.println(path);
		
		String msg ="Something went wrong"+ e.getMessage();
		redirect.addAttribute("msg",msg);
		return "redirect:"+path;
	}
}
