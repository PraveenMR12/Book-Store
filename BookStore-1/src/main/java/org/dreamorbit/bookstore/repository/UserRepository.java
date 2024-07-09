package org.dreamorbit.bookstore.repository;

import java.util.Optional;

import org.dreamorbit.bookstore.entity.Role;
import org.dreamorbit.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	User findByRole(Role admin);

}
