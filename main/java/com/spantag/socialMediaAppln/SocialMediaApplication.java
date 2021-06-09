package com.spantag.socialMediaAppln;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@ServletComponentScan
@SpringBootApplication
public class SocialMediaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApplication.class, args);
		System.out.println("Social Media Application Started");
		
		
	 
	}
	
	 
}
