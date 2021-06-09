package com.spantag.socialMediaAppln.uploadMedia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_followers")
public class Follow implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)/*, generator = "userRegisterSeq")
	@SequenceGenerator(name = "userRegisterSeq", sequenceName = "ATU_SEQ_ID", allocationSize = 1)*/
	@Column(name = "sm_id")
	private long seqId;
	
	@Column(name = "sm_user_id")
	private int userId;
	
	@Column(name = "sm_following_id")
	private int followingId;
	
	@Column(name = "sm_status")
	private int status;
	
	@Column(name="sm_createdat",nullable=false, columnDefinition="DATETIME")
	private String createdAt ;

	@Column(name = "sm_follow_createdby")
	private String createdby;

	@Column(name = "sm_accept_request")
	private int acceptRequest;
	
	@Column(name = "sm_block_condition")
	private String blockCondition;
	
	public String getBlockCondition() {
		return blockCondition;
	}
	public void setBlockCondition(String blockCondition) {
		this.blockCondition = blockCondition;
	}
	public int getAcceptRequest() {
		return acceptRequest;
	}
	public void setAcceptRequest(int acceptRequest) {
		this.acceptRequest = acceptRequest;
	}
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
