package com.spantag.socialMediaAppln.notification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.ast.HasAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.spantag.socialMediaAppln.login.UserRegisterRepository;
import com.spantag.socialMediaAppln.uploadMedia.SMFileDTO;
import com.spantag.socialMediaAppln.utils.ConnUtil;
import com.spantag.socialMediaAppln.utils.commonUtils;


@Service(value = "notificationService")
@Configuration
@PropertySource("classpath:SocialMediaApplnQueryList.properties")
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ConnUtil connutil;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private NotificationRepository notifyRepo;
	
	@Override
	public Map<String, Object> getNotificationCount(int userid) {
		
	List<Notification> followcount =notifyRepo.findByNotifyUserIdAndNotificationTypeAndReadStatus(userid,"follow",0);
	List<Notification> likescount =notifyRepo.findByNotifyUserIdAndNotificationTypeAndReadStatus(userid,"like",0);
	List<Notification> commentscount =notifyRepo.findByNotifyUserIdAndNotificationTypeAndReadStatus(userid,"comment",0);
	List<Notification> replycount =notifyRepo.findByNotifyUserIdAndNotificationTypeAndReadStatus(userid,"reply",0);

		 
		 Map<String, Object> out = new LinkedHashMap<>();
		 out.put("followcount", followcount.size());
		 out.put("likecount", likescount.size());

		 out.put("commentscount", commentscount.size()+replycount.size());
		return out;
	}

	@Override
	public Map<String, Object> pushNotification(int userid) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	 
		String date = format.format(new Date());
		String Query= env.getProperty("notificationFollowQuery");
		List<Object[]> followlist = connutil.executequerybyid(em, Query, userid);
		if(followlist.size() > 0){
		 for (Object[] object : followlist) {
			 Notification follow =new Notification();
			 follow.setNotificationType("follow");
			 follow.setNotifyFileId(String.valueOf(object[0]));
			 follow.setReadStatus(0);
			 follow.setUserId(String.valueOf(object[1]));
			 follow.setCreatedAt(date);
			 follow.setNotifyUserId(userid);
			 follow.setCreatedby(String.valueOf(userid));
			 notifyRepo.save(follow);
		 }
		}
		
		String commentQuery= env.getProperty("notificationCommentQuery");
		List<Object[]> commentlist = connutil.executequerybyid(em, commentQuery, userid);
		if(commentlist.size() > 0){
		 for (Object[] object : commentlist) {
			 Notification follow =new Notification();
			 follow.setNotificationType(String.valueOf(object[2]));
			 follow.setNotifyFileId(String.valueOf(object[0]));
			 follow.setReadStatus(0);
			 follow.setUserId(String.valueOf(object[1]));
			 follow.setCreatedAt(date);
			 follow.setCreatedby(String.valueOf(userid));
			 if(String.valueOf(object[2]).equals("reply")) {
				 String id = String.valueOf(object[1]);
				 follow.setNotifyUserId(Integer.valueOf(id));
			 }else {
				 follow.setNotifyUserId(userid);
			 }
			
			 notifyRepo.save(follow);
		 }
		}
		
		String likeQuery= env.getProperty("notificationLikeQuery");
		List<Object[]> likelist = connutil.executequerybyid(em, likeQuery, userid);
		if(likelist.size() > 0){
		 for (Object[] object : likelist) {
			 Notification follow =new Notification();
			 follow.setNotificationType("like");
			 follow.setNotifyFileId(String.valueOf(object[0]));
			 follow.setReadStatus(0);
			 follow.setUserId(String.valueOf(object[1]));
			 follow.setNotifyUserId(userid);
			 follow.setCreatedAt(date);
			 follow.setCreatedby(String.valueOf(userid));
			 notifyRepo.save(follow);
		 }
		}
		
		
		return null;
	}

	@Override
	public Map<String, Object> getfollowerNotification(int userid) {
		String Query= env.getProperty("notificationFollowDetailQuery");
		List<Object[]> list = connutil.executequerybyid(em, Query, userid);
		List<Object> List = new ArrayList();
		if(list.size() > 0){
		 for (Object[] object : list) {
			 Map<String, Object> out = new LinkedHashMap<>();
			 out.put("userid", String.valueOf(object[0]));
			 out.put("username", String.valueOf(object[1]));
			 out.put("profilepath", String.valueOf(object[2]));
			 out.put("readstatus", String.valueOf(object[3]));
			 out.put("duration", String.valueOf(object[4]));
			 out.put("timedesc", String.valueOf(object[5]));
			 out.put("notifyid", String.valueOf(object[6]));
			 out.put("duration", connutil.datecalculation(String.valueOf(object[7]),String.valueOf(object[8]) ) );
			 out.put("timedesc", connutil.getTimedesc(String.valueOf(object[7]),String.valueOf(object[8]) ));
			 out.put("acceptrequest", String.valueOf(object[9]));
			 List.add(out);
		 }
		}
		 Map<String, Object> out = new LinkedHashMap<>();
		 out.put("List", List);
		return out;
	}

	@Override
	public Map<String, Object> getlikesNotification(int userid) {
	
		String Query= env.getProperty("notificationLikesDetailQuery");
		List<Object[]> list = connutil.executequerybyid(em, Query, userid);
		List<Object> List = new ArrayList();
		if(list.size() > 0){
		 for (Object[] object : list) {
			 Map<String, Object> out = new LinkedHashMap<>();
			 out.put("userid", String.valueOf(object[0]));
			 out.put("username", String.valueOf(object[1]));
			 out.put("profilepath", String.valueOf(object[2]));
			 out.put("readstatus", String.valueOf(object[3]));
			 out.put("duration", String.valueOf(object[4]));
			 out.put("timedesc", String.valueOf(object[5]));
			 out.put("notifyid", String.valueOf(object[6]));
			 out.put("fileid", String.valueOf(object[7]));
			 out.put("filetype", String.valueOf(object[8]));
			 List.add(out);
		 }
		}
		 Map<String, Object> out = new LinkedHashMap<>();
		 out.put("List", List);
		return out;
	}
	
	
	@Override
	public Map<String, Object> getcommentsNotification(int userid) {
		
		String Query= env.getProperty("notificationcommentDetailQuery");
		List<Object[]> list = connutil.executequerybyid(em, Query, userid);
		List<Object> List = new ArrayList();
		if(list.size() > 0){
		 for (Object[] object : list) {
			 Map<String, Object> out = new LinkedHashMap<>();
			 out.put("userid", String.valueOf(object[0]));
			 out.put("username", String.valueOf(object[1]));
			 out.put("profilepath", String.valueOf(object[2]));
			 out.put("readstatus", String.valueOf(object[3]));
			/* out.put("duration", String.valueOf(object[4]));
			 out.put("timedesc", String.valueOf(object[5]));*/
			 out.put("duration", connutil.datecalculation(String.valueOf(object[13]),String.valueOf(object[14])));
			 out.put("timedesc", connutil.getTimedesc(String.valueOf(object[13]),String.valueOf(object[14])));
			 out.put("notifyid", String.valueOf(object[6]));
			 out.put("fileid", String.valueOf(object[7]));
			 out.put("filetype", String.valueOf(object[8]));
			 out.put("commentdesc", connutil.decodeStringUrl(String.valueOf(object[9])));
			 out.put("notifytype", String.valueOf(object[10]));
			 out.put("commentid", String.valueOf(object[11]));
			 out.put("replyid", String.valueOf(object[12]));
			 out.put("commentType", String.valueOf(object[15]));
			 out.put("jsxString", String.valueOf(object[16]));
			 List.add(out);
		 }
		}
		 Map<String, Object> out = new LinkedHashMap<>();
		 out.put("List", List);
		return out;
	}

	@Override
	public Map<String, Object> getAllNotificationCount(int userid) {
		List<Notification> count =notifyRepo.findByNotifyUserIdAndReadStatus(userid,0);
		
			 Map<String, Object> out = new LinkedHashMap<>();
			 out.put("allcount", count.size());
			
			
			return out;
	}
	
	@Override
	public Map<String, Object> readnotify(NotificationDTO details) {
		if(details.getNotifyId() >0){
			Optional<Notification> getNotify = notifyRepo.findByNotifyId(details.getNotifyId());
			if(getNotify.isPresent()){
				Notification upd = getNotify.get();
				upd.setReadStatus(1);
				notifyRepo.save(upd);
			}
		}
		return null;
	}
	
	@Override
	public Map<String, Object> samplePushNotification(String apktoken,HttpServletRequest httpRequest) {
		FileInputStream serviceAccount;
		
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length()-1);
		
		try {
			serviceAccount = new FileInputStream(path+File.separator+"diing-dong-app-firebase-adminsdk.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setDatabaseUrl("https://diing-dong-app.firebaseio.com")
					  .build();

					FirebaseApp.initializeApp(options);
					
					Map<String, String> data = new HashMap<>();
					
					data.put("messageToken", apktoken);
					data.put("messageNotificationTitle", "Portugal vs. Denmark");
					data.put("messageNotificationBody", "great match!");
					data.put("messageDataNick", "Mario");
					data.put("messageDataRoom", "PortugalVSDenmark");
					
							Message message = Message.builder().putAllData(data).setTopic("chuck")
					        .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
					            .setNotification(new WebpushNotification("Background Title (server)",
					                "Background Body (server)", "mail2.png"))
					            .build())
					        .build();

					    String response = FirebaseMessaging.getInstance().sendAsync(message).get();
					    System.out.println("Sent message: " + response);
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		return null;
	}

	@Override
	public Map<String, Object> tagPushNotification(NotificationDTO details) {
		System.out.println("priya spantag "+String.valueOf(details.getNotifyFileId()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	 
		String date = format.format(new Date());
		 Notification follow =new Notification();
		 follow.setNotificationType(details.getNotificationType());
		 follow.setNotifyFileId(details.getTagId());
		 follow.setReadStatus(0);
		 follow.setUserId(String.valueOf(details.getUserId()));
		 follow.setCreatedAt(date);
		 follow.setNotifyUserId(details.getNotifyUserId());
		 follow.setCreatedby(String.valueOf(String.valueOf(details.getUserId())));
		 notifyRepo.save(follow);
		return null;
	}
	
}
