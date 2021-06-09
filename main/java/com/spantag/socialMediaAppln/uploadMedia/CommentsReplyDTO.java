package com.spantag.socialMediaAppln.uploadMedia;

import java.util.Date;

public class CommentsReplyDTO {

	private long replyId;
	private int videoId;
	private int commentsId;
	private String reply;
	private int userId;
	private String createdAt;
	public String cmtlike;
	
	private int commentId;
	private String oprn;

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCmtlike() {
		return cmtlike;
	}

	public void setCmtlike(String cmtlike) {
		this.cmtlike = cmtlike;
	}
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	public int getCommentsId() {
		return commentsId;
	}
	public void setCommentsId(int commentsId) {
		this.commentsId = commentsId;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
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

	public String getOprn() {
		return oprn;
	}

	public void setOprn(String oprn) {
		this.oprn = oprn;
	}
	
}
