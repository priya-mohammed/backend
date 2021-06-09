
package com.spantag.socialMediaAppln.login;

public class ResetPasswordDTO {
	
	public int userid;

	public String userName;

	public String currentPassword;

	public String newPassword;

	public String confirmPassword;

	

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUserName() {

		return userName;
	}

	public void setUserName(String userName) {

		this.userName = userName;
	}

	public String getCurrentPassword() {

		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {

		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {

		return newPassword;
	}

	public void setNewPassword(String newPassword) {

		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {

		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {

		this.confirmPassword = confirmPassword;
	}

}
