package com.rishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.service.DomainService;
import com.rishi.wrapperModels.DomainRequestWrapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/domain")
@CrossOrigin
public class DomainRequestController {

	@Autowired
	private DomainService domainService;
	
	@PostMapping("requestDomain")
	public ResponseEntity<?> createRequest(@Valid @RequestBody DomainRequestWrapper domainWrapper){
		return domainService.createRequest(domainWrapper);
	}
	
	@GetMapping("domain/{domainId}")
	public ResponseEntity<?> getDomainRequest(@Positive @PathVariable Integer domainId){
		return domainService.getDomainById(domainId);
	}
	
	
}
