package com.spantag.socialMediaAppln.uploadMedia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spantag.socialMediaAppln.login.User;

@Repository
public interface VideoLikesRepository extends JpaRepository<videolikes, String>{
	List<videolikes> findByVideoidAndUserid(int videoid,String userid);
}