package com.rishi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.model.AppUser;
import com.rishi.repo.AppUserRepo;

import jakarta.validation.constraints.Email;

@Service
public class SecurityService {

	@Autowired
	private AppUserRepo userRepo;
	
	public Optional<AppUser> getUserByEmail(@Email String email) {
		System.out.println("INSIDE SECIRTY SERVICE+ EMAIL"+email);
		Optional<AppUser>user= userRepo.findByEmail(email);
		System.out.println("OPTIONAL "+user);
		return user;
	}

}
