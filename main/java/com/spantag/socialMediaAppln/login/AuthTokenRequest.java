
package com.spantag.socialMediaAppln.login;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuthTokenRequest {

	private String token;

	private long userId;

	private String userName;

	private String userType;

	private String message;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date loggedInDate;

	private long mobileNo;

	private String email;

	private String firstName;

	private String menuRights;

	private String customerCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public AuthTokenRequest() {

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

	public long getMobileNo() {

		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {

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

	public long getUserId() {

		return userId;
	}

	public void setUserId(long userId) {

		this.userId = userId;
	}

	public AuthTokenRequest(String token) {

		this.token = token;
	}

	public String getToken() {

		return token;
	}

	public void setToken(String token) {

		this.token = token;
	}

}
