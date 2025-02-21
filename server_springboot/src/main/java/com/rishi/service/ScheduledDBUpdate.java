package com.rishi.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rishi.model.DomainRequest;
import com.rishi.repo.DomainRequestRepo;

import jakarta.mail.MessagingException;
@Service
public class ScheduledDBUpdate {
	private DomainRequestRepo domainRepo;
	
	public ScheduledDBUpdate(DomainRequestRepo domainRepo) {
		this.domainRepo=domainRepo;
	}
//	
//	@Scheduled(cron="0 31 0 * * ?")
//	public void sendNotificationEmail() throws MessagingException {
//		List<DomainRequest> expiredDomains=domainRepo.findActiveRequests();
//		
//		for(DomainRequest domain:expiredDomains) {
//			emailService.sendExpiryNotifications(domain);
//		}
//	}


}
