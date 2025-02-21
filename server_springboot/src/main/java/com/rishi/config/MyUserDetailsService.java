package com.rishi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rishi.model.AppUser;
import com.rishi.model.UserPrincipal;
import com.rishi.service.SecurityService;
@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private SecurityService securityService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		System.out.println("INSIDE USER DETAILS SERVICE");
		AppUser user=securityService.getUserByEmail(username).orElseThrow(()->new UsernameNotFoundException("USER WITH EMAIL "+username+" NOT FOUND"));
//		System.out.println("ALAKJFNA"+user);
		return new UserPrincipal(user);
	}
	

}
