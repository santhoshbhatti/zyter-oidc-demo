package com.infinite.zyteroidcdemo.websocket.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


public class JWSAuthenticationToken extends 
AbstractAuthenticationToken implements Authentication{
	private static final long serialVersionUID = 1L;

	  private String token;
	  private User principal;

	  public JWSAuthenticationToken(String token) {
	    this(token, null, null);
	  }

	  public JWSAuthenticationToken(String token, User principal, Collection<GrantedAuthority> authorities) {
	    super(authorities);
	    this.token = token;
	    this.principal = principal;
	  }

	  @Override
	  public Object getCredentials() {
	    return token;
	  }

	  @Override
	  public Object getPrincipal() {
	    return principal;
	  }
}
