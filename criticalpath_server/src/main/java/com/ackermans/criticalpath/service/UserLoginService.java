package com.ackermans.criticalpath.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.mail.MessagingException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ackermans.criticalpath.common.dto.CPException;
import com.ackermans.criticalpath.controller.StockTakeCycleController;
import com.ackermans.criticalpath.email.EmailSenderService;
import com.ackermans.criticalpath.email.MailContentBuilder;
import com.ackermans.criticalpath.entity.UserLogin;
import com.ackermans.criticalpath.enums.ErrorStatus;
import com.ackermans.criticalpath.repository.UserLoginRepository;
import com.ackermans.criticalpath.utils.DateUtils;

@Service
public class UserLoginService {
	private static final Logger logger = LogManager.getFormatterLogger(StockTakeCycleController.class);
	
	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${frontend.baseUrl}")
	private String frontendBaseURL;

	@Autowired
	private MailContentBuilder mailcontentbuilder;

	@Autowired
	private EmailSenderService emailsenderservice;

//	@Autowired
//	private UserService userservices;

	private static final long LINK_EXPIER_MINS = 30;

	private static final String MAIL_FORGOT_PASSWORD_SUBJECT = "Critical Path - Reset Password Request";

	/**
	 * Find user by email (ignore case here)
	 * 
	 * @param email
	 * @return
	 */
	public UserLogin findByEmail(String email) {
		return userLoginRepository.findByEmailIgnoreCase(email);
	}

	/**
	 * Deletes the record
	 * 
	 * @param userLoginId
	 * @return
	 * @throws CPException
	 */
	public void deleteUser(Long userLoginId) throws CPException {

		UserLogin userLogin = userLoginRepository.getOne(userLoginId);

		userLogin.setIsActive(false);
		userLogin.setIsDelete(true);
		userLoginRepository.save(userLogin);
	}

	/**
	 * Restores the record
	 * 
	 * @param userLoginId
	 */
	public void restoreUser(Long userLoginId) {

		UserLogin userLogin = userLoginRepository.getOne(userLoginId);
		userLogin.setIsActive(true);
		userLogin.setIsDelete(false);
		userLoginRepository.save(userLogin);
	}

	/**
	 * Validate current password for given user id
	 * 
	 * @param userId
	 * @param currentPassword
	 * @return
	 * @throws CPException
	 */
	public boolean isCurrentPasswordValid(Long userId, String currentPassword) throws CPException {

		UserLogin userLogin = userLoginRepository.getOne(userId);

		String getpassword = userLogin.getPassword();

		boolean checkpassword = passwordEncoder.matches(currentPassword, getpassword);

		// Check if checkpassword false
		if (!checkpassword) {
			throw new CPException(ErrorStatus.INVALID_CURRENT_PASSWORD);
		}

		return checkpassword;
	}

	/**
	 * update UserLogin password using userId
	 * 
	 * @param userId
	 * @param newpassword
	 * @return
	 * @throws CPException
	 */
	public boolean updatePassword(Long userId, String newpassword) throws CPException {
		UserLogin userLogin = userLoginRepository.getOne(userId);

		userLogin.setPassword(passwordEncoder.encode(newpassword));
		userLoginRepository.save(userLogin);
		return true;
	}

	/**
	 * Forgot password
	 * 
	 * @param email
	 * @return
	 * @throws CPException
	 * @throws UnsupportedEncodingException
	 */
	public boolean forgotPassword(String email) throws CPException {

//		UserLogin userLogin = userLoginRepository.findByEmailIgnoreCase(email);
//
//		if (userLogin != null) {
//			try {
//
//				String name = userservices.getUserByUserLoginId(userLogin.getId()).getName();
//				String param = URLEncoder.encode(new Date().getTime() + "&" + email, "UTF8");
//				String resetLink = frontendBaseURL + "/resetpassword/" + param;
//				final String htmlContent = mailcontentbuilder.buildForgotPassword(name, resetLink);
//
//				emailsenderservice.send(email, MAIL_FORGOT_PASSWORD_SUBJECT, htmlContent);
//				return true;
//			} catch (IOException | MessagingException e) {
//				throw new CPException(ErrorStatus.EMAIL_SEND_FAILED, email);
//			}
//		} else {
//			throw new CPException(ErrorStatus.EMAIL_NOT_FOUND, email);
//		}
		return false;
	}

	/**
	 * Check resettoken is valid
	 * 
	 * @param resettoken
	 * @return
	 * @throws CPException
	 */
	public boolean isValidResetToken(String resetToken) throws CPException {
		
		String[] resetTokens = resetToken.split("&");
		UserLogin userLogin = userLoginRepository.findByEmailIgnoreCase(resetTokens[1]);
		
		// check userlogin is not null
		if (userLogin != null) {
			
			// difference between currenttime and resettoken time
			long min = DateUtils.getDiffInMin(Long.parseLong(resetTokens[0]));
			
			// check minute less then 30
			if (min < LINK_EXPIER_MINS)
				return true;
			else
				throw new CPException(ErrorStatus.LINK_EXPIRED);
		} else {
			throw new CPException(ErrorStatus.INVALID_RESET_LINK);
		}
	}

	/**
	 * update password for given email
	 * 
	 * @param email
	 * @param newpwd
	 * @return
	 * @throws CPException
	 */
	public boolean updatePassword(String email, String newpwd) throws CPException {
		
		UserLogin userLogin = userLoginRepository.findByEmailIgnoreCase(email);
		
		//check if user is not null
		if (userLogin != null) {
			userLogin.setPassword(passwordEncoder.encode(newpwd));
			userLoginRepository.save(userLogin);
			return true;
		} else {
			throw new CPException(ErrorStatus.EMAIL_NOT_FOUND , email);
		}
	}

	
}
