
package com.spantag.socialMediaAppln.uploadMedia;

import java.io.Serializable;
import javax.persistence.Temporal;
import java.sql.Time;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "sm_file_upload")
public class SMFile implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)/*, generator = "userRegisterSeq")
	@SequenceGenerator(name = "userRegisterSeq", sequenceName = "ATU_SEQ_ID", allocationSize = 1)*/
	@Column(name = "sm_fileid")
	public long fileid;

	@Column(name = "sm_filename")
	public String filename;

	@Column(name = "sm_fileurl")
	public String fileurl;
	
	@Column(name = "sm_created_userid")
	public String userid;
	
	@Column(name = "sm_file_createdat")
	public String createdAt;
	
	/* @Temporal(javax.persistence.TemporalType.TIMESTAMP)
     @Column(name="sm_file_createdat",nullable=false, columnDefinition="DATETIME")
     private Date createdAt = new Time(new java.util.Date().getTime());*/

	@Column(name = "sm_filetype")
	public String type;
	
	@Column(name = "sm_filecontenttype")
	public String filecontenttype;
	
	@Column(name = "sm_status")
	 public int status;
	
	@Column(name = "sm_file_createdby")
	private String createdby;
	
	@Column(name = "sm_file_from")
	private String fileFrom;
	
	@Column(name = "sm_isprocessing_completed")
	private int isprocesscompleted;
	
	@Column(name = "sm_file_language")
	 public int langid;
	
	
public int getLangid() {
		return langid;
	}

	public void setLangid(int langid) {
		this.langid = langid;
	}
	
	
	public int getIsprocesscompleted() {
		return isprocesscompleted;
	}

	public void setIsprocesscompleted(int isprocesscompleted) {
		this.isprocesscompleted = isprocesscompleted;
	}

	public String getFileFrom() {
		return fileFrom;
	}

	public void setFileFrom(String fileFrom) {
		this.fileFrom = fileFrom;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	
	

	@Column(name = "sm_file_poster")
	public String filePoster;
	
	@Column(name = "sm_file_caption")
	public String filecaption;
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getFilecaption() {
		return filecaption;
	}

	public void setFilecaption(String filecaption) {
		this.filecaption = filecaption;
	}

	public String getFilePoster() {
		return filePoster;
	}

	public void setFilePoster(String filePoster) {
		this.filePoster = filePoster;
	}

	public String getFilecontenttype() {
		return filecontenttype;
	}

	public void setFilecontenttype(String filecontenttype) {
		this.filecontenttype = filecontenttype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getFileid() {
		return fileid;
	}

	public void setFileid(long fileid) {
		this.fileid = fileid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	


	
	
}
