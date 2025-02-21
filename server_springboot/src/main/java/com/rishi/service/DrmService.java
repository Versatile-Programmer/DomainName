package com.rishi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rishi.model.AppUser;
import com.rishi.model.DRM;
import com.rishi.model.DomainRequest;
import com.rishi.model.Role;
import com.rishi.repo.AppUserRepo;
import com.rishi.repo.DrmRepo;
import com.rishi.wrapperModels.DrmWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
@Service
public class DrmService {

	@Autowired
	private DrmRepo drmRepo;
	@Autowired
	private AppUserRepo appUserRepo;
	
	public ResponseEntity<?> register(@Valid DrmWrapper drmWrapper) {
		AppUser appUser=new AppUser();
		DRM drm=new DRM();
		Role role=new Role();
		
		role.setId(1);
		role.setRole_name("ROLE_DRM");
		
		appUser.setEmail(drmWrapper.getDrmEmail());
		appUser.setPassword(drmWrapper.getDrmPassword());
		appUser.setRole(role);
		
		drm.setName(drmWrapper.getDrmName());
		drm.setEmail(drmWrapper.getDrmEmail());
		drm.setDept(drmWrapper.getDrmDept());
		
		try {
			
			
			
			drmRepo.save(drm);
			
			return new ResponseEntity<>("SUCCESSFULLY ENTRY CREATED."+appUserRepo.save(appUser),HttpStatus.CREATED);
			
		} catch (Exception e) {
			System.err.println("ERROR OCCURED WHILE SAVING DRM..");
			System.err.println(e);
			
			return new ResponseEntity<>("ERROR OCCURED: "+e.toString(),HttpStatus.BAD_REQUEST);
			
		}
		
	}

	public ResponseEntity<?> getAllRequestedDomains(@Positive Integer drmId) {
		List<DomainRequest> domainList=drmRepo.findAllRequestedDomainsByDrmId(drmId);
//		drmRepo.findById(drmId).orElseThrow(()->new EntityNotFoundException("DRM WITH DRM_ID: "+drmId+" EXISTS."))
		for(DomainRequest domainRequest:domainList) {
			if(domainRequest.getStatus().isActiveStatus()&&domainRequest.getDates().getDateOfExpiry()!=null) {
				long daysLeft=ChronoUnit.DAYS.between(LocalDate.now(),domainRequest.getDates().getDateOfExpiry());
				
				domainRequest.setDaysLeftTillExpiry(Math.max(0, daysLeft));
			}
		}
		return ResponseEntity.ok(domainList);
	}

	public ResponseEntity<?> getDrmByEmail(@Email String email) {
		
		DRM drm=drmRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("USER WITH EMAIL:"+email+"NOT FOUND"));
		
		return new ResponseEntity<>(drm,HttpStatus.OK);
	}

}
