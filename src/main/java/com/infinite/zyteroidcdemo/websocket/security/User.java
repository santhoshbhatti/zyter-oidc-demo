package com.infinite.zyteroidcdemo.websocket.security;

import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

	private String name;
	private String email;
	private String preferredUsername;
	private Set<String> roles;

}
