package com.rishi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.service.DrmService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/drm")
@CrossOrigin
public class DrmController {

	@Autowired
	private DrmService drmService;
	
	@GetMapping("requestedDomains/{drmId}")
	public ResponseEntity<?> getAllRequestsByDrmId(@Positive @PathVariable Integer drmId){
		return drmService.getAllRequestedDomains(drmId);
	}
	@PostMapping("getDrm")
	public ResponseEntity<?> getDrmByEmail(@RequestBody Map<String,String> mappings){
		
		  String email = mappings.get("email");
		    
		    if (email == null || email.isBlank()) {
		        return ResponseEntity.badRequest().body("Email is required");
		    }
		
		return drmService.getDrmByEmail(email);
	}
}
