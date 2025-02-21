package com.rishi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rishi.model.DomainRequest;
import com.rishi.repo.ArmRepo;
import com.rishi.repo.DomainRequestRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Positive;

@Service
public class ArmService {

	@Autowired
	private ArmRepo armRepo;
	
	@Autowired
	private DomainRequestRepo domainRepo;
	
	
	/*
	 * @Transactional // Ensure the transaction remains open for fetching
	 * lazy-loaded collections
	 */	public ResponseEntity<?> getAllDomainsRequests(Integer armId) {
//	    logger.info("Fetching domain requests for ARM ID: {}", armId);

//	    ARM arm = armRepo.findById(armId)
//	            .orElseThrow(() -> new EntityNotFoundException("ARM WITH ID :" + armId + " DOES NOT EXIST"));
//		ARM arm=armRepo.findById(armId).orElse(null);
	    // Force Hibernate to initialize the collection before using it
	    //Set<DomainRequest> dnsRequests = new HashSet<>(arm.getDnsRequests());
//	    System.out.println(arm);
	    List<DomainRequest> responseList = new ArrayList<>(armRepo.findRequestedDomainsByArmId(armId).orElseThrow(()->new EntityNotFoundException("THE ARM WITH ARM ID:"+armId+" DOES NOT EXIST")));
	    
	    for(DomainRequest domainRequest:responseList) {
			if(domainRequest.getStatus().isActiveStatus()&&domainRequest.getDates().getDateOfExpiry()!=null)
				domainRequest.setDaysLeftTillExpiry(ChronoUnit.DAYS.between(LocalDate.now(),domainRequest.getDates().getDateOfExpiry()));
		}

	    return ResponseEntity.ok(responseList);
	}

	public ResponseEntity<?> verifyRequest(@Positive Integer requestId) {
	
		DomainRequest domainRequest=domainRepo.findById(requestId).orElseThrow(()->new EntityNotFoundException("THE DOMAIN WITH DOMAIN ID :"+requestId+" DOES NOT EXIST"));
		
		domainRequest.getDates().setArmReviewedDate(LocalDate.now());
		domainRequest.getStatus().setArmStatus("VERIFIED");
		try {
			return new ResponseEntity<>(domainRepo.save(domainRequest),HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			System.err.println("ERROR OCCURED WHILE VERIFYING ARM STATUS");
			System.err.println(e);
			return new ResponseEntity<>("ERROR OCCURED WHILE VERIFYING ARM STATUS.EXCEPTION:"+e.toString(),HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> getArmById(Integer armId) {
		return ResponseEntity.ok(armRepo.findById(armId));
	}

	public ResponseEntity<?> returnDomainRequest(@Positive Integer requestId) {
DomainRequest domainRequest=domainRepo.findById(requestId).orElseThrow(()->new EntityNotFoundException("THE DOMAIN WITH DOMAIN ID :"+requestId+" DOES NOT EXIST"));
		
		domainRequest.getDates().setArmReviewedDate(LocalDate.now());
		domainRequest.getStatus().setArmStatus("SENT BACK TO DRM FOR REVIEW");
		try {
			return new ResponseEntity<>(domainRepo.save(domainRequest),HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			System.err.println("ERROR OCCURED WHILE UPDATING ARM STATUS");
			System.err.println(e);
			return new ResponseEntity<>("ERROR OCCURED WHILE UPDATING ARM STATUS.EXCEPTION:"+e.toString(),HttpStatus.BAD_REQUEST);
		}
	}

}
