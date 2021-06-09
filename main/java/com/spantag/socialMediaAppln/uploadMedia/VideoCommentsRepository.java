package com.spantag.socialMediaAppln.uploadMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spantag.socialMediaAppln.login.User;
import org.springframework.data.jpa.repository.Modifying;
import javax.transaction.Transactional;

@Repository
public interface VideoCommentsRepository extends JpaRepository<VideoComments, String>{
	CommentsReply save(CommentsReply inp);
	
	@Modifying    
	@Transactional	
	VideoComments deleteByCommentId(Long commentId);
}
