package org.dreamorbit.bookstore.repository;

import org.dreamorbit.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer>{
	
}
