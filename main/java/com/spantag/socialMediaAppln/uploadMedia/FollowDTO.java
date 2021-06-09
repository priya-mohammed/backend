package com.spantag.socialMediaAppln.uploadMedia;

public class FollowDTO {

	private long seqId;
	private int userId;
	private int followingId;
	private int status;
	private String createdAt;
	private int acceptRequest;
	private String notifyId;
	
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public int getAcceptRequest() {
		return acceptRequest;
	}
	public void setAcceptRequest(int acceptRequest) {
		this.acceptRequest = acceptRequest;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFollowingId() {
		return followingId;
	}
	public void setFollowingId(int followingId) {
		this.followingId = followingId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
