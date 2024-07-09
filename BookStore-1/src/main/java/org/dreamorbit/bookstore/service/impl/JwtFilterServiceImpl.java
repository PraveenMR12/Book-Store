package org.dreamorbit.bookstore.service.impl;

import org.dreamorbit.bookstore.repository.UserRepository;
import org.dreamorbit.bookstore.service.JwtFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtFilterServiceImpl implements JwtFilterService{
	
	@Autowired
	private UserRepository repo;
	
	@Override
	public UserDetailsService userDetailsService() {
		
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return repo.findByEmail(username)
						.orElseThrow(()->new RuntimeException("User not found"));
			}
		};
	}

}
