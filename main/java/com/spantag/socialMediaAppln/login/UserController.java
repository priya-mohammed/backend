package com.spantag.socialMediaAppln.login;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import com.spantag.socialMediaAppln.uploadMedia.FollowDTO;
import com.spantag.socialMediaAppln.uploadMedia.SMFileDTO;
import com.spantag.socialMediaAppln.utils.ResponseInfo;
import com.spantag.socialMediaAppln.utils.commonUtils;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.spantag.socialMediaAppln.utils.ResponseInfo.ResponseType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "USER MODULE", description = "Operations pertaining that user can signup,sigin,changing passwords etc..with this module")
public class UserController {

	

	@Autowired
	UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MessageSource messageSource;

	ObjectMapper obj = new ObjectMapper();

	ModelMapper mapper = new ModelMapper();

	ResponseEntity<Object> response = null;

	@PostMapping("/signin")
	@ApiOperation(value = "user can Login if they are already registered with this app")
	public String login(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.signIn(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	
	@PostMapping("/socialLogin")
	@ApiOperation(value = "user can Login if they are already registered with this app")
	public String socialLogin(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.socialLogin(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	
	@PostMapping("/fbloginwithoutemail")
	@ApiOperation(value = "user can Login if they are already registered with this app")
	public String fbloginwithoutemail(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.fbloginwithoutemail(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	
	@PostMapping("/forgotPassword")
	@ApiOperation(value = "user can reenter password if password forgetted")
	public String forgotPassword(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.forgotPassword(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	

	@PostMapping("/updateProfilewithstatus")
	@ApiOperation(value = "user can reenter password if password forgetted")
	public String updateProfilewithstatus(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.updateProfilewithstatus(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	
	@PostMapping("/verifyOtp")
	@ApiOperation(value = "verify otp if it is valid user or not")
	public String verifyOtp(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.verifyOtp(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
	
	@PostMapping("/signup")
	@ApiOperation(value = "user can register with this app")
	public String signup(@RequestBody SignUpRequest userInput) throws Exception {
		System.out.println("signup");
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.signUp(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@PostMapping("/RegisterMobileNo")
	@ApiOperation(value = "user can register with this app")
	public String RegisterMobileNo(@RequestBody SignUpRequest userInput) throws Exception {
		System.out.println("signup");
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.RegisterMobileNo(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@PostMapping("/updateProfile")
	@ApiOperation(value = "user can update our personal information.")
	public String updateProfile(@RequestBody SignUpRequest userInput,HttpServletRequest httpRequest) throws Exception {
		
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.updateProfile(signIn,httpRequest);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}

	@PostMapping("/resendOtp")
	@ApiOperation(value = "user can register with this app")
	public String resendOtp(@RequestBody SignUpRequest userInput) throws Exception {
		System.out.println("signup");
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.resendOtp(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}

	
	@GetMapping("/getdashboarddetails")
	@ApiOperation(value = "list dashboard details")
	public ResponseEntity<Object> getdashboarddetails() throws Exception {
		Map<String, Object> out = userService.getdashboarddetails();
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}

	@PutMapping("/changepassword")
	@ApiOperation(value = "user can Change their password")
	public ResponseEntity<Object> changePassword(@RequestBody ResetPasswordRequest userInput) throws Exception {

		// log.debug("SignIn Process--------------" +
		// obj.writeValueAsString(userInput));

		ResetPasswordDTO pwdetails = mapper.map(userInput, ResetPasswordDTO.class);
		Map<String, Object> output = userService.changePassword(pwdetails);

		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);

		return response;

	}
	
	@GetMapping("/getUserDetailsById/{userId}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> getUserDetailsById(@PathVariable("userId") String userId) throws Exception {
		Map<String, Object> out = userService.getUserDetailsById(userId);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}

	@GetMapping("/loaduser")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> loaduser() throws Exception {
		Map<String, Object> out = userService.loaduser();
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@PostMapping("/sendInvitationLink")
	@ApiOperation(value = "user can register with this app")
	public String sendInvitationLink(@RequestBody SignUpRequest userInput) throws Exception {
		System.out.println("signup");
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.sendInvitationLink(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@PostMapping("/changeuserpassword")
	@ApiOperation(value = "user can update our personal information.")
	public String changeuserpassword(@RequestBody SignUpRequest userInput,HttpServletRequest httpRequest) throws Exception {
		
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		
		Map<String, Object> output = userService.changeuserpassword(signIn,httpRequest);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@GetMapping("/SearchPeople/{userId}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> SearchPeople(@PathVariable("userId") String userId) throws Exception {
		List<SignUpDTO> out = userService.SearchPeople(userId);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@GetMapping("/SearchByname/{name}/{userid}/{length}")
	@ApiOperation(value = "get user By Name")
	public ResponseEntity<Object> SearchByname(@PathVariable("name") String name,
			@PathVariable("userid") String userid,@PathVariable("length") String length) throws Exception {
		List<SignUpDTO> out = userService.SearchByname(name,userid,length);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@PostMapping("/saveNewUser")
	@ApiOperation(value = "user can register with this app")
	public String saveNewUser(@RequestBody SignUpRequest userInput) throws Exception {
		System.out.println("signup");
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.saveNewUser(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@GetMapping("/getLimitedusersList/{userId}/{loginid}")
	@ApiOperation(value = "Limited user details")
	public ResponseEntity<Object> getLimitedusersList(@PathVariable("userId") int userId,@PathVariable("loginid") String loginid) throws Exception {
		List<SignUpDTO> out = userService.getLimitedusersList(userId,loginid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	


 	@GetMapping("/getAdminUserList/{length}")
	@ApiOperation(value = "Limited user details")
	public ResponseEntity<Object> getAdminUserList(@PathVariable("length") String length) throws Exception {
		List<SignUpDTO> out = userService.getAdminUserList(length);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@GetMapping("/getAdminvideoList/{userid}/{length}")
	@ApiOperation(value = "Limited user details")
	public ResponseEntity<Object> getAdminvideoList(@PathVariable("userid") String userid,@PathVariable("length") String length) throws Exception {
		List<SMFileDTO> out = userService.getAdminvideoList(userid,length);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	
	@GetMapping("/AdminSearchByFilecaption/{name}/{length}/{userid}")
	@ApiOperation(value = "get user By Name")
	public ResponseEntity<Object> AdminSearchByFilecaption(@PathVariable("name") String name,
			@PathVariable("length") String length,@PathVariable("userid") String userid) throws Exception {
		List<SMFileDTO> out = userService.AdminSearchByFilecaption(name,length,userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@PostMapping("/blockFromAdmin")
	@ApiOperation(value =" add for the particular video")
	public String blockFromAdmin(@RequestBody SignUpDTO input) throws Exception {
		
		System.out.println("input.blockFromAdmin()    "+input.getFiletype());
		System.out.println("input.blockFromAdmin()      "+input.getUserid());
	 
		
		Map<String, Object> output = userService.blockFromAdmin(input);
		response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	
	@GetMapping("/AdminSearchByname/{name}/{length}")
	@ApiOperation(value = "get user By Name")
	public ResponseEntity<Object> AdminSearchByname(@PathVariable("name") String name,
			@PathVariable("length") String length) throws Exception {
		List<SignUpDTO> out = userService.AdminSearchByname(name,length);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
   	@GetMapping("/getAdminDashboardDetails")
	@ApiOperation(value = "list dashboard details")
	public ResponseEntity<Object> getAdminDashboardDetails() throws Exception {
		Map<String, Object> out = userService.getAdminDashboardDetails();
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	@PostMapping("/Adminsignin")
	@ApiOperation(value = "user can Login if they are already registered with this app")
	public String Adminsignin(@RequestBody SignUpRequest userInput) throws Exception {
		SignUpDTO signIn = mapper.map(userInput, SignUpDTO.class);
		Map<String, Object> output = userService.Adminsignin(signIn);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("output  "+jsonString);
		return jsonString;
	}
}
