package com.ackermans.criticalpath.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

	private TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilder(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String build(String message, String name, String imageResourceName) {
		Context context = new Context();
		context.setVariable("message", message);
		context.setVariable("name", name);
		context.setVariable("imageResourceName", imageResourceName);
		return templateEngine.process("mailTemplate", context);
	}

	/**
	 * Set Parameter for forgot Password email template
	 * 
	 * @param name
	 * @param resetLink
	 * @return
	 */
	public String buildForgotPassword(String name, String resetLink) {
		Context context = new Context();
		context.setVariable("name", name);
		context.setVariable("resetLink", resetLink);
		return templateEngine.process("mailTemplate", context);
	}

	public String buildSendCriticalPathUpdate(String userName, String message, String message1) {
		Context context = new Context();
		System.out.println(message);
		context.setVariable("userName", userName);
		context.setVariable("message", message);
		context.setVariable("message1", message1);
		return templateEngine.process("mailTemplate", context);
	}
	
	public String buildSendApprovrDecStatus(String userName, String message) {
		Context context = new Context();
		System.out.println(message);
		context.setVariable("userName", userName);
		context.setVariable("message", message);
		return templateEngine.process("mailTemplate", context);
	}

}
