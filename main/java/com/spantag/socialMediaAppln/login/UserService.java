
package com.spantag.socialMediaAppln.login;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.boot.configurationprocessor.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spantag.socialMediaAppln.uploadMedia.SMFileDTO;

public interface UserService {

    public Map<String, Object> signIn(@Valid SignUpDTO userInput) throws JsonProcessingException,ParseException, ParseException;
    public Map<String, Object> signUp(@Valid SignUpDTO userInput) throws JsonProcessingException,ParseException, ParseException;
	public Map<String, Object> getdashboarddetails();
	public Map<String, Object> changePassword(@Valid ResetPasswordDTO userInput);
	public Map<String, Object> forgotPassword(SignUpDTO signIn);
	public Map<String, Object> verifyOtp(SignUpDTO signIn);
	public Map<String, Object> resendOtp(SignUpDTO signIn);
	public Map<String, Object> updateProfile(SignUpDTO signIn,HttpServletRequest httpRequest);
	public Map<String, Object> getUserDetailsById(String userId);
	public Map<String, Object> RegisterMobileNo(SignUpDTO signIn);
	public Map<String, Object> socialLogin(SignUpDTO signIn);
	public Map<String, Object> loaduser();
	public Map<String, Object> sendInvitationLink(SignUpDTO signIn);
	public Map<String, Object> changeuserpassword(SignUpDTO signIn,HttpServletRequest httpRequest);
	public List<SignUpDTO> SearchPeople(String userId);
	public List<SignUpDTO> SearchByname(String name,String userid, String length);
	public Map<String, Object> saveNewUser(SignUpDTO signIn);
	public List<SignUpDTO> getLimitedusersList(int userId,String loginid);
	public Map<String, Object> fbloginwithoutemail(SignUpDTO signIn);
	public Map<String, Object> updateProfilewithstatus(SignUpDTO signIn);
	
	public List<SignUpDTO> getAdminUserList(String length);
public List<SMFileDTO> getAdminvideoList(String userid,String length);
public Map<String, Object> blockFromAdmin(SignUpDTO input);
public List<SignUpDTO> AdminSearchByname(String name, String length);
public List<SMFileDTO> AdminSearchByFilecaption(String name, String length,String userid);
public Map<String, Object> getAdminDashboardDetails();
public Map<String, Object> Adminsignin(@Valid SignUpDTO userInput) throws JsonProcessingException,ParseException, ParseException;
}
