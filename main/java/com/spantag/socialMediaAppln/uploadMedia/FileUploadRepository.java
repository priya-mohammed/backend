
package com.spantag.socialMediaAppln.uploadMedia;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import java.lang.String;

@Repository
public interface FileUploadRepository extends JpaRepository<SMFile, String> {
	Optional<SMFile> findByFileid(long fileId);
	List<SMFile> findByUseridOrderByFileidDesc(String userid);
	List<SMFile> findAllOrderByFileid(Long fileid);
	List<SMFile> findAllByOrderByFileidDesc();
	
	@Modifying    
	@Transactional
	SMFile deleteByFileid(long fileid);
	
	List<SMFile> findByUseridAndStatusOrderByFileidDesc(String userid,int status);
	List<SMFile> findByUseridAndIsprocesscompleted(String userid, int string);
	
}
