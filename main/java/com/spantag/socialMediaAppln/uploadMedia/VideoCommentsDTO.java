package com.spantag.socialMediaAppln.uploadMedia;

import java.util.Date;

public class VideoCommentsDTO {
	private long commentId;
	private int videoId;
	private String comments;
	private int userId;
	private String createdAt;
	private int gifId;
	private String oprn;
	private String jsxString;
	private String commentType;
	
	
	public String getCommentType() {
		return commentType;
	}
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	public String getJsxString() {
		return jsxString;
	}
	public void setJsxString(String jsxString) {
		this.jsxString = jsxString;
	}
	public String getOprn() {
		return oprn;
	}
	public void setOprn(String oprn) {
		this.oprn = oprn;
	}
	public int getGifId() {
		return gifId;
	}
	public void setGifId(int gifId) {
		this.gifId = gifId;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
