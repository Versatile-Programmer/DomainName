package com.rishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.service.DrmService;
import com.rishi.wrapperModels.DrmWrapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "*")
public class DrmRegistrationController {

	@Autowired
	private DrmService drmService;
	
	@PostMapping("registerDrm")
	public ResponseEntity<?> registerDrm(@Valid @RequestBody DrmWrapper drmWrapper){
		return drmService.register(drmWrapper);
	}
}
