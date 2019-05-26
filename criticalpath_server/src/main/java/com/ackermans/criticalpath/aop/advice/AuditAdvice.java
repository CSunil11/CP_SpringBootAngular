package com.ackermans.criticalpath.aop.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ackermans.criticalpath.aop.service.AuditService;

@Aspect
@Component
public class AuditAdvice {

	@Autowired
	private AuditService auditService;

	private final Logger logger = LogManager.getFormatterLogger(AuditAdvice.class);

	private Object proceedJoinPointAndSaveAuditMessage(ProceedingJoinPoint joinPoint, String auditMessage)
			throws Throwable {

		Object val = null;
		val = joinPoint.proceed();
		if (auditMessage != null)
			auditService.saveAuditMessage(auditMessage);
		return val;
	}

	@Around("execution(* com.ackermans..*Save(..)) || execution(* com.ackermans..*service*..delete*(..)) && !execution(* com.ackermans..*UserLoginService*.delete*(..)) || execution(* com.ackermans..*service*..restore*(..)) && !execution(* com.ackermans..*UserLoginService*.restore*(..))")
	public Object saveUpdateDelRestoreAudit(ProceedingJoinPoint joinPoint) throws Throwable {

		String auditMessage = auditLoggerWithAsync(joinPoint);

		return proceedJoinPointAndSaveAuditMessage(joinPoint, auditMessage);
	}

	@Async
	private String auditLoggerWithAsync(ProceedingJoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String requestUrl = request.getRequestURI();
		String auditMessage = null;
		if (joinPoint != null)
			joinPoint.getArgs();
		try {

			auditMessage = auditService.generateAuditMessage(requestUrl);

		} catch (ClassCastException ex) {
			logger.debug("Exception while casting to BaseEntity ", ex);
		}
		return auditMessage;
	}

}
