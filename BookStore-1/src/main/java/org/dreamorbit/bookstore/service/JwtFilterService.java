package org.dreamorbit.bookstore.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtFilterService {
	UserDetailsService userDetailsService();

}
