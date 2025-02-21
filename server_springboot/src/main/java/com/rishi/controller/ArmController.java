package com.rishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.service.ArmService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/arm")
@CrossOrigin
public class ArmController {

	@Autowired
	private ArmService armService;
	
	@GetMapping("getDomainRequests/{armId}")
	public ResponseEntity<?> getForwardedRequests(@PathVariable Integer armId){
		return armService.getAllDomainsRequests(armId);
	}
	
	@GetMapping("getArm/{armId}")
	public ResponseEntity<?> getArmById(@PathVariable Integer armId){
		return armService.getArmById(armId);
	}
	
	
	@PostMapping("verifyDomainRequest/{requestId}")
	public ResponseEntity<?> verifyDomainRequest(@Positive @PathVariable Integer requestId){
		return armService.verifyRequest(requestId);
	}
	
	@PostMapping("sendForReview/{requestId}")
	public ResponseEntity<?> sendRequestBack(@Positive @PathVariable Integer requestId){
		return armService.returnDomainRequest(requestId);
	}
	
	
	
}
