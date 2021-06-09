package com.spantag.socialMediaAppln.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spantag.socialMediaAppln.securityConfig.JwtTokenUtil;
import com.spantag.socialMediaAppln.uploadMedia.FileUploadRepository;
import com.spantag.socialMediaAppln.uploadMedia.SMFile;
import com.spantag.socialMediaAppln.uploadMedia.SMFileDTO;
import com.spantag.socialMediaAppln.utils.ConnUtil;
import com.spantag.socialMediaAppln.utils.EmailSender;
import com.spantag.socialMediaAppln.utils.QuoteDBProcedureHelper;
import com.spantag.socialMediaAppln.utils.commonUtils;

import java.security.SecureRandom;

@Service(value = "userService")
@Configuration
@PropertySource("classpath:SocialMediaApplnQueryList.properties")
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final Logger log = LoggerFactory
			.getLogger(UserServiceImpl.class);

	ObjectMapper obj = new ObjectMapper();
	@Autowired
	QuoteDBProcedureHelper quoteDBProcedureHelper;
	@Autowired
	private UserRegisterRepository userRepo;
	
	@Autowired
	private FileUploadRepository fileRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private EntityManager em;

	@Autowired
	private Environment env;

	@Autowired
	private ConnUtil connutil;

	/*
	 * @Autowired private FileStorageProperties fileStorageProperties;
	 */
	@Autowired
	ConnUtil conn;

	@Autowired
	private MessageSource messageSource;

	@Value("${spring.config.sms.apikey}")
	private String apikey;
	
	@Value("${spring.config.sms.username}")
	private String smsUserName;
	
	@Value("${spring.config.sms.password}")
	private String smsPassword;
	
	@Value("${spring.config.sms.enable}")
	private String smsEnable;
	
	@Override
	public Map<String, Object> signIn(@Valid SignUpDTO userInput) throws JsonProcessingException {

		
		Map<String, Object> out = new LinkedHashMap<>();
		
		Optional<User> oUser = userRepo.findByEmail(userInput.getEmail());
		
		if (null != userInput.getEmail()) {
			if(!oUser.isPresent()) {
				throw new UsernameNotFoundException("invalidUser",null);
			} 
		else {
			if(null != userInput.getPassword() && null != userInput.getEmail()) {
				String encryptpassword=commonUtils.encrypt("SocialMediaappln", "nlppaaideMlaicoS", userInput.password);
				System.out.println("encrypted password "+encryptpassword);
				Optional<User> user1 = userRepo.findByPasswordAndEmail(encryptpassword,userInput.getEmail() );
				if(!user1.isPresent()) {
					throw new UsernameNotFoundException("user not found for given email and password !",null);
				}
				else {
					User userexist = user1.get();
					if(userexist.getUserstatus().equals("1")){
						System.out.println("welocome to token==="+userInput.getApkToken());
						
						userexist.setApkToken(userInput.getApkToken());
						userRepo.save(userexist);
						String token = jwtTokenUtil.generateToken(userexist);
					   String name = userexist.getUsername();
						out.put("userid", userexist.getUserid());
						out.put("mail", userexist.getEmail());
						out.put("name", name);
						out.put("token", token);
						out.put("message", "User logged in successfully !");
					}else{
						throw new UsernameNotFoundException("user not active",null);
					}
					
				}
			}
		}
		}
		return out;
	}

	
	@Override
	public Map<String, Object> signUp(@Valid SignUpDTO userInput) throws JsonProcessingException {

		
		Map<String, Object> out = new LinkedHashMap<>();
		System.out.println("user email=="+userInput.getEmail());
		
		Optional<User> oUser = userRepo.findByEmail(userInput.getEmail());
		

			if(oUser.isPresent()) {
				throw new UsernameNotFoundException("User Email already exists",null);
			}
		else {
			if(null != userInput.getEmail()) {
				
				    Random r = new Random( System.currentTimeMillis() );
				    int otp =   10000 + r.nextInt(20000);
				    Optional<User> optional = userRepo.findByUserid(Long.valueOf(userInput.getUserid()));
				    if(optional.isPresent()){
				    	User user = optional.get();
				    	
				    	user.setCreatedAt(userInput.getCreatedAt());
				    
				    	if(!userInput.getCountrycode().equals("+91")){
				    		user.setOtp(String.valueOf(otp));
				    		ApplicationContext context = new
									 ClassPathXmlApplicationContext("Spring-Mail.xml");
									 EmailSender mm = (EmailSender) context.getBean("mailMail");
									 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
									 Date dateobj = new Date();
									 InetAddress ip = null;
									 try {
										ip = InetAddress.getLocalHost();
									} catch (UnknownHostException e) {
									
										e.printStackTrace();
									}
									 String messagecontent="Your verification code is "+otp;
									 mm.sendattaMail("Invitation from DiiNG DoNG App", userInput.getEmail(),
												messagecontent);
									out.put("Type","S");
									out.put("message","Otp send successfully to your email, Please check the email to continue your application");
				    	}else{
				    		
				    	}
				    	userRepo.save(user);	
				    }
				    
//					String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
//					SecureRandom secureRnd = new SecureRandom();
//					StringBuilder sb = new StringBuilder(10);
//					for (int i = 0; i < 10; i++) {
//					sb.append(SOURCE.charAt(secureRnd.nextInt(SOURCE.length())));
//					}
//					String password = sb.toString();
					
					
//					System.out.println("password  "+password);
//					String encryptpassword = commonUtils.encrypt("SocialMediaappln", "nlppaaideMlaicoS", password);
//					Optional<User> optional = userRepo.findByUserid(Long.valueOf(userInput.getUserid()));
//					if(optional.isPresent()){
//						User user = optional.get();
//						user.setEmail(userInput.getEmail());
//						user.setPassword(encryptpassword);
//						user.setUsername(userInput.getEmail());
//						user.setUserstatus("1"); 
//						user.setDob(userInput.getDob());
//						user.setDiingdongId(connutil.randomNumber());
//						userRepo.save(user);
//						ApplicationContext context = new
//								 ClassPathXmlApplicationContext("Spring-Mail.xml");
//								 EmailSender mm = (EmailSender) context.getBean("mailMail");
//								 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//								 Date dateobj = new Date();
//								 InetAddress ip = null;
//								 try {
//									ip = InetAddress.getLocalHost();
//								} catch (UnknownHostException e) {
//								
//									e.printStackTrace();
//								}
//
//								 String messagecontent= "<table cellspacing='0' border='0' cellpadding='0' width='100%' height='100%' bgcolor='#adadad'>"
//				                                        +"<tbody><tr> <td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif'> "
//				                                        		+ " <table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>  <tbody>  <tr> <td> <table width='100%' cellspacing='0' cellpadding='0' border='0'> "
//				                                        		+ "<tbody> <tr> <td style='text-align: left; padding: 15px 0px 0px 20px'>&nbsp;</td> "
//				                                        		+ "</tr></tbody> </table> </td> </tr> <tr><td style='padding: 0 3px'><table border='0' cellpadding='0' cellspacing='0' style='border: 1px solid #6c6c6c;width: 100%;text-align: left;' bgcolor='#ffffff'>"
//				                                        		+ "<tbody> <tr> <td> <table cellpadding='20' cellspacing='0' border='0' height='100' style='border-bottom: 1px solid #cccccc; width: 100%; height: 100px' bgcolor='#cecece'> "
//				                                        		+ "<tbody><tr><td valign='middle' style='color: #474747;'><a target='_blank' rel='spantaglogin'><img src='http://spantagtech.com/our-products-logo/dingdong.png' alt='logo' border='0' width='auto' height='90'>"
//				                                        		+ "</a> </td> </tr>  </tbody> </table></td></tr><tr><td valign='middle'><table cellpadding=20' cellspacing='0' border='0' style='width: 100%; min-height: 350px; font-family: Arial,Verdana,sans-serif'>"
//				                                        		+ "<tbody><tr><td valign='top'> <h1 style='font-family: Arial,Verdana,sans-serif;font-size: 22px;color: #555555;'><span style='font-size: 18px'>Please confirm your email address to continue DiiNG DoNG APP</span>"
//				                                        		+ "</h1><div style='color: #555555; font-size: 12px; line-height: 20px'>Dear <span font-weight: bold;>user</span>,<br /> We recently received a request to create password to your account:  <a href='' style='color: #555555; font-weight: bold; text-decoration: none' rel='noreferrer'>"
//				                                        		+ " <br><span>"+userInput.getEmail()+"</span></a><br><br>Your new password is:<br>"
//				                                        		+ "<br> <a href='' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>"+password+"</a><br><br>If you did not request to reset your password, simply disregard this email. No changes will be made to your account. Remember, your Product ID and password gives you access to multiple great products by Product.<br>"
//				                                        		+ "<br><p style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>Time of Request: "+df.format(dateobj)+"<br>From IP Address: "+ip+"<br>From location: Nagercoil, India<br>"
//				                                        		+ "</p> </div></td> </tr> <tr bgcolor='#edeff0' width='100%'><td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
//				                                        		+ "<table cellspacing='0' border='0' cellpadding='0' width='100%' style='border-top: 1px solid #c0c0c0'><tbody> <tr>"
//				                                        		+ "<td align='left' valign='middle' style='padding: 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'><strong>Important Security Notice:</strong><br>Products never asks for your password or other sensitive information by email. Do not click links or respond to a suspicious email! For details, visit "
//				                                        		+ "<a href='http://spantagtech.com/' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>spantagtech.com</a>.<br></td>"
//				                                        		+ "</tr> </tbody></table></td> </tr><tr bgcolor='#edeff0' width='100%'>"
//				                                        		+ "<td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
//				                                        		+ " <table cellspacing='0' border='0' cellpadding='0' width='100%'><tbody> <tr>"
//				                                        		+ "<td align='left' valign='middle' style='padding: 0 20px 20px 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'>Replies to this email are not monitored. Need help with your account? <a href='http://spantagtech.com/contact-us.html' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>Contact Customer Support</a>."
//				                                        		+ "<br> <br>  SpanTAG Technologies,4-9B Kavimani Devi Nagar,Melaputhery,Nagercoil -629001  </td> </tr> </tbody>"
//				                                        		+ " </table> </td></tr> </tbody></table><br><br> </td> </tr></tbody> </table> </td></tr></tbody></table>" ;
//
//								
//								 
//
//								 mm.sendattaMail("Invitation from DiiNG DoNG App", userInput.getEmail(),
//											messagecontent);
//								out.put("Type","S");
//								out.put("message","Registered successfully, Please check the email to continue your application");
//					}
//					
					
				
			}
		}

		return out;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		
		Optional<User> oUser = userRepo.findByUsername(username);
		if (!oUser.isPresent()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		User user = oUser.get();
		return new CustomUser(user.getUsername(), user.getPassword(),
				user.getUserid(), getAuthority(user.getUsername()));

	}

	private List<SimpleGrantedAuthority> getAuthority(String userName) {

		return Arrays
				.asList(new SimpleGrantedAuthority(userName.toUpperCase()));

	}


	
	public Map<String, Object> getdashboarddetails() {

		int year = Calendar.getInstance().get(Calendar.YEAR);

		try {
			quoteDBProcedureHelper.callCreateCustomerProcedure(year);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		String customercountquery = env.getProperty("customercountquery");
		String currentmonthcustomercountquery = env
				.getProperty("currentmonthcustomercountquery");
		String orderscountquery = env.getProperty("orderscountquery");
		String currentmonthordersquery = env
				.getProperty("currentmonthordercountquery");
		String totalamountcountquery = env.getProperty("totalamountcountquery");
		String currentmonthtotalamountcountquery = env
				.getProperty("currentmonthtotalamountcountquery");
		String barcountquery = env.getProperty("bargraphvalue");
		List<BigInteger> customercount = connutil.executequerysdashboard(em,
				customercountquery);
		List<BigInteger> currentmonthcustomercount = connutil
				.executequerysdashboard(em, currentmonthcustomercountquery);
		List<BigInteger> orderscount = connutil.executequerysdashboard(em,
				orderscountquery);
		List<BigInteger> currentmonthorderscount = connutil
				.executequerysdashboard(em, currentmonthordersquery);
		List<BigInteger> totalamountcount = connutil.executequerysdashboard(em,
				totalamountcountquery);
		List<BigInteger> currentmonthtotalamountcount = connutil
				.executequerysdashboard(em, currentmonthtotalamountcountquery);
		List<Object[]> typelist = connutil.executequerys(em, barcountquery);
		List typeList = new ArrayList();
		if (typelist != null && typelist.size() > 0) {
			for (Object[] object : typelist) {
				Map<String, Object> responsemap = new HashMap<String, Object>();
				responsemap.put("month", object[0]);
				responsemap.put("percentage", object[1]);

				typeList.add(responsemap);
			}
		}
		Map<String, Object> out = new LinkedHashMap<>();
		out.put("customercount", customercount.get(0));
		out.put("currentmonthcustomercount", currentmonthcustomercount.get(0));
		out.put("orderscount", orderscount.get(0));
		out.put("currentmonthorderscount", currentmonthorderscount.get(0));
		out.put("totalamountcount", totalamountcount.get(0));
		out.put("currentmonthtotalamountcount",
				currentmonthtotalamountcount.get(0));
		out.put("typeList", typeList);

		return out;
	}

	@Override
	public Map<String, Object> changePassword(@Valid ResetPasswordDTO userInput) {
		

		return null;
	}


	@Override
	public Map<String, Object> forgotPassword(SignUpDTO signIn) {

		
		Map<String, Object> out = new LinkedHashMap<>();
		if(signIn.getEmail() !=null){
			Optional<User> oUser = userRepo.findByEmail(signIn.getEmail());
			if(oUser.isPresent()){
//				String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
//				SecureRandom secureRnd = new SecureRandom();
//				StringBuilder sb = new StringBuilder(10);
//				for (int i = 0; i < 10; i++) {
//				sb.append(SOURCE.charAt(secureRnd.nextInt(SOURCE.length())));
//				}
//				String password = sb.toString();
//				
              
				User updUser = oUser.get();
				  String password =updUser.getPassword();
							System.out.println("password  "+password);
							String encryptpassword = commonUtils.decrypt("SocialMediaappln", "nlppaaideMlaicoS", password);
//				updUser.setPassword(encryptpassword);
//				userRepo.save(updUser);
				ApplicationContext context = new
						 ClassPathXmlApplicationContext("Spring-Mail.xml");
						 EmailSender mm = (EmailSender) context.getBean("mailMail");
						 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
						 Date dateobj = new Date();
						 InetAddress ip = null;
						 try {
							ip = InetAddress.getLocalHost();
						} catch (UnknownHostException e) {
							
							e.printStackTrace();
						}
						
						 String messagecontent= "<table cellspacing='0' border='0' cellpadding='0' width='100%' height='100%' bgcolor='#adadad'>"
		                                        +"<tbody><tr> <td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif'> "
		                                        		+ " <table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>  <tbody>  <tr> <td> <table width='100%' cellspacing='0' cellpadding='0' border='0'> "
		                                        		+ "<tbody> <tr> <td style='text-align: left; padding: 15px 0px 0px 20px'>&nbsp;</td> "
		                                        		+ "</tr></tbody> </table> </td> </tr> <tr><td style='padding: 0 3px'><table border='0' cellpadding='0' cellspacing='0' style='border: 1px solid #6c6c6c;width: 100%;text-align: left;' bgcolor='#ffffff'>"
		                                        		+ "<tbody> <tr> <td> <table cellpadding='20' cellspacing='0' border='0' height='100' style='border-bottom: 1px solid #cccccc; width: 100%; height: 100px' bgcolor='#cecece'> "
		                                        		+ "<tbody><tr><td valign='middle' style='color: #474747;'><a target='_blank' rel='spantaglogin'><img src='http://diingdong.co.in/logo-new.png' alt='logo' border='0' width='auto' height='90'>"
		                                        		+ "</a> </td> </tr>  </tbody> </table></td></tr><tr><td valign='middle'><table cellpadding=20' cellspacing='0' border='0' style='width: 100%; min-height: 350px; font-family: Arial,Verdana,sans-serif'>"
		                                        		+ "<tbody><tr><td valign='top'> <h1 style='font-family: Arial,Verdana,sans-serif;font-size: 22px;color: #555555;'><span style='font-size: 18px'>Please confirm your email address to continue DiiNG DoNG APP</span>"
		                                        		+ "</h1><div style='color: #555555; font-size: 12px; line-height: 20px'>Dear <span font-weight: bold;>user</span>,<br /> We recently received a request to create password to your account:  <a href='' style='color: #555555; font-weight: bold; text-decoration: none' rel='noreferrer'>"
		                                        		+ " <br><span>"+signIn.getEmail()+"</span></a><br><br>Your new password is:<br>"
		                                        		+ "<br> <a href='' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>"+encryptpassword+"</a><br><br>If you did not request to reset your password, simply disregard this email. No changes will be made to your account. Remember, your Product ID and password gives you access to multiple great products by Product.<br>"
		                                        		+ "<br><p style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>Time of Request: "+df.format(dateobj)+"<br>From IP Address: "+ip+"<br>From location: Nagercoil, India<br>"
		                                        		+ "</p> </div></td>"
		                                        		+ "</tr></tbody> </table></td> </tr> <tr bgcolor='#edeff0' width='100%'><td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
		                                        		+ "<table cellspacing='0' border='0' cellpadding='0' width='100%' style='border-top: 1px solid #c0c0c0'><tbody> <tr>"
		                                        		+ "<td align='left' valign='middle' style='padding: 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'><strong>Important Security Notice:</strong><br>Products never asks for your password or other sensitive information by email. Do not click links or respond to a suspicious email! For details, visit "
		                                        		+ "<a href='http://spantagtech.com/' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>spantagtech.com</a>.<br></td>"
		                                        		+ "</tr> </tbody></table></td> </tr><tr bgcolor='#edeff0' width='100%'>"
		                                        		+ "<td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
		                                        		+ " <table cellspacing='0' border='0' cellpadding='0' width='100%'><tbody> <tr>"
		                                        		+ "<td align='left' valign='middle' style='padding: 0 20px 20px 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'>Replies to this email are not monitored. Need help with your account? <a href='http://spantagtech.com/contact-us.html' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>Contact Customer Support</a>."
		                                        		+ "<br> <br>  SpanTAG Technologies,4-9B Kavimani Devi Nagar,Melaputhery,Nagercoil -629001  </td> </tr> </tbody>"
		                                        		+ " </table> </td></tr> </tbody></table><br><br> </td> </tr></tbody> </table> </td></tr></tbody></table>" ;

						
						 

						 mm.sendattaMail("Forgot password", signIn.getEmail(),
									messagecontent);
						out.put("Type","S");
						out.put("message","Password changed successfully, Please check the email to continue your application.");
				
			}else{
				out.put("Type","F");
				out.put("message", "User not found");
			}
		}
		return out;
	
	}


	@Override
	public Map<String, Object> verifyOtp(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
		if(signIn.getOtp() !=null){
			Optional<User> oUser = userRepo.findByUserid(signIn.getUserid());
			if(oUser.isPresent()){
				User otp = oUser.get();
				if(otp.getOtp().equals(signIn.getOtp())){
					otp.setUserstatus("1");
					userRepo.save(otp);
					out.put("Type","S");
					out.put("message","Valid otp");
			
				}else{
					User otps = oUser.get();
					otps.setUserstatus("0");
					userRepo.save(otps);
					out.put("Type","F");
					out.put("message","Invalid otp");
			
				}
			}
		}
		return out;
	}


	@Override
	public Map<String, Object> resendOtp(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
		Optional<User> oUser = userRepo.findByUserid(1);
		if(oUser.isPresent()){
			User otpSms = oUser.get();
			 Random r = new Random( System.currentTimeMillis() );
			 int otp =   10000 + r.nextInt(20000);
			
			
			String templateMessage = "Your verification code is "+otp;
			String apiKey = "apikey=" + apikey;
			String message = "&message=" + templateMessage;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + otpSms.getPhoneNo();
			String username = "&username="+smsUserName;
			String apipassword = "&password="+smsPassword;
			
			System.out.println("SMS Settings Values are   "+apiKey+"   "+message+"   "+numbers+"   "+username+"   "+apipassword +"  "+otp);
			
			 StringBuffer stringBuffer = new StringBuffer();
			HttpURLConnection conn;
			try {
				conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			
			String data = apiKey + numbers + message + sender+username+apipassword;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			String SMSresult = stringBuffer.toString();
			System.out.println("sms settings 222"+stringBuffer.toString());
            JSONObject jsonObj = new JSONObject(SMSresult);
			
			if(jsonObj.get("status") != null && ((String)jsonObj.get("status")).equalsIgnoreCase("success"))
			{
				 otpSms.setOtp(String.valueOf(otp));
					userRepo.save(otpSms);
					out.put("Type","S");
					out.put("message","Otp send successfully.");
			}else{
				out.put("Type","F");
				out.put("message","Invalid mobile number");	
			}
			rd.close();
			} catch (MalformedURLException e1) {
				
				e1.printStackTrace();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return out;
	}
	@Override
	public Map<String, Object> updateProfilewithstatus(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
		Optional<User> oUser = userRepo.findByUserid(signIn.getUserid());
		if(oUser.isPresent()){
		
			User updUser  = oUser.get();
			updUser.setAccountStatus(signIn.getAccountStatus());
			 userRepo.save(updUser);
			  out.put("Type","S");
			out.put("message","Profile updated successfully");
		}
		return out;
	}

	@Override
	public Map<String, Object> updateProfile(SignUpDTO signIn,HttpServletRequest httpRequest) {
		Map<String, Object> out = new LinkedHashMap<>();
		Optional<User> oUser = userRepo.findByEmailAndUseridNot(signIn.getEmail(),signIn.getUserid());
		if(oUser.isPresent()){
			out.put("Type","F");
			out.put("message","User already exist.");
		}else{
			Optional<User> upd = userRepo.findByUserid(signIn.getUserid());
			if(upd.isPresent()){
				System.out.println("  "+signIn.getFiletype());
				User updUser  = upd.get();
				updUser.setFullName(signIn.getFullName());
				updUser.setHandleName(signIn.getHandleName());
				//updUser.setUsername(signIn.getUsername());
				updUser.setAbout(signIn.getAbout());
				updUser.setAge(signIn.getAge());
				updUser.setGender(signIn.getGender());
				//updUser.setEmail(signIn.getEmail());
				updUser.setPhoneNo(signIn.getPhoneNo());
				updUser.setDob(signIn.getDob());
				System.out.println("file name "+signIn.getFileName());
				if(signIn.getFileName() !=null && signIn.getFileName() != ""){
					if(updUser.getProfilePath()!=null && signIn.getFileName() != ""){
						connutil.deleteFileFromS3Bucket(updUser.getProfilePath());
					}
					/*String rootLocation = commonUtils.getserverfilepath(httpRequest);
					rootLocation=rootLocation+File.separator+signIn.getFileName();
					System.out.println("ori"+rootLocation);
					File file = new File(rootLocation);
					
					String fileName = file.getName();
					//if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
					String extension= fileName.substring(fileName.lastIndexOf(".")+1);
					System.out.println("extension = "+extension);
					
					String destination = commonUtils.getserverfilepath(httpRequest);
					destination = destination+File.separator+"profile"+File.separator+"image";
					File uploadPath = new File(destination);
					
					 if(!uploadPath.exists())
					{
						 uploadPath.mkdirs();
					}
					destination = destination+File.separator+signIn.getUserid()+"."+extension;
				System.out.println("destination file  = "+destination);
				
				if(updUser.getProfilePath() != null && updUser.getFiletype() != null && updUser.getProfilePath().length() > 0 && updUser.getFiletype().length() > 0 )
						{ 
							String myrootLocation=commonUtils.getdelprofilefilepath(httpRequest, "profile", 0,0);
							// if file copied successfully then delete the original file 
							File f= new File(myrootLocation+File.separator+updUser.getProfilePath());
							f.delete();
							System.out.println("File deleted successfully"+myrootLocation+updUser.getProfilePath()); 
						} 
				file.renameTo(new File(destination));*/
				updUser.setFiletype("image/"+signIn.getFiletype());
				updUser.setProfilePath(signIn.getFileName() );
				
				}
				
			  userRepo.save(updUser);
			  out.put("Type","S");
			out.put("message","Profile updated successfully");
			}
		}
		
		return out;
	}


	@Override
	public Map<String, Object> getUserDetailsById(String userId) {
		Map<String, Object> out = new LinkedHashMap<>();
		Optional<User> optional = userRepo.findByUserid(Long.valueOf(userId));
		 ArrayList<User> res = new ArrayList<>();
		if(optional.isPresent()){
			 res.add(optional.get() );
			out.put("Type", "S");
			out.put("userList", res);
			
		}else{
			out.put("Type", "F");
		}
		System.out.println("list "+out);
		return out; 
	}




	@Override
	public Map<String, Object> RegisterMobileNo(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
		String userstatus = "0";
		if(signIn.getPhoneNo() !=null){
			Optional<User> optional1 = userRepo.findByPhoneNo(signIn.getPhoneNo());
			if(optional1.isPresent()){
				userstatus = optional1.get().getUserstatus();
				if(optional1.get().getUserstatus() == null){
					userstatus = "0";
				}else{
					userstatus = optional1.get().getUserstatus();
				}
			}else{
				userstatus = "0";
			}
            if(userstatus == "0"){
			if(signIn.getCountrycode().equals("+91") && smsEnable.equals("yes")){
				 Random r = new Random( System.currentTimeMillis() );
				 int otp =   10000 + r.nextInt(20000);
				 String templateMessage = "OTP for DiiNG DoNG user authentication is "+otp+".Do not share OTP for security reasons.";
					String apiKey = "apikey=" + apikey;
					String message = "&message=" + templateMessage;
					String sender = "&sender=" + "KRITHA";
					String numbers = "&numbers=" +signIn.getPhoneNo();
					String username = "&username="+smsUserName;
					String apipassword = "&password="+smsPassword;
					
					System.out.println("SMS Settings Values are   "+apiKey+"   "+message+"   "+numbers+"   "+username+"   "+apipassword +"  "+otp);
					
					 StringBuffer stringBuffer = new StringBuffer();
					HttpURLConnection conn;
					try {
						conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
					
					String data = apiKey + numbers + message + sender+username+apipassword;
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
					conn.getOutputStream().write(data.getBytes("UTF-8"));
					final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					
					String line;
					while ((line = rd.readLine()) != null) {
						stringBuffer.append(line);
						System.out.println("sms settings"+stringBuffer.toString());
					}
					String SMSresult = stringBuffer.toString();
					System.out.println("sms settings 222"+stringBuffer.toString());
	                JSONObject jsonObj = new JSONObject(SMSresult);
					
					if(jsonObj.get("status") != null && ((String)jsonObj.get("status")).equalsIgnoreCase("success"))
					{
						Optional<User> optional = userRepo.findByPhoneNo(signIn.getPhoneNo());
						if(optional.isPresent()){
							User upd = optional.get();
							upd.setOtp(String.valueOf(otp));
							userRepo.save(upd);
							out.put("Type", "S");
							out.put("userId", upd.getUserid());
						}else{
							User user = new User();
							user.setPhoneNo(signIn.getPhoneNo());
							user.setOtp(String.valueOf(otp));
							User output = userRepo.save(user);
							out.put("Type", "S");
							out.put("userId", output.getUserid());
						}
					}else{
						out.put("Type", "F");
						out.put("message","Invalid");
					}
					rd.close();
					} catch (MalformedURLException e1) {
						
						e1.printStackTrace();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				
			}else{

				Optional<User> optional = userRepo.findByPhoneNo(signIn.getPhoneNo());
				if(optional.isPresent()){
					User upd = optional.get();
					//upd.setOtp(String.valueOf(otp));
					userRepo.save(upd);
					out.put("Type", "S");
					out.put("userId", upd.getUserid());
				}else{
					User user = new User();
					user.setPhoneNo(signIn.getPhoneNo());
					//user.setOtp(String.valueOf(otp));
					User output = userRepo.save(user);
					out.put("Type", "S");
					out.put("userId", output.getUserid());
				}
			}
//				}else{
//					out.put("Type", "F");
//					out.put("message","Invalid");	
//				}
//				rd.close();
//				} catch (MalformedURLException e1) {
//					
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					
//					e1.printStackTrace();
//				}
//
// catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		}else{
			out.put("Type", "F");
			out.put("message","Mobile no already registered");
		}
		}else{
			out.put("Type", "F");
			out.put("message","Invalid");
		}
		
		return out;
	}


	@Override
	public Map<String, Object> socialLogin(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
	
		
     Optional<User> oUser = userRepo.findByEmail(signIn.getEmail());
		System.out.println("email===="+signIn.getEmail());
		if (null != signIn.getEmail()) {
			if(!oUser.isPresent()) {
				User ins = new User();
				ins.setUsername(signIn.getUsername());
				ins.setEmail(signIn.getEmail());
				ins.setUserstatus("1");
				ins.setPassword("zXxsfQAQInmHwKFDGITINw==");
				ins.setDiingdongId(connutil.randomNumber());
				ins.setApkToken(signIn.getApkToken());
				User saveOut=userRepo.save(ins);
				 Optional<User> insUser = userRepo.findByEmail(signIn.getEmail());
				User userexist = insUser.get();
				
				String token = jwtTokenUtil.generateToken(userexist);
				String name = userexist.getUsername();
				out.put("userid", userexist.getUserid());
				out.put("mail", userexist.getEmail());
				out.put("name", name);
				out.put("token", token);
				out.put("message", "User logged in successfully !");
			
			} 
		else {
			User userexist = oUser.get();
				
						String token = jwtTokenUtil.generateToken(userexist);
						String name = userexist.getUsername();
						out.put("userid", userexist.getUserid());
						out.put("mail", userexist.getEmail());
						out.put("name", name);
						out.put("token", token);
						out.put("message", "User logged in successfully !");
				}
		}
		
		return out;
	}
	@Override
	public Map<String, Object> fbloginwithoutemail(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
	
		
     Optional<User> oUser = userRepo.findByUsername(signIn.getUsername());
		System.out.println("email===="+signIn.getUsername());
		if (null != signIn.getUsername()) {
			if(!oUser.isPresent()) {
				User ins = new User();
				ins.setUsername(signIn.getUsername());
				//ins.setEmail(signIn.getEmail());
				ins.setUserstatus("1");
				ins.setPassword("zXxsfQAQInmHwKFDGITINw==");
				ins.setDiingdongId(connutil.randomNumber());
				ins.setApkToken(signIn.getApkToken());
				User saveOut=userRepo.save(ins);
				 Optional<User> insUser = userRepo.findByUsername(signIn.getUsername());
				User userexist = insUser.get();
				
				String token = jwtTokenUtil.generateToken(userexist);
				String name = userexist.getUsername();
				out.put("userid", userexist.getUserid());
				out.put("mail", userexist.getEmail());
				out.put("name", name);
				out.put("token", token);
				out.put("message", "User logged in successfully !");
			
			} 
		else {
			User userexist = oUser.get();
				
						String token = jwtTokenUtil.generateToken(userexist);
						String name = userexist.getUsername();
						out.put("userid", userexist.getUserid());
						out.put("mail", userexist.getEmail());
						out.put("name", name);
						out.put("token", token);
						out.put("message", "User logged in successfully !");
				}
		}
		
		return out;
	}

	
	@Override
	public Map<String, Object> loaduser() {
		// TODO Auto-generated method stub
		List<User> userList = userRepo.findAll();
		Map<String, Object> out = new LinkedHashMap<>();
		out.put("userList", userList);
		return out;
	}
	
	
	@Override
	public Map<String, Object> sendInvitationLink(SignUpDTO signIn) {
		Map<String, Object> out = new LinkedHashMap<>();
		
		String templateMessage = "Hey I'm using DiiNG DoNG for entertainment! It's a fast ,simple and secure app.Join me !";
		String apiKey = "apikey=" + apikey;
		String message = "&message=" + templateMessage;
		String sender = "&sender=" + "TXTLCL";
		String numbers = "&numbers=" + signIn.getPhoneNo();
	
		String username = "&username="+smsUserName;
		String apipassword = "&password="+smsPassword;
		
		System.out.println("SMS Settings Values are   "+apiKey+"   "+message+"   "+numbers+"   "+username+"   "+apipassword );
		
		 StringBuffer stringBuffer = new StringBuffer();
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
		
		String data = apiKey + numbers + message + sender+username+apipassword;
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
		conn.getOutputStream().write(data.getBytes("UTF-8"));
		final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String line;
		while ((line = rd.readLine()) != null) {
			stringBuffer.append(line);
		}
		String SMSresult = stringBuffer.toString();
		System.out.println("sms settings 222"+stringBuffer.toString());
        JSONObject jsonObj = new JSONObject(SMSresult);
		
		if(jsonObj.get("status") != null && ((String)jsonObj.get("status")).equalsIgnoreCase("success"))
		{
			
				out.put("Type","S");
				out.put("message","Invitation send successfully");
		}else{
			out.put("Type","F");
			out.put("message","Invalid mobile number");	
		}
		rd.close();
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	
	@Override
	public Map<String, Object> changeuserpassword(SignUpDTO signIn,HttpServletRequest httpRequest) {

		
		Map<String, Object> out = new LinkedHashMap<>();	
		
		Optional<User> ouser = userRepo.findByUserid(Long.valueOf(signIn.getUserid()));
		Optional<User> upd = userRepo.findByUserid(signIn.getUserid());
		
			if(upd.isPresent()){
				
				User updUser  = upd.get();
				
				String decryptpassword = commonUtils.decrypt("SocialMediaappln", "nlppaaideMlaicoS", updUser.getPassword());			
				
				if(signIn.getPassword().equals(decryptpassword) ){
					
					String encryptpassword = commonUtils.encrypt("SocialMediaappln", "nlppaaideMlaicoS", signIn.getNewPassword());
					  updUser.setPassword(encryptpassword);
					  updUser.setUserid(Long.valueOf(signIn.getUserid()));
					
					  userRepo.save(updUser);
					  
					  ApplicationContext context = new
								 ClassPathXmlApplicationContext("Spring-Mail.xml");
								 EmailSender mm = (EmailSender) context.getBean("mailMail");
								 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
								 Date dateobj = new Date();
								 InetAddress ip = null;
								 try {
									ip = InetAddress.getLocalHost();
								} catch (UnknownHostException e) {
									
									e.printStackTrace();
								}
								
								 String messagecontent= "<table cellspacing='0' border='0' cellpadding='0' width='100%' height='100%' bgcolor='#adadad'>"
				                                        +"<tbody><tr> <td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif'> "
				                                        		+ " <table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>  <tbody>  <tr> <td> <table width='100%' cellspacing='0' cellpadding='0' border='0'> "
				                                        		+ "<tbody> <tr> <td style='text-align: left; padding: 15px 0px 0px 20px'>&nbsp;</td> "
				                                        		+ "</tr></tbody> </table> </td> </tr> <tr><td style='padding: 0 3px'><table border='0' cellpadding='0' cellspacing='0' style='border: 1px solid #6c6c6c;width: 100%;text-align: left;' bgcolor='#ffffff'>"
				                                        		+ "<tbody> <tr> <td> <table cellpadding='20' cellspacing='0' border='0' height='100' style='border-bottom: 1px solid #cccccc; width: 100%; height: 100px' bgcolor='#cecece'> "
				                                        		+ "<tbody><tr><td valign='middle' style='color: #474747;'><a target='_blank' rel='spantaglogin'><img src='http://diingdong.co.in/logo-new.png' alt='logo' border='0' width='auto' height='90'>"
				                                        		+ "</a> </td> </tr>  </tbody> </table></td></tr><tr><td valign='middle'><table cellpadding=20' cellspacing='0' border='0' style='width: 100%; min-height: 350px; font-family: Arial,Verdana,sans-serif'>"
				                                        		+ "<tbody><tr><td valign='top'> <h1 style='font-family: Arial,Verdana,sans-serif;font-size: 22px;color: #555555;'><span style='font-size: 18px'>Please confirm your email address to continue DiiNG DoNG APP</span>"
				                                        		+ "</h1><div style='color: #555555; font-size: 12px; line-height: 20px'>Dear <span font-weight: bold;>user</span>,<br /> We recently received a request to create password to your account:  <a href='' style='color: #555555; font-weight: bold; text-decoration: none' rel='noreferrer'>"
				                                        		+ " <br><span>"+updUser.getEmail()+"</span></a><br><br>Your new password is:<br>"
				                                        		+ "<br> <a href='' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>"+signIn.getNewPassword()+"</a><br><br>If you did not request to reset your password, simply disregard this email. No changes will be made to your account. Remember, your Product ID and password gives you access to multiple great products by Product.<br>"
				                                        		+ "<br><p style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>Time of Request: "+df.format(dateobj)+"<br>From IP Address: "+ip+"<br>From location: Nagercoil, India<br>"
				                                        		+ "</p> </div></td>"
				                                        		+ "</tr></tbody> </table></td> </tr> <tr bgcolor='#edeff0' width='100%'><td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
				                                        		+ "<table cellspacing='0' border='0' cellpadding='0' width='100%' style='border-top: 1px solid #c0c0c0'><tbody> <tr>"
				                                        		+ "<td align='left' valign='middle' style='padding: 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'><strong>Important Security Notice:</strong><br>Products never asks for your password or other sensitive information by email. Do not click links or respond to a suspicious email! For details, visit "
				                                        		+ "<a href='http://spantagtech.com/' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>spantagtech.com</a>.<br></td>"
				                                        		+ "</tr> </tbody></table></td> </tr><tr bgcolor='#edeff0' width='100%'>"
				                                        		+ "<td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
				                                        		+ " <table cellspacing='0' border='0' cellpadding='0' width='100%'><tbody> <tr>"
				                                        		+ "<td align='left' valign='middle' style='padding: 0 20px 20px 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'>Replies to this email are not monitored. Need help with your account? <a href='http://spantagtech.com/contact-us.html' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>Contact Customer Support</a>."
				                                        		+ "<br> <br>  SpanTAG Technologies,4-9B Kavimani Devi Nagar,Melaputhery,Nagercoil -629001  </td> </tr> </tbody>"
				                                        		+ " </table> </td></tr> </tbody></table><br><br> </td> </tr></tbody> </table> </td></tr></tbody></table>" ;

								
								 

								 mm.sendattaMail("Change password", updUser.getEmail(),
											messagecontent);
					  out.put("Type","S");
					  out.put("message","Password updated successfully");
				}else{
					  out.put("Type","F");
					  out.put("message","Old Password is wrong");
				}
				
			}
		

		return out;

	}
	
	@Override
	public List<SignUpDTO> SearchPeople(String userid) {
		 String mQuery= env.getProperty("checkblockconditionQuery");
         String blockedids = "0";

         List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
         List<Object> blockconditionList = new ArrayList();
          for (String object : blockconditionlist) {
                         Map<String, Object> out = new LinkedHashMap<>();
                         if(object != null && !object.equals("") && object.length() > 0){
                                 //out.put("blockstatus", object);
                                 String[] blockedidss = object.split(",");
                             if(blockedidss.length > 0){
                                     for(int i = 0; i< blockedidss.length; i++){
                                             blockedids = blockedidss[i] + ",";
                                     }
                             }
                             if(blockedids != null && blockedids.length()>0  ){
                                     blockedids = blockedids.substring(0, blockedids.length() - 1);
                             }
                         }
          }
		String Query= env.getProperty("searchpeoplequery");
		
		List<Object[]> orderlist = connutil.executequerytwobyid(em,Query,userid,blockedids);
		System.out.println(orderlist.size());
		List<SignUpDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SignUpDTO User = new SignUpDTO();
			 User.setUserid(Long.valueOf(String.valueOf(object[0])));
			 User.setUsername(String.valueOf(object[1]));
			 User.setFollowcount(String.valueOf(object[2]));
			 User.setImageurl(String.valueOf(object[3]));
			 User.setUsercount(String.valueOf(object[4]));
			 User.setAcceptRequest(String.valueOf(object[5]));
            
             orderList.add(User);
     }

		return orderList;
	}
	@Override
	public List<SignUpDTO> SearchByname(String name,String userid, String length) {
		
		String mQuery= env.getProperty("checkblockconditionQuery");
		String blockedids = "0";

		List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
		List<Object> blockconditionList = new ArrayList();
		 for (String object : blockconditionlist) {
				Map<String, Object> out = new LinkedHashMap<>();
				if(object != null && !object.equals("") && object.length() > 0){
					//out.put("blockstatus", object);
					System.out.println("object   "+object);
					blockedids = object;
					
				}
		 }
		
		 int limitlength = Integer.valueOf(length);
		
		String Query= env.getProperty("SearchByNameQuery");
		
		List<Object[]> orderlist = connutil.executequerywithFourparams(em,Query,name,userid,blockedids,limitlength);
		System.out.println("SearchByNameQuery SearchByNameQuery SearchByNameQuery "+name +"  limitlength "+limitlength);
		List<SignUpDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SignUpDTO User = new SignUpDTO();
			 User.setUserid(Long.valueOf(String.valueOf(object[0])));
			 User.setUsername(String.valueOf(object[1]));
			 User.setFollowcount(String.valueOf(object[2]));
			 User.setImageurl(String.valueOf(object[3]));
			 User.setAcceptRequest(String.valueOf(object[4]));
			 User.setFullName(String.valueOf(object[5]));
             orderList.add(User);
     }

		return orderList;
	}
	
	@Override
	public Map<String, Object> saveNewUser(SignUpDTO userInput) {
		Map<String, Object> out = new LinkedHashMap<>();
		Optional<User> oUser = userRepo.findByEmail(userInput.getEmail());
		

		if(oUser.isPresent()) {
			throw new UsernameNotFoundException("User Email already exists",null);
		}
	else {
		if(null != userInput.getEmail()) {
			   
			 
			   
				String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
				SecureRandom secureRnd = new SecureRandom();
				StringBuilder sb = new StringBuilder(10);
				for (int i = 0; i < 10; i++) {
				sb.append(SOURCE.charAt(secureRnd.nextInt(SOURCE.length())));
				}
			String password = sb.toString();
				
				
				System.out.println("password  "+password);
				String encryptpassword = commonUtils.encrypt("SocialMediaappln", "nlppaaideMlaicoS", password);
				Optional<User> optional = userRepo.findByUserid(Long.valueOf(userInput.getUserid()));
				if(optional.isPresent()){
					User user = optional.get();
					user.setEmail(userInput.getEmail());
				    user.setPassword(encryptpassword);
					user.setUsername(userInput.getEmail());
					user.setUserstatus("1"); 
					user.setDob(userInput.getDob());
					user.setDiingdongId(connutil.randomNumber());
					userRepo.save(user);
					ApplicationContext context = new
							 ClassPathXmlApplicationContext("Spring-Mail.xml");
							 EmailSender mm = (EmailSender) context.getBean("mailMail");
							 DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
							 Date dateobj = new Date();
							 InetAddress ip = null;
							 try {
								ip = InetAddress.getLocalHost();
							} catch (UnknownHostException e) {
						
								e.printStackTrace();
							}

						 String messagecontent= "<table cellspacing='0' border='0' cellpadding='0' width='100%' height='100%' bgcolor='#adadad'>"
			                                        +"<tbody><tr> <td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif'> "
			                                        		+ " <table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>  <tbody>  <tr> <td> <table width='100%' cellspacing='0' cellpadding='0' border='0'> "
			                                        		+ "<tbody> <tr> <td style='text-align: left; padding: 15px 0px 0px 20px'>&nbsp;</td> "
			                                        		+ "</tr></tbody> </table> </td> </tr> <tr><td style='padding: 0 3px'><table border='0' cellpadding='0' cellspacing='0' style='border: 1px solid #6c6c6c;width: 100%;text-align: left;' bgcolor='#ffffff'>"
			                                        		+ "<tbody> <tr> <td> <table cellpadding='20' cellspacing='0' border='0' height='100' style='border-bottom: 1px solid #cccccc; width: 100%; height: 100px' bgcolor='#cecece'> "
			                                        		+ "<tbody><tr><td valign='middle' style='color: #474747;'><a target='_blank' rel='spantaglogin'><img src='http://diingdong.co.in/logo-new.png' alt='logo' border='0' width='auto' height='90'>"
			                                        		+ "</a> </td> </tr>  </tbody> </table></td></tr><tr><td valign='middle'><table cellpadding=20' cellspacing='0' border='0' style='width: 100%; min-height: 350px; font-family: Arial,Verdana,sans-serif'>"
			                                        		+ "<tbody><tr><td valign='top'> <h1 style='font-family: Arial,Verdana,sans-serif;font-size: 22px;color: #555555;'><span style='font-size: 18px'>Please confirm your email address to continue DiiNG DoNG APP</span>"
			                                        		+ "</h1><div style='color: #555555; font-size: 12px; line-height: 20px'>Dear <span font-weight: bold;>user</span>,<br /> We recently received a request to create password to your account:  <a href='' style='color: #555555; font-weight: bold; text-decoration: none' rel='noreferrer'>"
			                                        		+ " <br><span>"+userInput.getEmail()+"</span></a><br><br>Your new password is:<br>"
			                                        		+ "<br> <a href='' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>"+password+"</a><br><br>If you did not request to reset your password, simply disregard this email. No changes will be made to your account. Remember, your Product ID and password gives you access to multiple great products by Product.<br>"
			                                        		+ "<br><p style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>Time of Request: "+df.format(dateobj)+"<br>From IP Address: "+ip+"<br>From location: Nagercoil, India<br>"
			                                        		+ "</p> </div></td> </tr> <tr bgcolor='#edeff0' width='100%'><td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
			                                        		+ "<table cellspacing='0' border='0' cellpadding='0' width='100%' style='border-top: 1px solid #c0c0c0'><tbody> <tr>"
			                                        		+ "<td align='left' valign='middle' style='padding: 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'><strong>Important Security Notice:</strong><br>Products never asks for your password or other sensitive information by email. Do not click links or respond to a suspicious email! For details, visit "
			                                        		+ "<a href='http://spantagtech.com/' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>spantagtech.com</a>.<br></td>"
			                                        		+ "</tr> </tbody></table></td> </tr><tr bgcolor='#edeff0' width='100%'>"
			                                        		+ "<td align='center' valign='top' style='font-size: 11px; font-family: Arial,Verdana,sans-serif; color: #666666'>"
			                                        		+ " <table cellspacing='0' border='0' cellpadding='0' width='100%'><tbody> <tr>"
			                                        		+ "<td align='left' valign='middle' style='padding: 0 20px 20px 20px; font-size: 11px; line-height: 16px; font-family: Arial,Verdana,sans-serif; color: #666666'>Replies to this email are not monitored. Need help with your account? <a href='http://spantagtech.com/contact-us.html' style='outline: none; color: #00aeef; font-weight: bold; text-decoration: none' target='_blank' rel='noreferrer'>Contact Customer Support</a>."
			                                        		+ "<br> <br>  SpanTAG Technologies,4-9B Kavimani Devi Nagar,Melaputhery,Nagercoil -629001  </td> </tr> </tbody>"
			                                        		+ " </table> </td></tr> </tbody></table><br><br> </td> </tr></tbody> </table> </td></tr></tbody></table>" ;

							
							 

						 mm.sendattaMail("Invitation from DiiNG DoNG App", userInput.getEmail(),
										messagecontent);
							out.put("Type","S");
							out.put("message","Registered successfully, Please check the email to continue your application");
				}
				
				
			
		}
	}

	return out;
	}
	
	@Override
	public List<SignUpDTO> getLimitedusersList(int userid,String loginid) {
		

		String Query= env.getProperty("limitedUsersQuery");
		
		List<Object[]> orderlist = connutil.executequerybyidswithinteger(em,Query,loginid,userid);
		System.out.println("getLimitedusersList limitedUsersQuery  "+userid);
		List<SignUpDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SignUpDTO User = new SignUpDTO();
			 User.setUserid(Long.valueOf(String.valueOf(object[0])));
			 User.setUsername(String.valueOf(object[1]));
			 User.setFollowcount(String.valueOf(object[2]));
			 User.setImageurl(String.valueOf(object[3]));
            
             orderList.add(User);
     }

		return orderList;
	}


	@Override
	public List<SignUpDTO> getAdminUserList(String length) {
		
		System.out.println("adminUserListQuery  adminUserListQuery adminUserListQuery ");
		String Query= env.getProperty("adminUserListQuery");
		int lengthlimit = Integer.parseInt(length);
		List<Object[]> list = connutil.executequerybyid(em,Query,lengthlimit);
		List<SignUpDTO> List = new ArrayList();
		 for (Object[] object : list) {
	            
			 SignUpDTO User = new SignUpDTO();
			 User.setUserid(Long.valueOf(String.valueOf(object[0])));
			 User.setUsername(String.valueOf(object[1]));
			 User.setEmail(String.valueOf(object[2]));
			 User.setFullName(String.valueOf(object[3]));
			 User.setDob(String.valueOf(object[4]));
			 User.setGender(String.valueOf(object[5]));
			 User.setAccountStatus(String.valueOf(object[6]));
			 User.setUserstatus(String.valueOf(object[7]));
			 User.setDiingdongid(String.valueOf(object[8]));
			 User.setPhoneNo(String.valueOf(object[9]));
			 User.setTotalpost(String.valueOf(object[10]));
			 User.setIntstatus(String.valueOf(object[11]));
			 List.add(User);
     }

		return List;
	}
	
	@Override
	public List<SMFileDTO> getAdminvideoList(String userid,String length) {
		String Query= env.getProperty("adminvideoListQuery");
		int lengthlimit = Integer.parseInt(length);
		List<Object[]> list = connutil.executequerybystringandinteger(em,Query,userid,lengthlimit);
		List<SMFileDTO> List = new ArrayList();
		 for (Object[] object : list) {
	            
			 SMFileDTO file = new SMFileDTO();
			 file.setFileid(Long.valueOf(String.valueOf(object[0])));
			 file.setFilename(String.valueOf(object[1]));
			 file.setLangName(String.valueOf(object[2]));
			 file.setUsername(String.valueOf(object[3]));
			 file.setVideoType(String.valueOf(object[4]));			 
			 file.setCreatedat(String.valueOf(object[5]));
			 file.setEmail(String.valueOf(object[6]));
			 file.setVideostatus(String.valueOf(object[7]));
			 file.setFileurl(String.valueOf(object[8]));
			 file.setFilecontenttype(String.valueOf(object[9]));
			 List.add(file);
     }

		return List;
	}


	@Override
	public Map<String, Object> blockFromAdmin(SignUpDTO input) {
		
		System.out.println("input.getFiletype()    "+input.getFiletype());
		System.out.println("input.getUserid()      "+input.getUserid());
	 
		if(input.getFiletype().equals("user")){
			Optional<User> upd = userRepo.findByUserid(input.getUserid());
			if(upd.isPresent()){
				User status = upd.get();
				System.out.println("status      "+status);
				
				if(input.getUserstatus().equals("0")){
					status.setUserstatus("0");
				}else{
					status.setUserstatus("1");
				}
				
				userRepo.save(status);
			}
			
		}else{
			Optional<SMFile> upd = fileRepo.findByFileid(input.getUserid());
			if(upd.isPresent()){
				SMFile status = upd.get();
				status.setStatus(0);
				fileRepo.save(status);
			}
		}
		
		Map<String, Object> out = new LinkedHashMap<>();
		out.put("Type", "S");
		if(input.getFiletype().equals("user")){
			out.put("message", "User Blocked successfully");
		}else{
			out.put("message", " Video Deleted successfully");
		}
		
		return out;
	}
	
	
	@Override
	public List<SignUpDTO> AdminSearchByname(String name,String length) {
		
		    int limitlength = Integer.valueOf(length);
			
			String Query= env.getProperty("SearchByAdminNameQuery");
			
			List<Object[]> orderlist = connutil.executequerybystringandinteger(em,Query,name,limitlength);
			
			System.out.println("SearchByNameQuery is "+name +"  limitlength is "+length);
			
			List<SignUpDTO> searchlist = new ArrayList();
			 for (Object[] object : orderlist) {
	            
				 SignUpDTO User = new SignUpDTO();
				 User.setUserid(Long.valueOf(String.valueOf(object[0])));
				 User.setUsername(String.valueOf(object[1]));
				 User.setImageurl(String.valueOf(object[2]));
				 User.setFullName(String.valueOf(object[3]));				
				 User.setUserstatus(String.valueOf(object[4]));
				 User.setAccountStatus(String.valueOf(object[5]));
				 User.setEmail(String.valueOf(object[6]));
				 User.setDiingdongid(String.valueOf(object[7]));
				 User.setPhoneNo(String.valueOf(object[8]));
				 User.setTotalpost(String.valueOf(object[9]));
				 User.setIntstatus(String.valueOf(object[10]));
				 
				 searchlist.add(User);
	     }

			return searchlist;
	}
	
	
	@Override
	public List<SMFileDTO> AdminSearchByFilecaption(String name,String length,String userid) {
		
		    int limitlength = Integer.valueOf(length);
			
			String Query= env.getProperty("SearchByAdminFilecaptionQuery");
			
			List<Object[]> orderlist = connutil.executequerybythreevariable(em,Query,name,userid,limitlength);
			
			System.out.println("SearchByAdminFilecaptionQuery is "+name +"  limitlength is "+length);
			
			List<SMFileDTO> searchlist = new ArrayList();
			 for (Object[] object : orderlist) {
	            
				 SMFileDTO file = new SMFileDTO();
				 file.setFileid(Long.valueOf(String.valueOf(object[0])));
				 file.setFilename(String.valueOf(object[1]));
				 file.setLangName(String.valueOf(object[2]));
				 file.setUsername(String.valueOf(object[3]));
				 file.setVideoType(String.valueOf(object[4]));			 
				 file.setCreatedat(String.valueOf(object[5]));
				 file.setEmail(String.valueOf(object[6]));
				 file.setVideostatus(String.valueOf(object[7]));
				 file.setFileurl(String.valueOf(object[8]));
				 file.setFilecontenttype(String.valueOf(object[9]));
				 searchlist.add(file);
	     }

			return searchlist;
	}



public Map<String, Object> getAdminDashboardDetails() {

	
		String usercountquery = env.getProperty("usercountquery");
		String totalpostquery = env.getProperty("totalpostquery");
		String Activeuserquery = env.getProperty("Activeuserquery");
		String ActivePostsquery = env.getProperty("ActivePostsquery");
		String InActiveuserquery = env.getProperty("InActiveuserquery");
		String InActivePostsquery = env.getProperty("InActivePostsquery");
		
		String totalvideoquery = env.getProperty("totalvideoquery");
		String totalimagequery = env.getProperty("totalimagequery");
		
	
		
		List<BigInteger> Totalusercount1 = connutil.executequerysdashboard(em, usercountquery);
		List<BigInteger> Totalpost1 = connutil.executequerysdashboard(em, totalpostquery);		
		List<BigInteger> Activeusercount1 = connutil.executequerysdashboard(em, Activeuserquery);
		List<BigInteger> ActivePostcount1 = connutil.executequerysdashboard(em, ActivePostsquery);
		List<BigInteger> InActiveusercount1 = connutil.executequerysdashboard(em, InActiveuserquery);
		List<BigInteger> InActivePostcount1 = connutil.executequerysdashboard(em, InActivePostsquery);		
		List<BigInteger> totalvideocount = connutil.executequerysdashboard(em, totalvideoquery);
		List<BigInteger> totalimagecount = connutil.executequerysdashboard(em, totalimagequery);
		
	
		Map<String, Object> out = new LinkedHashMap<>();
		out.put("Totalusercount", Totalusercount1.get(0));
		out.put("totalpost", Totalpost1.get(0));
		out.put("Activeusers", Activeusercount1.get(0));
		out.put("ActivePosts", ActivePostcount1.get(0));	
		out.put("InActiveusers", InActiveusercount1.get(0));
		out.put("InActivePosts", InActivePostcount1.get(0));
		out.put("totalvideocount", totalvideocount.get(0));
		out.put("totalimagecount", totalimagecount.get(0));
		

		return out;
	}
	
	
@Override
public Map<String, Object> Adminsignin(@Valid SignUpDTO userInput) throws JsonProcessingException {

	
	Map<String, Object> out = new LinkedHashMap<>();
	
	Optional<User> oUser = userRepo.findByEmail(userInput.getEmail());
	
	if (null != userInput.getEmail()) {
		if(!oUser.isPresent()) {
			throw new UsernameNotFoundException("invalidUser",null);
		} 
	else {
		if(null != userInput.getPassword() && null != userInput.getEmail()) {
			
			String encryptpassword=commonUtils.encrypt("SocialMediaappln", "nlppaaideMlaicoS", userInput.password);
			System.out.println("encrypted password "+encryptpassword);
			
			Optional<User> user1 = userRepo.findByPasswordAndEmail(encryptpassword,userInput.getEmail() );					
			
			if(!user1.isPresent()) {
				throw new UsernameNotFoundException("user not found for given email and password !",null);
			}
			else {
				User userexist = user1.get();					
				
				System.out.println("userexist.getUserstatus()"+userexist.getUserstatus());
				System.out.println("userInput.getRole() "+userInput.getRole());				
				
				if(userexist.getUserstatus().equals("1") && userInput.getRole().equals("admin")){				
											
					userexist.setApkToken(userInput.getApkToken());
					userRepo.save(userexist);
					
					String token = jwtTokenUtil.generateToken(userexist);
					
				    String name = userexist.getUsername();
					out.put("userid", userexist.getUserid());
					out.put("mail", userexist.getEmail());
					out.put("name", name);
					out.put("token", token);
					out.put("message", "User logged in successfully !");
				}else{
					throw new UsernameNotFoundException("user not active",null);
				}
				
			}
		}
	}
	}
	return out;
}
}
