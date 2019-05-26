package com.ackermans.criticalpath.email;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {
	
	private final Logger logger = LogManager.getFormatterLogger(EmailService.class);

	@Autowired
	private JavaMailSender javaMailSender;
	
	/*
	 * @Autowired private SystemSettingRepository systemSettingRepository;
	 */

	@Value("${spring.mail.fromName}")
	private String fromName;

	@Value("${spring.mail.from}")
	private String fromEmail;

	/**
	 * Send email
	 * 
	 * @param messagePreparator
	 */
	public void sendEmail(MimeMessage mimeMessage) {
		
		//SystemSetting systemSetting = systemSettingRepository.findByKey(SystemSettingEnum.EMAIL.ordinal());
		//If setting for EMAIL is not found or it's turned on then send email
		//if(systemSetting == null || systemSetting.getValue())
			javaMailSender.send(mimeMessage);
		//else
			logger.debug("Email is turned off through system settings. To send email please turn on.");
	}
	
	/**
	 * Send email with given details. System will set from Email & Name if it's configured at properties file.
	 * Note: You can configure from name & email using mail.from.name and mail.from.email properties key respectively.
	 * 
	 * @param subject
	 * @param htmlMailBody
	 * @param toEmail
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public void sendEmail(String subject, String htmlMailBody, String... toEmails) throws MessagingException, UnsupportedEncodingException {
		sendEmail(createMineMessage(fromName, fromEmail, subject, htmlMailBody, toEmails).getMimeMessage());
	}
	
	/**
	 * Send email with given details
	 * 
	 * @param subject
	 * @param htmlMailBody
	 * @param toEmail
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public void sendEmail(String fromName, String fromEmail, String subject, String htmlMailBody, String... toEmails) throws MessagingException, UnsupportedEncodingException {
		sendEmail(createMineMessage(fromName, fromEmail, subject, htmlMailBody, toEmails).getMimeMessage());
	}
	
	/**
	 * Create a mime message
	 * 
	 * @param fromName
	 * @param fromEmail
	 * @param subject
	 * @param htmlMailBody
	 * @param toEmails
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public MimeMessageHelper createMineMessage(String fromName, String fromEmail, String subject, String htmlMailBody, String... toEmails) throws UnsupportedEncodingException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		//If from email and name is set in properties file then use it
		if(StringUtils.hasText(fromName) && StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail, fromName);
		else if(StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail);
		
		helper.setFrom(fromEmail, fromName);
		helper.setTo(toEmails);
		helper.setSubject(subject);
		helper.setText(htmlMailBody, true);
		
		return helper;
	}
	
	/**
	 * @param subject
	 * @param htmlMailBody
	 * @param toEmails
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public MimeMessageHelper createMineMessage(String subject, String htmlMailBody, String... toEmails) throws UnsupportedEncodingException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		//If from email and name is set in properties file then use it
		if(StringUtils.hasText(fromName) && StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail, fromName);
		else if(StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail);
		
		helper.setFrom(fromEmail, fromName);
		helper.setTo(toEmails);
		helper.setSubject(subject);
		helper.setText(htmlMailBody, true);
		
		return helper;
	}
	
	public MimeMessageHelper createMineMessageForcc(String subject, String htmlMailBody, List toEmails) throws UnsupportedEncodingException, MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		//If from email and name is set in properties file then use it
		if(StringUtils.hasText(fromName) && StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail, fromName);
		else if(StringUtils.hasText(fromEmail))
			helper.setFrom(fromEmail);
		
		helper.setFrom(fromEmail, fromName);
		helper.setTo(toEmails.get(0).toString());
		helper.setCc(toEmails.get(1).toString());
		helper.setSubject(subject);
		helper.setText(htmlMailBody, true);
		
		return helper;
	}
}
