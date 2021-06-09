package com.spantag.socialMediaAppln.uploadMedia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "sm_video_likes")
public class videolikes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sm_like_id")
	public int likeid;
	
	@Column(name = "sm_video_id")
	public int videoid;
	
	@Column(name = "sm_user_id")
	public String userid;
	
	@Column(name = "sm_total_likes")
	public int totallikes;

	@Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
	private String createdAt ;
	
	@Column(name = "sm_like_createdby")
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

	public int getLikeid() {
		return likeid;
	}

	public void setLikeid(int likeid) {
		this.likeid = likeid;
	}

	public int getVideoid() {
		return videoid;
	}

	public void setVideoid(int videoid) {
		this.videoid = videoid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getTotallikes() {
		return totallikes;
	}

	public void setTotallikes(int totallikes) {
		this.totallikes = totallikes;
	}
	


}
