package com.spantag.socialMediaAppln.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spantag.socialMediaAppln.login.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {


	List<Notification> findByNotifyUserIdAndNotificationTypeAndReadStatus(
			int userid, String string, int i);

	List<Notification> findByNotifyUserIdAndReadStatus(int userid, int i);

	Optional<Notification> findByNotifyId(long notifyId);

}
