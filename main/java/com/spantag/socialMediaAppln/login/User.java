
package com.spantag.socialMediaAppln.login;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "sm_users")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)/*, generator = "userRegisterSeq")
	@SequenceGenerator(name = "userRegisterSeq", sequenceName = "ATU_SEQ_ID", allocationSize = 1)*/
	@Column(name = "sm_userid")
	public long userid;

	@Column(name = "sm_username")
	public String username;

	@Column(name = "sm_userpassword")
	public String password;
	
	@Column(name = "sm_useremailid")
	public String email;
	
	@Column(name = "sm_userstatus")
	public String userstatus;
	
	@Column(name = "sm_userphone")
	private String phoneNo;
	
	@Column(name = "sm_sms_otp")
	private String otp;
	
	@Column(name = "sm_profile_path")
	private String profilePath;
	
	@Column(name = "sm_full_name")
	private String fullName;
	
	@Column(name = "sm_handle_name")
	private String handleName;
	
	@Column(name = "sm_dob")
	private String dob;
	
	@Column(name = "sm_age")
	private int age;
	
	@Column(name = "sm_about")
	private String about;

	@Column(name = "sm_gender")
	private String gender;

	@Column(name = "sm_profile_content_type")
	   private String filetype;
	
	@Column(name = "sm_diingdong_id")
	private String diingdongId;
	
	@Column(name = "sm_account")
	private String accountStatus;
		
	
	@Column(name="sm_user_createdat",nullable=false, columnDefinition="DATETIME")
	private String createdAt ;
	
	@Column(name = "sm_apk_token")
	private String apkToken;
	
	@Column(name = "sm_blocked_ids")
	private String blockIds;
	
	@Column(name = "sm_role")
	private String role;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getBlockIds() {
		return blockIds;
	}

	public void setBlockIds(String blockIds) {
		this.blockIds = blockIds;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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
	
		public String getDiingdongId() {
		return diingdongId;
	}

	public void setDiingdongId(String diingdongId) {
		this.diingdongId = diingdongId;
	}

		public String getFiletype() {
			return filetype;
		}

		public void setFiletype(String filetype) {
			this.filetype = filetype;
		}

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


	public String getUserstatus() {
		return userstatus;
	}


	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
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

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	
	
}
