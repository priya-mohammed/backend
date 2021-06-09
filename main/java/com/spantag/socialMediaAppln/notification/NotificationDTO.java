package com.spantag.socialMediaAppln.notification;

public class NotificationDTO {

	public long notifyId;
	public int userId;
	public String notificationType;
	public int notifyFileId;
	public int readStatus;
	public int notifyUserId;
	public String tagId;
	
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public int getNotifyUserId() {
		return notifyUserId;
	}
	public void setNotifyUserId(int notifyUserId) {
		this.notifyUserId = notifyUserId;
	}
	public long getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(long notifyId) {
		this.notifyId = notifyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public int getNotifyFileId() {
		return notifyFileId;
	}
	public void setNotifyFileId(int notifyFileId) {
		this.notifyFileId = notifyFileId;
	}
	public int getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}
	
	
}
