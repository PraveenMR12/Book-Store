package org.dreamorbit.bookstore.repository;

import org.dreamorbit.bookstore.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer>{

}
