package org.dreamorbit.bookstore.repository;

import java.util.Optional;

import org.dreamorbit.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer>{
	
	Optional<Book> findByBookName(String name);

}
