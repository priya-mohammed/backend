package com.spantag.socialMediaAppln.uploadMedia;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "sm_video_comments_reply")
public class CommentsReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sm_reply_id")
	private long replyId;
	
	@Column(name = "sm_video_id")
	private int videoId;
	
	@Column(name = "sm_comments_id")
	private int commentId;
	
	@Column(name = "sm_reply")
	private String reply;
	
	@Column(name = "sm_created_userid")
	private int userId;
	
	@Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
	private String createdAt ;
	
	/*@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
    private Date createdAt = new Time(new java.util.Date().getTime());*/
	
	@Column(name = "sm_comment_likes")
	public int cmtlike;
	
	@Column(name = "sm_gif_id")
	public int gifId;

	@Column(name = "sm_reply_createdby")
	private String createdby;
	
	@Column(name = "sm_reply_jsxstring")
	public String jsxString;
	
	@Column(name = "sm_reply_type")
	public String commentType;

	public String getJsxString() {
		return jsxString;
	}

	public void setJsxString(String jsxString) {
		this.jsxString = jsxString;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedby() {
		return createdby;
	}
	
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public int getGifId() {
		return gifId;
	}

	public void setGifId(int gifId) {
		this.gifId = gifId;
	}

	public int getCmtlike() {
		return cmtlike;
	}

	public void setCmtlike(int cmtlike) {
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

	
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
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

	
	
	
}
