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
@Table(name = "sm_video_comments")
public class VideoComments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sm_comment_id")
	public long commentId;
	
	@Column(name = "sm_video_id")
	public int videoId;
	
	@Column(name = "sm_comments")
	public String comments;
	
	@Column(name = "sm_created_userid")
	public int userId;
	
	@Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
	public String createdAt ;
	
	 /* @Temporal(javax.persistence.TemporalType.TIMESTAMP)
      @Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
      private Date createdAt = new Time(new java.util.Date().getTime());*/
	

	@Column(name = "sm_gif_id")
	public int gifId;
	
	@Column(name = "sm_comments_jsxstring")
	public String jsxString;
	
	@Column(name = "sm_comments_type")
	public String commentType;
	
	
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "sm_comments_createdby")
	private String createdby;

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

	//	public String cmtlike;
//
//	public String getCmtlike() {
//		return cmtlike;
//	}
//
//	public void setCmtlike(String cmtlike) {
//		this.cmtlike = cmtlike;
//	}
//	

	
	
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

	

}
