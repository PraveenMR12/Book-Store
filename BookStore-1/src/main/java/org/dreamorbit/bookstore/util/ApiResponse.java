package org.dreamorbit.bookstore.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
	private String msg;
	private boolean success; 

}
