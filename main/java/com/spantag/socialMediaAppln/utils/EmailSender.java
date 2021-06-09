package com.spantag.socialMediaAppln.utils;

import java.io.File;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

public class EmailSender {

	private JavaMailSender mailSender;
	private SimpleMailMessage simpleMailMessage;
	
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendattaMail(String subject,String to, String content) {
	
	   MimeMessage message = mailSender.createMimeMessage();
		
	   try{
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
		helper.setFrom(simpleMailMessage.getFrom());
		helper.setTo(String.format(
				to, simpleMailMessage.getTo()));
		helper.setSubject(subject);
		helper.setText(String.format(
			simpleMailMessage.getText(), content),true);
			
		/*FileSystemResource file = new FileSystemResource("C:/Users/jbinngl1/Downloads/attached.pdf");
		helper.addAttachment(file.getFilename(), file);*/

	     }catch (MessagingException e) {
		throw new MailParseException(e);
	     }
	     mailSender.send(message);
         }
}