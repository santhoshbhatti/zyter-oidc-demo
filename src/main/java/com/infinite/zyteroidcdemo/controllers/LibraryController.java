package com.infinite.zyteroidcdemo.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.infinite.zyteroidcdemo.repo.BookRepository;

@Controller
public class LibraryController {
	private final HttpServletRequest request;
	private final BookRepository bookRepository;

	@Autowired
	public LibraryController(HttpServletRequest request, BookRepository bookRepository) {
		this.request = request;
		this.bookRepository = bookRepository;
	}

	@GetMapping(value = "/")
	public String getHome() {
		return "index";
	}

	@GetMapping(value = "/books")
	public String getBooks(Model model) {
		var securityContext = (KeycloakPrincipal<KeycloakSecurityContext>)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		var auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		System.out.println(auths);
		configCommonAttributes(model);
		model.addAttribute("books", bookRepository.readAll());
		return "books";
	}

	@GetMapping(value = "/manager")
	public String getManager(Model model) {
		configCommonAttributes(model);
		model.addAttribute("books", bookRepository.readAll());
		return "manager";
	}
	
	@GetMapping(value = "/chat")
	public String getChat(Model model) {
		
		return "chat";
	}

	@GetMapping(value = "/logout")
	public String logout() throws ServletException {
		request.logout();
		return "redirect:/";
	}

	private void configCommonAttributes(Model model) {
		model.addAttribute("name", getKeycloakSecurityContext().getIdToken().getGivenName());
	}

	private KeycloakSecurityContext getKeycloakSecurityContext() {
		return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	}
}