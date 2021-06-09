
package com.spantag.socialMediaAppln.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spantag.socialMediaAppln.login.User;

import java.lang.String;

@Repository
public interface UserRegisterRepository extends JpaRepository<User, String> {

	Optional<User> findByUsername(String userName);
	Optional<User> findByUserid(long userid);	
	Optional<User>	findByPasswordAndEmail(String password, String userName);
	Optional<User> findByEmail(String emailId);
	Optional<User> findByEmailAndPhoneNo(String email, String phoneNo);
	Optional<User> findByEmailAndUseridNot(String email, long userid);
	Optional<User> findByPhoneNo(String phoneNo);
	
}
