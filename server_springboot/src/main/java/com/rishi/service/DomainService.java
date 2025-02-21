package com.rishi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rishi.model.ARM;
import com.rishi.model.DRM;
import com.rishi.model.DomainRequest;
import com.rishi.model.Hod;
import com.rishi.model.ImportantDate;
import com.rishi.model.Status;
import com.rishi.repo.DomainRequestRepo;
import com.rishi.repo.DrmRepo;
import com.rishi.wrapperModels.DomainRequestWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Service
public class DomainService {

	@Autowired
	private DrmRepo drmRepo;
	@Autowired
	private DomainRequestRepo domainRepo;
	
	
	public ResponseEntity<?> createRequest(@Valid DomainRequestWrapper domainWrapper) {
		DRM drm=drmRepo.findById(domainWrapper.getDrmId()).orElseThrow(()->new EntityNotFoundException("NO DRM FOR WITH: "+domainWrapper.getDrmId()+" EXISTS."));
		
		ARM arm=drm.getSupervisingArm();
		Hod hod=arm.getSupervisor();
		
		if(arm==null||hod==null){
			System.err.println("NO ARM OR HOD FOR DRM:"+drm.toString());
			throw new DataRetrievalFailureException("NO ARM OR HOD EXISTS FOR THOS DRM: "+drm.toString());
		}
		
		DomainRequest domainRequest=new DomainRequest();
		Status status=new Status();
		ImportantDate impDate=new ImportantDate();
		
		impDate.setDrmRequestedDate(LocalDate.now());
		
		status.setActiveStatus(false);
		status.setHodStatus("NOT_VERIFIED");
		status.setArmStatus("NOT_VERIFIED");
		
		domainRequest.setArm(arm);
		domainRequest.setInitiatingDrm(drm);
		domainRequest.setHod(hod);
		
		domainRequest.setDomainName(domainWrapper.getDomainName());
		domainRequest.setStatus(status);
		domainRequest.setDates(impDate);
		
		try {
			return new ResponseEntity<>(domainRepo.save(domainRequest),HttpStatus.CREATED);
			
		} catch (Exception e) {
			System.err.println("ERROR OCCURED WHILE SAVING DOMAIN IN DB..");
			System.err.println(e);
			return new ResponseEntity<>("ERROR OCCURED WHILE SAVING DOMAIN IN DB.EXCEPTION: "+e.toString(),HttpStatus.BAD_REQUEST);
			
		}

	}


	public ResponseEntity<?> getDomainById(@Positive Integer domainId) {
		return ResponseEntity.ok(domainRepo.findById(domainId).orElseThrow(()->new EntityNotFoundException("THE DOMAIN WITH DOMAIN ID:"+domainId+" DOES NOT EXIST")));
	}


	public List<DomainRequest> getDomainsExpiring() {
		return domainRepo.findAllDomainsExpiringSoon();
	}

}
