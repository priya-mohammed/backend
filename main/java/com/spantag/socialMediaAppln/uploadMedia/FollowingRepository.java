package com.spantag.socialMediaAppln.uploadMedia;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface FollowingRepository extends JpaRepository<Follow, String>{

	Optional<Follow> findBySeqId(Long valueOf);
	@Transactional
	void removeByUserIdAndFollowingId(Integer valueOf, Integer valueOf2);

	Optional<Follow> findByUserIdAndFollowingId(Integer valueOf,
			Integer valueOf2);
	

}
