package com.ackermans.criticalpath.email;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	
	private Logger logger = LogManager.getFormatterLogger(getClass());

	@Autowired
	private EmailService emailService;

	/**
	 * @param from
	 * @param fromName
	 * @param to
	 * @param subject
	 * @param htmlContent
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void send(String to, String subject, String htmlContent) throws MessagingException, IOException {

		this.send(to, subject, htmlContent, null, null, null);
	}

	/**
	 * @param from
	 * @param fromName
	 * @param to
	 * @param subject
	 * @param htmlContent
	 * @param pathOfAttachment
	 * @param nameOfAttachment
	 * @param file
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void send(String to, String subject, String htmlContent, String pathOfAttachment, String nameOfAttachment,
			File file) throws MessagingException, IOException {

		try {

			MimeMessageHelper helper = emailService.createMineMessage(subject, htmlContent, to);
			
			// Any attachment related to subject
			if (pathOfAttachment != null && pathOfAttachment != "") {
				FileSystemResource fileOfAttachement = new FileSystemResource(new File(pathOfAttachment));
				helper.addAttachment(nameOfAttachment, fileOfAttachement);
			}
			
			// Attach the logo of company
			if (file != null) {
				final InputStreamSource imageSource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
				helper.addInline(file.getName(), imageSource, Files.probeContentType(file.toPath()));
			}
			emailService.sendEmail(helper.getMimeMessage());

		} catch (Exception e) {
			logger.error("Error while sending email :: ", e);
		}
	}
	
	public void sendApproveDecStatus(List to, String subject, String htmlContent, String pathOfAttachment, String nameOfAttachment,
			File file) throws MessagingException, IOException {

		try {

			MimeMessageHelper helper = emailService.createMineMessageForcc(subject, htmlContent, to);
			
			// Any attachment related to subject
			if (pathOfAttachment != null && pathOfAttachment != "") {
				FileSystemResource fileOfAttachement = new FileSystemResource(new File(pathOfAttachment));
				helper.addAttachment(nameOfAttachment, fileOfAttachement);
			}
			
			// Attach the logo of company
			if (file != null) {
				final InputStreamSource imageSource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
				helper.addInline(file.getName(), imageSource, Files.probeContentType(file.toPath()));
			}
			emailService.sendEmail(helper.getMimeMessage());

		} catch (Exception e) {
			logger.error("Error while sending email :: ", e);
		}
	}
}
