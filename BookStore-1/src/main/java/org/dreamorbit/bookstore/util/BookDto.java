package org.dreamorbit.bookstore.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BookDto {
	@NotEmpty
	@Size(min = 3, message = "size should be greater than 4 characters")
	private String bookName;
	
	@NotEmpty
	@Size(min = 3, message = "size should be greater than 4 characters")
	private String author;
	@NotEmpty
	private String price;
	@NotEmpty
	private String rating;
}
