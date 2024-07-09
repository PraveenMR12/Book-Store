package org.dreamorbit.bookstore.util;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetObject {

	private String otp;
	@Size(min = 3, message = "Password must be min 3 letters")
	private String newPassword;
}
