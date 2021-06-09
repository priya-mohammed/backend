
package com.spantag.socialMediaAppln.login;

import java.util.Date;

import javax.persistence.Column;

public class SignUpDTO {

	public long userid;

	public String username;

	public String password;
	
	public String email;

	private String phoneNo;
	
	private String otp;
	
	private String profilePath;
	
	private String fullName;
	
	private String handleName;
	
	private String dob;
	
	private String fileName;
	
	private String filetype;
	
	public String newPassword;
	
	private String followcount;
	
	private String usercount;
	
	private String createdAt ;
	
	private String apkToken;
	
	private String countrycode;
	
	private String acceptRequest;
	
	private String diingdongid;
	
	private String userstatus;
	
	private String totalpost;
	
	private String intstatus;
	
	private String status;
	
    private String role;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getTotalpost() {
		return totalpost;
	}

	public void setTotalpost(String totalpost) {
		this.totalpost = totalpost;
	}

	public String getIntstatus() {
		return intstatus;
	}

	public void setIntstatus(String intstatus) {
		this.intstatus = intstatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}
	
	
	
	public String getDiingdongid() {
		return diingdongid;
	}

	public void setDiingdongid(String diingdongid) {
		this.diingdongid = diingdongid;
	}

	public String getAcceptRequest() {
		return acceptRequest;
	}

	public void setAcceptRequest(String acceptRequest) {
		this.acceptRequest = acceptRequest;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	private String accountStatus;
	
	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getApkToken() {
		return apkToken;
	}

	public void setApkToken(String apkToken) {
		this.apkToken = apkToken;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getUsercount() {
		return usercount;
	}

	public void setUsercount(String usercount) {
		this.usercount = usercount;
	}
	
	public String getFollowcount() {
		return followcount;
	}

	public void setFollowcount(String followcount) {
		this.followcount = followcount;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	private String imageurl;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private int age;
	
	private String about;
	
	private String gender;
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProfilePath() {
		return profilePath;
	}

	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getHandleName() {
		return handleName;
	}

	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
 

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
}
