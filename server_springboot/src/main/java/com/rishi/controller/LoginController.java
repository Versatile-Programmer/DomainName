package com.rishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.dto.LoginDTO;
import com.rishi.model.AppUser;
import com.rishi.repo.AppUserRepo;
import com.rishi.repo.ArmRepo;
import com.rishi.repo.DrmRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired 
    private DrmRepo drmRepo;
    @Autowired
    private ArmRepo armRepo;

    // Define a map for role-based handlers
    private final Map<String, Function<String, ResponseEntity<?>>> roleHandlers = new HashMap<>();

    // Constructor for dependency injection and initializing the map
	public LoginController(/* AppUserRepo appUserRepo, DrmRepo drmRepo, ArmRepo armRepo */) {
//        this.appUserRepo = appUserRepo;
//        this.drmRepo = drmRepo;
//        this.armRepo = armRepo;

        // Map role to the corresponding database query
        roleHandlers.put("ROLE_DRM", email -> new ResponseEntity<>(new LoginDTO(  drmRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("THE USER WITH EMAIL: " + email + " DOES NOT EXIST"))), HttpStatus.OK));
        roleHandlers.put("ROLE_ARM", email -> new ResponseEntity<>(new LoginDTO(  armRepo.findByArmEmail(email).orElseThrow(() -> new UsernameNotFoundException("THE USER WITH EMAIL: " + email+ " DOES NOT EXIST"))), HttpStatus.OK));
    }

    @PostMapping("loginUser")
    public ResponseEntity<?> loginUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("User details not found", HttpStatus.UNAUTHORIZED);
        }

        System.out.println("USERNAME: " + userDetails.getUsername() + " PASSWORD: " + userDetails.getPassword());

        AppUser appUser = appUserRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("THE USER WITH EMAIL: " + userDetails.getUsername() + " DOES NOT EXIST"));

        String email = userDetails.getUsername();
        String role = appUser.getRole().getRole_name();

        // Check if the role is mapped, otherwise return a forbidden response
        return roleHandlers.getOrDefault(role, e -> new ResponseEntity<>("Invalid role", HttpStatus.FORBIDDEN))
                .apply(email);
    }
}
