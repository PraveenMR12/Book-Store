package org.dreamorbit.bookstore.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookId;
	private String bookName;
	private String author;
	private double price;
	private double rating;
	@ManyToMany(mappedBy = "books")@JsonIgnore
	private List<Cart> carts;
	@ManyToMany(mappedBy = "books")@JsonIgnore
	private List<Wishlist> wishlists;
	@ManyToMany(mappedBy = "books")@JsonIgnore
	private List<User> users;
	
	
	@PreRemove
	public void remove() {
		users.forEach(user->user.getBooks().removeIf(e->e.equals(this)));
		carts.forEach(cart->cart.getBooks().removeIf(e->e.equals(this)));
		wishlists.forEach(wishlist->wishlist.getBooks().removeIf(e->e.equals(this)));
	}
}
