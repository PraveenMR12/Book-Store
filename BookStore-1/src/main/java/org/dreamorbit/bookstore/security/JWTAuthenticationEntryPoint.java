package org.dreamorbit.bookstore.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.dreamorbit.bookstore.controller.NavController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	NavController nav;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			
			System.out.println("validity");
			response.sendRedirect("/user/error");

	}
	
}
