package com.spantag.socialMediaAppln.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_notification")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)/*, generator = "userRegisterSeq")
	@SequenceGenerator(name = "userRegisterSeq", sequenceName = "ATU_SEQ_ID", allocationSize = 1)*/
	@Column(name = "sm_notify_id")
	public long notifyId;

	@Column(name = "sm_user_id")
	public String userId;
	
	@Column(name = "sm_notification_type")
	public String notificationType;
	
	@Column(name = "sm_notification_file_id")
	public String notifyFileId;
	
	@Column(name = "sm_notification_read_status")
	public int readStatus;
	
	@Column(name = "sm_notify_user_id")
	public int notifyUserId;
	
	@Column(name="sm_notify_createdat",nullable=false, columnDefinition="DATETIME")
	private String createdAt ;
	
	@Column(name = "sm_notify_createdby")
	private String createdby;

	public String getCreatedby() {
		return createdby;
	}
	
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	public String getCreatedAt() {
	return createdAt;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotifyFileId() {
		return notifyFileId;
	}

	public void setNotifyFileId(String notifyFileId) {
		this.notifyFileId = notifyFileId;
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}
	
}
