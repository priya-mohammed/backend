package com.spantag.socialMediaAppln.notification;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService {
	public Map<String, Object> getNotificationCount(int userid);

	public Map<String, Object> pushNotification(int userid);

	public Map<String, Object> getfollowerNotification(int userid);

	public Map<String, Object> getlikesNotification(int userid);
	


	public Map<String, Object> getcommentsNotification(int userid);

	public Map<String, Object> getAllNotificationCount(int userid);

	public Map<String, Object> readnotify(NotificationDTO details);
	
	public Map<String, Object> samplePushNotification(String apktoken,HttpServletRequest httpRequest);

	public Map<String, Object> tagPushNotification(NotificationDTO details);
}
