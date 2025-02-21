package com.rishi.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rishi.model.DomainRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendExpiryNotifications(DomainRequest domain) throws MessagingException {
        if (domain == null || domain.getDates() == null || domain.getDates().getDateOfExpiry() == null) {
            logger.warn("Domain expiry date is missing for domain: {}", (domain != null ? domain.getDomainName() : "Unknown"));
            return;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String drmEmail = domain.getInitiatingDrm().getEmail();
        String armEmail = domain.getArm().getArmEmail();
        String hodEmail = domain.getHod().getHodEmail();
        String domainName = domain.getDomainName();
        LocalDate dateOfExpiry = domain.getDates().getDateOfExpiry();
        long daysLeft = Math.max(ChronoUnit.DAYS.between(LocalDate.now(), dateOfExpiry), 0);

        helper.setFrom("no-reply@yourdomain.com");
        helper.setTo(drmEmail);

        // Properly set CC recipients
        if (daysLeft <= 15) 
            helper.setCc(new String[]{armEmail});
        if (daysLeft <= 7)
            helper.setCc(new String[]{armEmail, hodEmail});
        

        helper.setSubject("DOMAIN EXPIRY NOTICE");
        String emailContent = "<html><body>"
                + "<h2 style='color: red;'>Domain Expiry Alert</h2>"
                + "<p>Dear User,</p>"
                + "<p>Your domain <strong>" + domainName + "</strong> is set to expire in <strong>" + daysLeft + " days (" + dateOfExpiry + ")</strong>.</p>"
                + "<p>Please renew it as soon as possible to avoid any disruptions.</p>"
                + "<p>Best Regards,<br/>DNS Management Team</p>"
                + "</body></html>";

        helper.setText(emailContent, true); // Enable HTML

        javaMailSender.send(message);

        logger.info("Email sent to: {}, CC: {}", drmEmail, (daysLeft <= 15 ? armEmail + ", " + hodEmail : daysLeft <= 30 ? armEmail : "None"));
    }
}
