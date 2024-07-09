package org.dreamorbit.bookstore.util;

import org.dreamorbit.bookstore.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserDto {
	@NotEmpty(message = "Name should be min 3 letters")
	@Size(min = 3, message = "Name should be min 3 letters")
	private String fullName;
	@Email(message = "enter valid email")
	@NotEmpty(message = "enter valid email")
	private String email;
	@NotEmpty(message = "Password should be min 3 letters")
	@Size(min = 3, message = "Password should be min 3 letters")
	private String password;
	@NotEmpty(message = "Phone Number should be min 3 letters")
	private String phoneNumber;
	private Role role;
	
}
