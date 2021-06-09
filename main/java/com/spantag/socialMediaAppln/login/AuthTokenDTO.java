
package com.spantag.socialMediaAppln.login;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonSerialize(JsonInclude.Include.NON_NULL)
public class AuthTokenDTO {

	private String token;
	// @JsonIgnore
	private long otpId;

	private long userId;

	private String userName;

	private String userType;
	// @JsonIgnore
	private int otp;

	private String message;

	private String password;
	private String lastName;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date loggedInDate;

	private String mobileNo;

	private String email;

	private String firstName;

	private String menuRights;

	private String customerCode;

	public int getOtp() {
		return otp;
	}

	/*
	 * public long getUserId() { return userId; }
	 * 
	 * public void setUserId(long userId) { this.userId = userId; }
	 */

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getCustomerCode() {

		return customerCode;
	}

	public void setCustomerCode(String customerCode) {

		this.customerCode = customerCode;
	}

	public String getFirstName() {

		return firstName;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public AuthTokenDTO() {

	}

	public String getUserName() {

		return userName;
	}

	public void setUserName(String userName) {

		this.userName = userName;
	}

	public String getUserType() {

		return userType;
	}

	public void setUserType(String userType) {

		this.userType = userType;
	}

	public Date getLoggedInDate() {

		return loggedInDate;
	}

	public void setLoggedInDate(Date loggedInDate) {

		this.loggedInDate = loggedInDate;
	}

	public String getMobileNo() {

		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {

		this.mobileNo = mobileNo;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getMenuRights() {

		return menuRights;
	}

	public void setMenuRights(String menuRights) {

		this.menuRights = menuRights;
	}

	public long getOtpId() {
		return otpId;
	}

	public void setOtpId(long otpId) {
		this.otpId = otpId;
	}

	public AuthTokenDTO(String token) {

		this.token = token;
	}

	public String getToken() {

		return token;
	}

	public void setToken(String token) {

		this.token = token;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
