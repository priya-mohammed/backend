package com.spantag.socialMediaAppln.uploadMedia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;


@Repository
public interface CommentsReplyRepository extends JpaRepository<CommentsReply, String>{
	List<CommentsReply> findByVideoIdAndUserIdAndCommentId(int videoId,int userId, int commentId);
	
	@Modifying    
	@Transactional	
	CommentsReply deleteByReplyId(Long replyId);
}
