package com.spantag.socialMediaAppln.notification;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spantag.socialMediaAppln.utils.ResponseInfo;
import com.spantag.socialMediaAppln.utils.ResponseInfo.ResponseType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "NOTIFICATION MODULE", description = "notificatin for like, commment, and following services are handled here")
public class NotificationController {
	
	ModelMapper mapper = new ModelMapper();
	
	@Autowired
	NotificationService notificationService;
	
	ResponseEntity<Object> response = null;
	
	@GetMapping("/getNotificationCount/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> getNotificationCount(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.getNotificationCount(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@GetMapping("/getAllNotificationCount/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> getAllNotificationCount(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.getAllNotificationCount(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@GetMapping("/pushNotification/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> pushNotification(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.pushNotification(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	@GetMapping("/getfollowerNotification/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> getfollowerNotification(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.getfollowerNotification(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	@GetMapping("/getlikesNotification/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> getlikesNotification(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.getlikesNotification(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}

	
	@GetMapping("/getcommentsNotification/{userid}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> getcommentsNotification(@PathVariable("userid") int userid) throws Exception {
		System.out.println("welcome oto notification");
		Map<String, Object> out = notificationService.getcommentsNotification(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	@PostMapping("/readnotify")
	@ApiOperation(value = "user can Change their password")
	public ResponseEntity<Object> readnotify(@RequestBody NotificationDTO Input) throws Exception {

		NotificationDTO details = mapper.map(Input, NotificationDTO.class);
		Map<String, Object> output = notificationService.readnotify(details);

		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);

		return response;

	}
	
	@PostMapping("/tagPushNotification")
	@ApiOperation(value = "user can Change their password")
	public ResponseEntity<Object> tagPushNotification(@RequestBody NotificationDTO Input) throws Exception {

		NotificationDTO details = mapper.map(Input, NotificationDTO.class);
		Map<String, Object> output = notificationService.tagPushNotification(details);

		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);

		return response;

	}
	
	@GetMapping("/samplePushNotification/{apktoken}")
	@ApiOperation(value = "get notification count of user")
	public ResponseEntity<Object> samplePushNotification(@PathVariable("apktoken") String apktoken,
			HttpServletRequest httpRequest) throws Exception {

		Map<String, Object> out = notificationService.samplePushNotification(apktoken,httpRequest);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
}
