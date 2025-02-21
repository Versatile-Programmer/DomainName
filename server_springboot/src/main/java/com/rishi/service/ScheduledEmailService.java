package com.rishi.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rishi.model.DomainRequest;

import jakarta.mail.MessagingException;
@Service
public class ScheduledEmailService {
	private final EmailService emailService;
	private final DomainService domainService;
	
	public ScheduledEmailService(EmailService emailService,DomainService domainService) {
		this.emailService=emailService;
		this.domainService=domainService;
	}
	
	@Scheduled(cron="0 31 0 * * ?")
	public void sendNotificationEmail() throws MessagingException {
		
		List<DomainRequest> expiredDomains=domainService.getDomainsExpiring();
		
		for(DomainRequest domain:expiredDomains) {
			emailService.sendExpiryNotifications(domain);
		}
	}

}
