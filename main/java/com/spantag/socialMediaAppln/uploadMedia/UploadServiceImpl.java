package com.spantag.socialMediaAppln.uploadMedia;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.spantag.socialMediaAppln.notification.Notification;
import com.spantag.socialMediaAppln.notification.NotificationRepository;
import com.spantag.socialMediaAppln.utils.ConnUtil;
import com.spantag.socialMediaAppln.login.User;
import com.spantag.socialMediaAppln.login.UserRegisterRepository;



@Service(value = "uploadService")
@Configuration
@PropertySource("classpath:SocialMediaApplnQueryList.properties")
public class UploadServiceImpl implements UploadService {

	@Autowired
	private NotificationRepository notifyRepo;
	
	@Autowired
	private FileUploadRepository fileUploadRepo;
	
	@Autowired
	private VideoCommentsRepository commentsRepo;
	
	@Autowired
	private FollowingRepository followRepo;
	
	@Autowired
	private VideoLikesRepository videolikesRepo;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ConnUtil connutil;
	
	@Autowired
	private CommentsReplyRepository commentsreplyRepo;
	
	@Autowired
	private UserRegisterRepository userRepo;
	@Override
	public List<SMFileDTO> getMyGalleryList(String hid, String length) {
		// TODO Auto-generated method stub
		int status= 1;
		
		String Query= env.getProperty("gallerylimits");
		System.out.println("hid"+hid+"status"+status+"length"+length);
		List<Object[]> gallerylist = connutil.executequerybyidswithinteger(em, Query, hid,Integer.valueOf(length));
		
		List<SMFileDTO> galleryList = new ArrayList();
		 for (Object[] object : gallerylist) {
           
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[7]));
			 smFileDTO.setFileurl(String.valueOf(object[8]));
			 smFileDTO.setType(String.valueOf(object[12]));
			 
			 smFileDTO.setUserid(String.valueOf(object[13]));
			 smFileDTO.setCreatedat(String.valueOf(object[1]));
			 
			 smFileDTO.setFilePoster(String.valueOf(object[4]));
			 smFileDTO.setFileFrom(String.valueOf(object[3]));
			 smFileDTO.setIsprocesscompleted(Integer.valueOf(String.valueOf(object[9])));
			
			length = length + 1;
			galleryList.add(smFileDTO);
     }

		
//		PageRequest topTwenty = PageRequest.of(Integer.valueOf(length), 12, new Sort(Direction.DESC, "fileid"));
//		List<SMFile> fileList = fileUploadRepo.findByUseridAndStatus(hid,status,topTwenty);
//		System.out.println("filelistlength  "+fileList.size()+"  "+Integer.valueOf(length));
//		List<SMFileDTO> outputList = new ArrayList<>();
//		if(fileList != null && fileList.size() > 0) {
//			
//			Iterator<SMFile> headerDtlItr = fileList.iterator();
//			while(headerDtlItr.hasNext()) {
//				SMFile smfile = headerDtlItr.next();
//				ModelMapper modelmapper = new ModelMapper();
//				SMFileDTO sMFileDTO = modelmapper.map(smfile,SMFileDTO.class);
//				outputList.add(sMFileDTO);
//			}
//		}
		return galleryList;
	}
	
	@Override
	public List<SMFileDTO> getMyhomelist(String userid,int langid) {
		
		List<SMFile> fileList = fileUploadRepo.findAll();
		String mQuery= env.getProperty("checkblockconditionQuery");
		String blockedids = "0";

		List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
		List<Object> blockconditionList = new ArrayList();
		 for (String object : blockconditionlist) {
				Map<String, Object> out = new LinkedHashMap<>();
				if(object != null && !object.equals("") && object.length() > 0){
					//out.put("blockstatus", object);
					String[] blockedidss = object.split(",");
			    	if(blockedidss.length > 0){
			    		for(int i = 0; i< blockedidss.length; i++){
			    			blockedids = blockedidss[i] + ",";
			    		}
			    	}
			    	if(blockedids != null && blockedids.length()>0  ){
			    		blockedids = blockedids.substring(0, blockedids.length() - 1);
				    }
					
					
				}
		 }
		
		String Query= env.getProperty("homevideolistquery");
		int length = 0;
		List<Object[]> orderlist = connutil.executequerybyidswithintegerandSTring(em, Query, userid,langid,blockedids);
		System.out.println(orderlist.size());
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
           
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 
			 smFileDTO.setUserid(String.valueOf(object[4]));
			// smFileDTO.setFilename(String.valueOf(object[5]));
			 smFileDTO.setCreatedat(String.valueOf(object[6]));
			 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setLikeid(String.valueOf(object[17]));
			 
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			  smFileDTO.setUsername(String.valueOf(object[10]));
			  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
			  smFileDTO.setFilePoster(String.valueOf(object[12]));
			  smFileDTO.setFollowCount(String.valueOf(object[13]));
			 /* smFileDTO.setDuration(String.valueOf(object[14]));
			smFileDTO.setTimedesc(String.valueOf(object[15]));*/
			smFileDTO.setFileuploadername(String.valueOf(object[16]));
			 /* smFileDTO.setCommentid(String.valueOf(object[11]));
			 smFileDTO.setComments(String.valueOf(object[12]));*/
			smFileDTO.setPauseswitch(true);
			smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
			smFileDTO.setIndices(String.valueOf(length));
			smFileDTO.setLikestatus(String.valueOf(object[18]));
			smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setFileFrom(String.valueOf(object[21]));
			smFileDTO.setDubsfileurl(String.valueOf(object[22]));
			String isprocessingcompleted = String.valueOf(object[23]);
			smFileDTO.setIsprocesscompleted(Integer.valueOf(isprocessingcompleted));
			length = length + 1;
             orderList.add(smFileDTO);
     }

		return orderList;
	}
	
	@Override
	public Map<String, Object> getfriendstotaglist(int id) {
		String Query= env.getProperty("friendstotaglistQuery");
		List<Object[]> followinglist = connutil.executequerybyid(em, Query, String.valueOf(id));
		List<Object> followingList = new ArrayList();
		 for (Object[] object : followinglist) {
				Map<String, Object> out = new LinkedHashMap<>();
				out.put("userName", String.valueOf(object[0]));
				out.put("profilePath", String.valueOf(object[1]));
				out.put("userId", String.valueOf(object[2]));
				out.put("status", String.valueOf(object[3]));
				out.put("id", String.valueOf(object[4]));
				out.put("handleName", String.valueOf(object[5]));
				out.put("jsxstring", String.valueOf(object[0])+"-"+String.valueOf(object[2]));
				followingList.add(out);
		 }
		 Map<String, Object> out = new LinkedHashMap<>();
		 out.put("followeringList", followingList);
		return out;
	} 
	

	@Override
	public List<SMFileDTO> Enduserhomelist(String userid,String fileid) {
		
		List<SMFile> fileList = fileUploadRepo.findAll();
		
		String Query= env.getProperty("enduservideolistquery");
		int length = 0;
		List<Object[]> orderlist = connutil.executequerybyidswithinteger(em, Query, userid,Integer.parseInt(fileid));
		System.out.println(orderlist.size());
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
           
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 
			 smFileDTO.setUserid(String.valueOf(object[4]));
			// smFileDTO.setFilename(String.valueOf(object[5]));
			 smFileDTO.setCreatedat(String.valueOf(object[6]));
			 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setLikeid(String.valueOf(object[17]));
			 
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			  smFileDTO.setUsername(String.valueOf(object[10]));
			  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
			  smFileDTO.setFilePoster(String.valueOf(object[12]));
			  smFileDTO.setFollowCount(String.valueOf(object[13]));
			 /* smFileDTO.setDuration(String.valueOf(object[14]));
			smFileDTO.setTimedesc(String.valueOf(object[15]));*/
			smFileDTO.setFileuploadername(String.valueOf(object[16]));
			 /* smFileDTO.setCommentid(String.valueOf(object[11]));
			 smFileDTO.setComments(String.valueOf(object[12]));*/
			smFileDTO.setPauseswitch(true);
			smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
			smFileDTO.setIndices(String.valueOf(length));
			smFileDTO.setLikestatus(String.valueOf(object[18]));
			smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setFileFrom(String.valueOf(object[21]));
			smFileDTO.setDubsfileurl(String.valueOf(object[22]));
			length = length + 1;
             orderList.add(smFileDTO);
     }

		return orderList;
	}
	
	@Override
	public List<SMFileDTO> getMyhomelistwithLimit(String length, String userid,int langid) {
		String mQuery= env.getProperty("checkblockconditionQuery");
		String blockedids = "0";

		List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
		List<Object> blockconditionList = new ArrayList();
		for (String object : blockconditionlist) {
			Map<String, Object> out = new LinkedHashMap<>();
			if(object != null && !object.equals("") && object.length() > 0){
				//out.put("blockstatus", object);
				String[] blockedidss = object.split(",");
		    	if(blockedidss.length > 0){
		    		for(int i = 0; i< blockedidss.length; i++){
		    			blockedids = blockedidss[i] + ",";
		    		}
		    	}
		    	if(blockedids != null && blockedids.length()>0  ){
		    		blockedids = blockedids.substring(0, blockedids.length() - 1);
			    }
				
				
			}
	 }
		List<SMFile> fileList = fileUploadRepo.findAll();
		
		String Query= env.getProperty("homevideolistwithlimitsquery");
		int limitvalue = Integer.parseInt(length);
		List<Object[]> orderlist = connutil.executequerybyfourIntvariable(em,Query,userid,limitvalue,langid,blockedids);
		System.out.println("block ids "+blockedids);
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 
			 smFileDTO.setUserid(String.valueOf(object[4]));
			// smFileDTO.setFilename(String.valueOf(object[5]));
			 smFileDTO.setCreatedat(String.valueOf(object[6]));
			 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setLikeid("0");
			 
//			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
//			 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[9])));
//			 smFileDTO.setFilePoster(String.valueOf(object[10]));
//			 smFileDTO.setFollowCount(String.valueOf(object[11]));
			 /* smFileDTO.setCommentid(String.valueOf(object[11]));
			 smFileDTO.setComments(String.valueOf(object[12]));*/
			 
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			  smFileDTO.setUsername(String.valueOf(object[10]));
			  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
			  smFileDTO.setFilePoster(String.valueOf(object[12]));
			  smFileDTO.setFollowCount(String.valueOf(object[13]));
			  /*smFileDTO.setDuration(String.valueOf(object[14]));
			smFileDTO.setTimedesc(String.valueOf(object[15]));*/
			smFileDTO.setFileuploadername(String.valueOf(object[16]));
			smFileDTO.setPauseswitch(true);
			smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
			smFileDTO.setIndices(String.valueOf(limitvalue));
			smFileDTO.setLikestatus(String.valueOf(object[18]));
			smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setFileFrom(String.valueOf(object[21]));
			smFileDTO.setDubsfileurl(String.valueOf(object[22]));
			String isprocessingcompleted = String.valueOf(object[23]);
			smFileDTO.setIsprocesscompleted(Integer.valueOf(isprocessingcompleted));
             orderList.add(smFileDTO);
             limitvalue = limitvalue + 1;
		 }

		return orderList;
	}
	@Override
	public List<SMFileDTO> Enduserhomelistwithlimit(String length, String userid,String fileid) {
		
		List<SMFile> fileList = fileUploadRepo.findByUseridAndStatusOrderByFileidDesc(userid,1);
		
		String Query= env.getProperty("enduserpostwithlimitquery");
		int limitvalue = Integer.parseInt(length);
		List<Object[]> orderlist = connutil.executequerybythreeIntvariable(em,Query,userid,limitvalue, Integer.parseInt(fileid));
		System.out.println(limitvalue);
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 
			 smFileDTO.setUserid(String.valueOf(object[4]));
			// smFileDTO.setFilename(String.valueOf(object[5]));
			 smFileDTO.setCreatedat(String.valueOf(object[6]));
			 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setLikeid("0");
			 
//			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
//			 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[9])));
//			 smFileDTO.setFilePoster(String.valueOf(object[10]));
//			 smFileDTO.setFollowCount(String.valueOf(object[11]));
			 /* smFileDTO.setCommentid(String.valueOf(object[11]));
			 smFileDTO.setComments(String.valueOf(object[12]));*/
			 
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			  smFileDTO.setUsername(String.valueOf(object[10]));
			  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
			  smFileDTO.setFilePoster(String.valueOf(object[12]));
			  smFileDTO.setFollowCount(String.valueOf(object[13]));
			  /*smFileDTO.setDuration(String.valueOf(object[14]));
			smFileDTO.setTimedesc(String.valueOf(object[15]));*/
			smFileDTO.setFileuploadername(String.valueOf(object[16]));
			smFileDTO.setPauseswitch(true);
			smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
			smFileDTO.setIndices(String.valueOf(limitvalue));
			smFileDTO.setLikestatus(String.valueOf(object[18]));
			smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setFileFrom(String.valueOf(object[21]));
			smFileDTO.setDubsfileurl(String.valueOf(object[22]));
             orderList.add(smFileDTO);
             limitvalue = limitvalue + 1;
		 }

		return orderList;
	}
	
	@Override
	public List<SMFileDTO> EnduserhomelistwithlimitHome(String length, String userid,String fileid) {
		System.out.println("welcome to enduser");
		List<SMFile> fileList = fileUploadRepo.findByUseridAndStatusOrderByFileidDesc(userid,1);
		
		String Query= env.getProperty("enduserpostwithlimitqueryhome");
		int limitvalue = Integer.parseInt(length);
		List<Object[]> orderlist = connutil.executequerybyidswithinteger(em,Query,userid,limitvalue);
		System.out.println(limitvalue);
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 
			 smFileDTO.setUserid(String.valueOf(object[4]));
			// smFileDTO.setFilename(String.valueOf(object[5]));
			 smFileDTO.setCreatedat(String.valueOf(object[6]));
			 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setLikeid("0");
			 
//			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
//			 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[9])));
//			 smFileDTO.setFilePoster(String.valueOf(object[10]));
//			 smFileDTO.setFollowCount(String.valueOf(object[11]));
			 /* smFileDTO.setCommentid(String.valueOf(object[11]));
			 smFileDTO.setComments(String.valueOf(object[12]));*/
			 
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			  smFileDTO.setUsername(String.valueOf(object[10]));
			  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
			  smFileDTO.setFilePoster(String.valueOf(object[12]));
			  smFileDTO.setFollowCount(String.valueOf(object[13]));
			  /*smFileDTO.setDuration(String.valueOf(object[14]));
			smFileDTO.setTimedesc(String.valueOf(object[15]));*/
			smFileDTO.setFileuploadername(String.valueOf(object[16]));
			smFileDTO.setPauseswitch(true);
			smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
			smFileDTO.setIndices(String.valueOf(limitvalue));
			smFileDTO.setLikestatus(String.valueOf(object[18]));
			smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
			smFileDTO.setFileFrom(String.valueOf(object[21]));
			smFileDTO.setDubsfileurl(String.valueOf(object[22]));
			String isprocessingcompleted = String.valueOf(object[23]);
			smFileDTO.setIsprocesscompleted(Integer.valueOf(isprocessingcompleted));
             orderList.add(smFileDTO);
             limitvalue = limitvalue + 1;
		 }

		return orderList;
	}

	
	
	@Override
	public List<SMFileDTO> getMyVideolist(String fileid) {
		

		String Query= env.getProperty("itemquery");
		
		List<Object[]> orderlist = connutil.executequerybyid(em,Query,fileid);
		System.out.println(orderlist.size());
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
			 smFileDTO.setFilename(String.valueOf(object[1]));
			 smFileDTO.setFileurl(String.valueOf(object[2]));
			 smFileDTO.setType(String.valueOf(object[3]));
			 smFileDTO.setFileFrom(String.valueOf(object[12]));
            
             orderList.add(smFileDTO);
     }

		return orderList;
	}
	@Override
	public List<SMFileDTO> videolistById(String videoid) {
		// TODO Auto-generated method stub

		String Query= env.getProperty("itemqueryByid");
		
		List<Object[]> orderlist = connutil.executequerybyid(em,Query,videoid);
		System.out.println(orderlist.size());
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
            
			 SMFileDTO smFileDTO = new SMFileDTO();
			
			
			 smFileDTO.setFileurl(String.valueOf(object[0]));
			
			 smFileDTO.setFileFrom(String.valueOf(object[1]));
            
             orderList.add(smFileDTO);
     }

		return orderList;
	} 

	@Override
	public Map<String, Object> addComments(VideoCommentsDTO commentsInput) {
		Map<String, Object> out = new LinkedHashMap<>();
//		ModelMapper modelmapper = new ModelMapper();
//		
//		VideoComments inp = modelmapper.map(commentsInput, VideoComments.class);
		System.out.println("normal comments ====="+commentsInput.getComments());
		System.out.println("encrypted comments ====="+connutil.encodeStringUrl(commentsInput.getComments()));
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	        LocalDateTime now = LocalDateTime.now();  
		VideoComments cmd= new VideoComments();
		cmd.setComments(connutil.encodeStringUrl(commentsInput.getComments()));
		cmd.setGifId(commentsInput.getGifId());
		cmd.setUserId(commentsInput.getUserId());
		cmd.setVideoId(commentsInput.getVideoId());
		cmd.setCreatedAt(dtf.format(now));
		cmd.setCreatedby(String.valueOf(commentsInput.getUserId()));
		cmd.setJsxString(commentsInput.getJsxString());
		cmd.setCommentType(commentsInput.getCommentType());
		VideoComments output = commentsRepo.save(cmd);
		out.put("commentId", output.getCommentId());
		out.put("Type", "S");
		out.put("message", "Comments added successfully");
		return out;
	}

	@Override
	public Map<String, Object> getCommentsByVideoId(int videoId) {
           String Query= env.getProperty("commentsQuery");
           Map<String, Object> out = new LinkedHashMap<>();
		List<Object[]> commentlist = connutil.executequerybyid(em,Query,String.valueOf(videoId));
		System.out.println(commentlist.size());
		List<Object> commentsList = new ArrayList<>();
		 for (Object[] object : commentlist) {
			 Map<String, Object> responsemap = new HashMap<String, Object>();
				responsemap.put("commentId", object[0]);
				responsemap.put("userName", object[1]);
				responsemap.put("userLetter", object[2]);
				responsemap.put("comments", object[3]);
            
			 commentsList.add(responsemap);
     }
		 out.put("commentsList", commentsList); 
		return out;
	}

	@Override
	public Map<String, Object> addCommentsReply(CommentsReply commentsInput) {
		
		List<CommentsReply> count = commentsreplyRepo.findByVideoIdAndUserIdAndCommentId(commentsInput.getVideoId(),commentsInput.getUserId(),commentsInput.getCommentId());
		
		Map<String, Object> out = new LinkedHashMap<>();
		ModelMapper modelmapper = new ModelMapper();
		
		CommentsReply Check = new CommentsReply();
		
		
		if(count.size() > 0){
			
			Check.cmtlike = count.get(0).cmtlike;	
			
			
			ModelMapper Newmodelmapper = new ModelMapper();
			
			CommentsReply comments = Newmodelmapper.map(commentsInput, CommentsReply.class);
	
						if(Check.cmtlike > 0){
							comments.setCmtlike(0);
							comments.setReplyId(count.get(0).getReplyId());
							CommentsReply output = commentsreplyRepo.save(comments);
							out.put("Type", "S");
							out.put("message", "CommentLike Removed");		
							
							
						}else{
							comments.setCmtlike(1);
							comments.setReplyId(count.get(0).getReplyId());
							CommentsReply output = commentsreplyRepo.save(comments);
							out.put("Type", "S");
							out.put("message", "CommentLike updated");
						}
	
			}else{
				ModelMapper Newmodelmapper = new ModelMapper();
				CommentsReply comments = Newmodelmapper.map(commentsInput, CommentsReply.class);
				comments.setReplyId(0);
				comments.setCreatedby(String.valueOf(commentsInput.getUserId()));
				CommentsReply output = commentsreplyRepo.save(commentsInput);
				out.put("Type", "S");
				out.put("message", "CommentLikes added successfully");
			}
		
		return out;
	}
	
	@Override
	public Map<String, Object> addlikes(videolikes likesInput) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	        LocalDateTime now = LocalDateTime.now();  
		
		List<videolikes> count = videolikesRepo.findByVideoidAndUserid(likesInput.videoid,likesInput.userid);
		
		Map<String, Object> outresult = new LinkedHashMap<>();	
		
		videolikes video = new videolikes();
		System.out.println("count  "+count.size());
		if(count.size() > 0){
			
				video.totallikes = count.get(0).totallikes;		
				
				
				ModelMapper modelmapper = new ModelMapper();
				
				videolikes like = modelmapper.map(likesInput, videolikes.class);
		
							if(video.totallikes > 0){
								like.setTotallikes(0);
								like.setLikeid(count.get(0).likeid);
								videolikes output = videolikesRepo.save(like);
								outresult.put("Type", "S");
								outresult.put("message", "Like Removed");		
								
								
							}else{
								like.setTotallikes(1);
								like.setVideoid(count.get(0).videoid);
								like.setLikeid(count.get(0).likeid);
								videolikes output = videolikesRepo.save(like);
								outresult.put("Type", "S");
								outresult.put("message", "Like updated");
							}
		
				}else{
					ModelMapper modelmapper = new ModelMapper();
					videolikes like = modelmapper.map(likesInput, videolikes.class);
					like.setLikeid(0);
					like.setCreatedby(String.valueOf(likesInput.getUserid()));
					like.setCreatedAt(dtf.format(now));
					videolikes output = videolikesRepo.save(likesInput);
					outresult.put("Type", "S");
					outresult.put("message", "Likes added successfully");
				}
		
		return outresult;
	}
	
	@Override
	public List<SMFileDTO> getComments(String fileid, String length, String type, String commentid) {
		String Query="";List<Object[]> orderlist;
		
		if(type.equals("comment")) {
			System.out.println("welcometo particular id");
			System.out.println("String length"+commentid);
			Query= env.getProperty("commentswithparticularidquery");
			int numlength = Integer.parseInt(length);
			orderlist = connutil.executequerybythreevariable(em,Query,fileid,commentid,numlength);
		}else {
			 Query= env.getProperty("commentsquery");
			 int numlength = Integer.parseInt(length);
			 orderlist = connutil.executequerybyidswithinteger(em,Query,fileid,numlength);
		}

		
		
		
		List<SMFileDTO> orderList = new ArrayList();
		 for (Object[] object : orderlist) {
			
			 SMFileDTO smFileDTO = new SMFileDTO();
			 smFileDTO.setCommentid((String.valueOf(object[0])));
			 smFileDTO.setVideoid(String.valueOf(object[1]));
			 smFileDTO.setComments(connutil.decodeStringUrl(String.valueOf(object[2])));
			 smFileDTO.setUserid(String.valueOf(object[3]));
			 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[4])));
			 smFileDTO.setUsername(String.valueOf(object[5]));
//			 smFileDTO.setReply(String.valueOf(object[6]));
//			 smFileDTO.setReplyid(String.valueOf(object[7]));
			 smFileDTO.setTotalcommentlikes(connutil.numbertoKformat(String.valueOf(object[6])));
			 smFileDTO.setTotalcommentreply(connutil.numbertoKformat(String.valueOf(object[7])));
			 smFileDTO.setGifId(String.valueOf(object[8]));
			 smFileDTO.setImageurl(String.valueOf(object[9]));
			 smFileDTO.setDurationhour(String.valueOf(object[10]));
			 smFileDTO.setDurationminute(String.valueOf(object[11]));
			 /*smFileDTO.setDuration(String.valueOf(object[12]));
			 smFileDTO.setTimedesc(String.valueOf(object[13]));*/
			 smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[16]),String.valueOf(object[17]) ) );
			 smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[16]),String.valueOf(object[17]) ) );
			 smFileDTO.setJsxString(String.valueOf(object[18]));
			 smFileDTO.setCommentType(String.valueOf(object[19]));
			 orderList.add(smFileDTO);
	 }

		return orderList;
	}
	
	 @Override
		public Map<String, Object> saveCommentsReply(CommentsReply commentsInput) {
			
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	        LocalDateTime now = LocalDateTime.now();  
			Map<String, Object> out = new LinkedHashMap<>();
			ModelMapper modelmapper = new ModelMapper();
			
			CommentsReply inp = modelmapper.map(commentsInput, CommentsReply.class);
			commentsInput.setCreatedby(String.valueOf(commentsInput.getUserId() ));
			commentsInput.setCreatedAt(dtf.format(now));
			CommentsReply output = commentsreplyRepo.save(inp);
			out.put("replyId", output.getReplyId());
			out.put("Type", "S");
			out.put("message", "Sucess");
			return out;
		}
		
		
		
		@Override
		public List<SMFileDTO> getCommentDetails(String commentid, String length, String type, String replyid) {
			String Query = "";
			List<SMFileDTO> orderList = new ArrayList();
			List<Object[]> orderlist;
			System.out.println("getCommentDetails  getCommentDetails  getCommentDetails "+type +" "+length);
			if(type.equals("reply")) {
				Query= env.getProperty("singlecommentdetailswithparticularidQuery");			
				int numlength = Integer.parseInt(length);
				orderlist = connutil.executequerybythreevariable(em,Query,commentid,replyid,numlength);
				
				
			}else{
				Query= env.getProperty("singlecommentdetailsQuery");
				System.out.println("numlength===="+length);
			int numlength = Integer.parseInt(length);
			System.out.println("length===="+numlength);
			orderlist = connutil.executequerybyidswithinteger(em,Query,commentid,numlength);
			
			
			}
			

			 for (Object[] object : orderlist) {
				
				 SMFileDTO smFileDTO = new SMFileDTO();
				 smFileDTO.setReply(String.valueOf(object[0]));
				 smFileDTO.setUsername(String.valueOf(object[3]));
				 smFileDTO.setImageurl(String.valueOf(object[1]));
				 smFileDTO.setUserid(String.valueOf(object[2]));
				 /*smFileDTO.setDuration(String.valueOf(object[4]));
				 smFileDTO.setTimedesc(String.valueOf(object[5]));*/
				// smFileDTO.setDuration(String.valueOf(object[6]));
				 smFileDTO.setTotallikes(String.valueOf(object[7]));
				 smFileDTO.setReplyid(String.valueOf(object[8]));
				 smFileDTO.setGifId(String.valueOf(object[9]));
				 smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[10]),String.valueOf(object[11]) ) );
				 smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[10]),String.valueOf(object[11]) ) );
				 smFileDTO.setJsxString(String.valueOf(object[12]));
				 smFileDTO.setCommentType(String.valueOf(object[13]));
				 orderList.add(smFileDTO);
		 }

			return orderList;
		}
		
		@Override
		public List<SMFileDTO> getCommentMainDetails(String commentid) {
			
			String Query= env.getProperty("singlecommentmailQuery");
			
			List<Object[]> orderlist = connutil.executequerybyid(em,Query,commentid);
			
			List<SMFileDTO> orderList = new ArrayList();
			 for (Object[] object : orderlist) {
				
				 SMFileDTO smFileDTO = new SMFileDTO();

				 smFileDTO.setUsername(String.valueOf(object[0]));
				 smFileDTO.setImageurl(String.valueOf(object[1]));
				 smFileDTO.setUserid(String.valueOf(object[2]));
				 smFileDTO.setComments(connutil.decodeStringUrl(String.valueOf(object[3])));
				 smFileDTO.setJsxString(String.valueOf(object[4]));
				 smFileDTO.setCommentType(String.valueOf(object[5]));
				 orderList.add(smFileDTO);
		 }

			return orderList;
		}
		
	
		  @Override
		  public List<SMFileDTO> getMyprofileGallery(String userid) {
			
			System.out.println("rgrfhgdfhdfhdfhdfhdfhdfghdfg");
			String Query= env.getProperty("myprofileGalleryQuery");
			
			List<Object[]> orderlist = connutil.executequerybyid(em,Query,userid);
			System.out.println(orderlist.size());
			List<SMFileDTO> orderList = new ArrayList();
			 for (Object[] object : orderlist) {
	            
				 SMFileDTO smFileDTO = new SMFileDTO();
				 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
				 smFileDTO.setFilename(String.valueOf(object[1]));
				 smFileDTO.setFileurl(String.valueOf(object[2]));
				 smFileDTO.setType(String.valueOf(object[3]));			 
				 smFileDTO.setFilecontenttype(String.valueOf(object[4]));
				 smFileDTO.setUserid(String.valueOf(object[5]));
				 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[7])));
				 smFileDTO.setFilecaption(String.valueOf(object[7]));
				 smFileDTO.setIsprocesscompleted(Integer.parseInt(String.valueOf(object[9])));

	            
	             orderList.add(smFileDTO);
	     }

			return orderList;
		}
		  @Override
			public Map<String, Object> acceptrequest(FollowDTO input) {
			  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	 
				String date = format.format(new Date());
			  Optional<Follow> upd = followRepo.findByUserIdAndFollowingId(Integer.valueOf(input.getUserId()),Integer.valueOf(input.getFollowingId()));
			  if(upd.isPresent()){
					Follow setstatue = upd.get();
					setstatue.setAcceptRequest(input.getAcceptRequest());;
					followRepo.save(setstatue);
					
					 Notification follow =new Notification();
					 follow.setNotificationType("request");
					 follow.setNotifyFileId("0");
					 follow.setReadStatus(0);
					 follow.setUserId(String.valueOf(input.getUserId()));
					 follow.setCreatedAt(date);
					 follow.setNotifyUserId(Integer.valueOf(input.getFollowingId()));
					 follow.setCreatedby(String.valueOf(input.getUserId()));
					 notifyRepo.save(follow);
				}
			  Map<String, Object> out = new LinkedHashMap<>();
				out.put("Type", "S");
				out.put("message", "Added successfully");
				return out;
			}
		  @Override
				public Map<String, Object> confirmAcceptRequest(FollowDTO input) {
				  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	 
					String date = format.format(new Date());
				  Optional<Follow> upd = followRepo.findByUserIdAndFollowingId(Integer.valueOf(input.getUserId()),Integer.valueOf(input.getFollowingId()));
				  if(upd.isPresent()){
						Follow setstatue = upd.get();
						setstatue.setAcceptRequest(input.getAcceptRequest());;
						followRepo.save(setstatue);
						
						Optional<Notification>updNotify = notifyRepo.findByNotifyId(Long.valueOf(input.getNotifyId()));
						if(updNotify.isPresent()){
							updNotify.get().setReadStatus(1);
							 notifyRepo.save(updNotify.get());
						}
						
						
					}
				  Map<String, Object> out = new LinkedHashMap<>();
					out.put("Type", "S");
					out.put("message", "Confirmed successfully");
					return out;
				}
		  @Override
			public Map<String, Object> addfollowing(FollowDTO input) {
			  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		        LocalDateTime now = LocalDateTime.now();
			  Map<String, Object> out = new LinkedHashMap<>();
			  Optional<Follow> checkblock = followRepo.findByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
			  if(checkblock.isPresent()){
				  Follow checkblockprofile = checkblock.get();
				  if(!(checkblockprofile.getBlockCondition().equals("blocked") || checkblockprofile.getBlockCondition().equals("block_requested"))){
					  if(input.getSeqId() > 0){
							Optional<Follow> upd = followRepo.findBySeqId(Long.valueOf(input.getSeqId()));
							if(upd.isPresent()){
								Follow setstatue = upd.get();
								setstatue.setStatus(0);
								followRepo.save(setstatue);
							}
							
						}
						Optional<Follow> upd1 = followRepo.findByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
						if(upd1.isPresent()){
							Follow ins =upd1.get();
							ins.setAcceptRequest(0);
							followRepo.save(ins);	
						}
						else{
							Follow ins = new Follow();
							ins.setUserId(input.getUserId());
							ins.setFollowingId(input.getFollowingId());
							ins.setStatus(input.getStatus());
							ins.setCreatedAt(dtf.format(now));
							ins.setCreatedby( String.valueOf(input.getUserId()) );
							followRepo.save(ins);
						}
						
						out.put("Type", "S");
						out.put("message", "Added successfully");
				  }else{
					  
						out.put("Type", "F");
						out.put("message", "Person blocked you");
				  }
			  }
			  else{
					Follow ins = new Follow();
					ins.setUserId(input.getUserId());
					ins.setFollowingId(input.getFollowingId());
					ins.setStatus(input.getStatus());
					ins.setCreatedAt(dtf.format(now));
					ins.setCreatedby( String.valueOf(input.getUserId()) );
					followRepo.save(ins);
			}
				
				
				return out;
			
		  }
		  @Override
			public Map<String, Object> removeblock(FollowDTO input) {
			
				Optional<Follow> blockperson = followRepo.findByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
				Optional<Follow> blockedperson = followRepo.findByUserIdAndFollowingId(input.getFollowingId(),input.getUserId());
				if(blockperson.isPresent() && blockedperson.isPresent()){
					Follow blockpersonprofile =blockperson.get();
					Follow blockedpersonprofile =blockedperson.get();
					if(blockpersonprofile.getBlockCondition().equals("block_requested") && 
							blockedpersonprofile.getBlockCondition().equals("blocked")){
						followRepo.removeByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
						followRepo.removeByUserIdAndFollowingId(input.getFollowingId(),input.getUserId());
						System.out.println("**************");
					}else if(blockpersonprofile.getBlockCondition().equals("block_requested") && 
							blockedpersonprofile.getBlockCondition().equals("block_requested")){
						blockpersonprofile.setBlockCondition("blocked");
						followRepo.save(blockpersonprofile);
						System.out.println("---------------------");
					}
				}else if(blockperson.isPresent()){
					Follow blockpersonprofile =blockperson.get();
					if(blockpersonprofile.getBlockCondition().equals("block_requested")){
						followRepo.removeByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
					}
				}
				
				String replacedids = null, replacedidsf = null;
				Optional<User> optional = userRepo.findByUserid(Long.valueOf(input.getUserId()));
				if(optional.isPresent()){
			    	User user = optional.get();
			    	if(user.getBlockIds() != null){
				    	String[] blockedids = user.getBlockIds().split(",");
				    	if(blockedids.length > 0){
				    		for(int i = 0; i< blockedids.length; i++){
				    			if(!blockedids[i].equals(String.valueOf(input.getFollowingId()))){
				    				replacedids = blockedids[i]+",";
				    			}
				    		}
				    	}
			    	}
			    	if(replacedids != null && replacedids.length()>0  ){
			    	replacedids = replacedids.substring(0, replacedids.length() - 1);
			    	}
			    	user.setBlockIds(replacedids);
			    	userRepo.save(user);
				}
				
				Optional<User> optionalf = userRepo.findByUserid(Long.valueOf(input.getFollowingId()));
				if(optionalf.isPresent()){
			    	User user = optionalf.get();
			    	if(user.getBlockIds() != null){
				    	String[] blockedids = user.getBlockIds().split(",");
				    	if(blockedids.length > 0){
				    		for(int i = 0; i< blockedids.length; i++){
				    			if(!blockedids[i].equals(String.valueOf(input.getUserId()))){
				    				replacedidsf = blockedids[i]+",";
				    			}
				    		}
				    	}
			    	}
			    	if( replacedidsf != null && replacedidsf.length() > 0){
			    		replacedidsf = replacedidsf.substring(0, replacedidsf.length() - 1);
			    	}
			    	
			    	user.setBlockIds(replacedidsf);
			    	userRepo.save(user);
				}
				
				Map<String, Object> out = new LinkedHashMap<>();
				out.put("Type", "S");
				out.put("message", "Block Removed successfully");
				return out;
			}

		  
		  @Override
			public Map<String, Object> blockperson(FollowDTO input) {
			
				Optional<Follow> blkreqperson = followRepo.findByUserIdAndFollowingId(input.getUserId(),input.getFollowingId());
				if(blkreqperson.isPresent()){
					Follow insblkreqperson =blkreqperson.get();
					
					insblkreqperson.setBlockCondition("block_requested");
					followRepo.save(insblkreqperson);
						
				}else{
					System.out.println("++++++++++++++++++");
					Follow ins = new Follow();
					ins.setUserId(input.getUserId());
					ins.setFollowingId(input.getFollowingId());
					ins.setStatus(input.getStatus());
					ins.setCreatedAt(input.getCreatedAt());
					ins.setCreatedby( String.valueOf(input.getUserId()) );
					ins.setBlockCondition("block_requested");
					followRepo.save(ins);
				}
				
				Optional<User> optional = userRepo.findByUserid(Long.valueOf(input.getUserId()));
				if(optional.isPresent()){
			    	User user = optional.get();
			    	if(user.getBlockIds() != null &&  user.getBlockIds().length() > 0){
				    	
				    		if(!user.getBlockIds().contains(String.valueOf(input.getFollowingId()))){
				    			user.setBlockIds(user.getBlockIds()+","+String.valueOf(input.getFollowingId()));
				    		}
				    	
			    	}else{
			    		user.setBlockIds(String.valueOf(input.getFollowingId()));
			    	}
			    	userRepo.save(user);
				}
				Optional<Follow> blockedperson = followRepo.findByUserIdAndFollowingId(input.getFollowingId(),input.getUserId());
				if(blockedperson.isPresent()){
					Follow insblockedperson =blockedperson.get();
					if(insblockedperson.getBlockCondition()!=null &&insblockedperson.getBlockCondition().equals("blocked")){
						insblockedperson.setBlockCondition("block_requested");
						followRepo.save(insblockedperson);
					}else{
					insblockedperson.setBlockCondition("blocked");
					followRepo.save(insblockedperson);
					}
				}else{
					System.out.println("++++++++++++++++++");
					Follow ins = new Follow();
					ins.setUserId(input.getFollowingId());
					ins.setFollowingId(input.getUserId());
					ins.setStatus(input.getStatus());
					ins.setCreatedAt(input.getCreatedAt());
					ins.setCreatedby( String.valueOf(input.getUserId()) );
					ins.setBlockCondition("blocked");
					followRepo.save(ins);
				}
				Optional<User> optionalblocked = userRepo.findByUserid(Long.valueOf(input.getFollowingId()));
				
				if(optionalblocked.isPresent()){
			    	User userblocked = optionalblocked.get();
			    	if(userblocked.getBlockIds() != null &&  userblocked.getBlockIds().length() > 0){
				    	
				    		if(!userblocked.getBlockIds().contains(String.valueOf(input.getUserId()))){
				    			userblocked.setBlockIds(userblocked.getBlockIds()+","+String.valueOf(input.getUserId()));
				    		}
				    	
			    	}else{
			    		userblocked.setBlockIds(String.valueOf(input.getUserId()));
			    	}
			    	userRepo.save(userblocked);
				}
				
				Map<String, Object> out = new LinkedHashMap<>();
				out.put("Type", "S");
				out.put("message", "Blocked successfully");
				return out;
			}
			@Override
			public Map<String, Object> getfollowersdetails(String id,String userid) {
				System.out.println("welcome to all "+userid);
				String Query= env.getProperty("followersQuery");
				List<Object[]> followerlist = connutil.executequerybyid(em, Query,userid);
				List<Object> followerList = new ArrayList();
				 for (Object[] object : followerlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						out.put("userName", String.valueOf(object[0]));
						out.put("profilePath", String.valueOf(object[1]));
						out.put("userId", String.valueOf(object[2]));
						out.put("status", String.valueOf(object[3]));
						out.put("id", String.valueOf(object[4]));
						out.put("count", String.valueOf(object[5]));
						out.put("acceptRequest", String.valueOf(object[6]));
						
						followerList.add(out);
				 }
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("followersList", followerList);
				return out;
			}
			@Override
			public Map<String, Object> getfollowingdetails(int id) {
				String Query= env.getProperty("followingQuery");
				List<Object[]> followinglist = connutil.executequerybyid(em, Query, String.valueOf(id));
				List<Object> followingList = new ArrayList();
				 for (Object[] object : followinglist) {
						Map<String, Object> out = new LinkedHashMap<>();
						out.put("userName", String.valueOf(object[0]));
						out.put("profilePath", String.valueOf(object[1]));
						out.put("userId", String.valueOf(object[2]));
						out.put("status", String.valueOf(object[3]));
						out.put("id", String.valueOf(object[4]));
						followingList.add(out);
				 }
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("followeringList", followingList);
				return out;
			} 
			@Override
			public Map<String, Object> checkblockconditions(String userid,
					String follwingid) {
				
				String Query= env.getProperty("checkblockconditionQuery");
				System.out.println("query ======"+Query);
				List<String> blockconditionlist = connutil.executequerysStringop(em, Query, String.valueOf(follwingid));
				List<Object> blockconditionList = new ArrayList();
				 for (String object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null && !object.equals("") && object.length() > 0){
							//out.put("blockstatus", object);
							System.out.println("object   "+object);
							String mQuery= "SELECT "+String.valueOf(userid)+" IN ("+object+")";
							System.out.println("query ======"+mQuery);
							List<Object[]> blockconditionlistm = connutil.executequerys(em, mQuery);
							for (Object objectm : blockconditionlistm) {
								if(objectm != null && !objectm.equals("")){
									out.put("blockstatus", String.valueOf(objectm));
									System.out.println("objectm  "+objectm);
								}
							}
							
						}else{
							out.put("blockstatus", "0");
						}
						
						
						blockconditionList.add(out);
				 }
				 
				
				 
				 String checkblocktextQuery= env.getProperty("checkblocktextQuery");
					System.out.println("query ======"+checkblocktextQuery);
					List<String> blockconditiontextlist = connutil.executequerysgetStringop(em, checkblocktextQuery, String.valueOf(userid),  String.valueOf(follwingid));
					List<Object> blockconditiontextList = new ArrayList();
					if(blockconditiontextlist.size()>0){
					 for (String object2 : blockconditiontextlist) {
							Map<String, Object> outtext = new LinkedHashMap<>();
							if(object2 != null && !object2.equals("") && object2.length() > 0){
								outtext.put("blockstatustext", object2);
								System.out.println("object   "+object2);
							}else{
								outtext.put("blockstatustext", "0");
							}
							
							
							blockconditiontextList.add(outtext);
					 }
					}else{
						Map<String, Object> outtext = new LinkedHashMap<>();
						outtext.put("blockstatustext", "0");
						blockconditiontextList.add(outtext);
					}
					 Map<String, Object> out = new LinkedHashMap<>();
					 out.put("blockconditionlist", blockconditionList);
					 out.put("blockconditiontextlist", blockconditiontextList);
				 
				return out;
			}
			
			@Override
			public List<SMFileDTO> followingHomeList(String userId) {
				String mQuery= env.getProperty("checkblockconditionQuery");
                String blockedids = "0";

                List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userId));
                List<Object> blockconditionList = new ArrayList();
                 for (String object : blockconditionlist) {
                                Map<String, Object> out = new LinkedHashMap<>();
                                if(object != null && !object.equals("") && object.length() > 0){
                                        //out.put("blockstatus", object);
                                        String[] blockedidss = object.split(",");
                                    if(blockedidss.length > 0){
                                            for(int i = 0; i< blockedidss.length; i++){
                                                    blockedids = blockedidss[i] + ",";
                                            }
                                    }
                                    if(blockedids != null && blockedids.length()>0  ){
                                            blockedids = blockedids.substring(0, blockedids.length() - 1);
                                    }
                                        
                                        
                                }
                 }
				String Query= env.getProperty("followinghomelistquery");
				
				List<Object[]> orderlist = connutil.executequerytwobyid(em,Query,userId,blockedids);
				
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
					 smFileDTO.setLikeid(String.valueOf(object[17]));
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					 /* smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));*/
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					smFileDTO.setLikestatus(String.valueOf(object[18]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					smFileDTO.setPauseswitch(true);
					//smFileDTO.setTotalhomelistlength(String.valueOf(object[17]));
//					  smFileDTO.setFollowCount(String.valueOf(object[17]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
					smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
					smFileDTO.setFileFrom(String.valueOf(object[21]));
					smFileDTO.setDubsfileurl(String.valueOf(object[22]));
					String isprocessingcompleted = String.valueOf(object[23]);
					smFileDTO.setIsprocesscompleted(Integer.valueOf(isprocessingcompleted));
		             orderList.add(smFileDTO);
		     }

				return orderList;
			}
			
			
			@Override
			public Map<String, Object> checkfollowingdetails(String userid,
					String follwingid) {
				
				String Query= env.getProperty("checkfollowingQuery");
				List<Object[]> followinglist = connutil.executequerybyids(em, Query, String.valueOf(userid),  String.valueOf(follwingid));
				List<Object> followingList = new ArrayList();
				 for (Object[] object : followinglist) {
						Map<String, Object> out = new LinkedHashMap<>();
						out.put("id", String.valueOf(object[0]));
						out.put("status", String.valueOf(object[1]));
						out.put("acceptRequest", String.valueOf(object[2]));
						
						followingList.add(out);
				 }
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("followeringList", followingList);
				return out;
			}
			@Transactional
			@Override
			public Map<String, Object> unfollow(String userid, String follwingid) {
				followRepo.removeByUserIdAndFollowingId(Integer.valueOf(userid),Integer.valueOf(follwingid));
				
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("Type", "S");
				return out;
			}
			@Override
			public Map<String, Object> getlikesByfileid(int id,int userid) {
				String mQuery= env.getProperty("checkblockconditionQuery");
				String blockedids = "0";

				List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
				List<Object> blockconditionList = new ArrayList();
				 for (String object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null && !object.equals("") && object.length() > 0){
							//out.put("blockstatus", object);
							String[] blockedidss = object.split(",");
					    	if(blockedidss.length > 0){
					    		for(int i = 0; i< blockedidss.length; i++){
					    			blockedids = blockedidss[i] + ",";
					    		}
					    	}
					    	if(blockedids != null && blockedids.length()>0  ){
					    		blockedids = blockedids.substring(0, blockedids.length() - 1);
						    }
							
							
						}
				 }
				String Query= env.getProperty("alllikesquery");
				List<Object[]> likelist = connutil.executequerybythreeStringids(em, Query, String.valueOf(userid),String.valueOf(id),blockedids);
				List<Object> likeList = new ArrayList();
				 for (Object[] object : likelist) {
						Map<String, Object> out = new LinkedHashMap<>();
						out.put("userid", String.valueOf(object[0]));
						out.put("username", String.valueOf(object[1]));
						out.put("profilepath", String.valueOf(object[2]));
						out.put("status", String.valueOf(object[3]));
						out.put("blkstatus", String.valueOf(object[4]));
						likeList.add(out);
				 }
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("likeList", likeList);
				return out;
			}
			@Override
			public Map<String, Object> updateVideocomments(VideoCommentsDTO commentsInput) {
					
				String oprn = commentsInput.getOprn();
				System.out.println("commentsInput.getOprn() "+oprn);
				
				ModelMapper modelmapper = new ModelMapper();
				
				Map<String, Object> out = new LinkedHashMap<>();
				
				if(oprn.equals("UPD")){
					System.out.println("UPD UPD  UPD  UPD UPD ");
					VideoComments commentDetails = modelmapper.map(commentsInput, VideoComments.class);
					
					commentDetails.setCommentId(commentsInput.getCommentId());
					commentDetails.setComments(commentsInput.getComments());
					VideoComments output = commentsRepo.save(commentDetails);
					out.put("type", "S");
					out.put("message", "Comments updated successfully");
				}else{
					
					VideoComments commentDetails = modelmapper.map(commentsInput, VideoComments.class);
					commentDetails.setCommentId(commentsInput.getCommentId());
					VideoComments output = commentsRepo.deleteByCommentId(commentDetails.getCommentId());
					out.put("type", "S");
					out.put("message", "Comments deleted successfully");
				 }	
			
			   
				return out;
			}
			
			@Override
			public Map<String, Object> updateCommentsReply(CommentsReplyDTO commentsInput) {
					
				String oprn = commentsInput.getOprn();
				System.out.println("commentsInput.getOprn() "+oprn);
				
				ModelMapper modelmapper = new ModelMapper();
				
				Map<String, Object> out = new LinkedHashMap<>();
				
				if(oprn.equals("UPD")){
					System.out.println("UPD UPD  UPD  UPD UPD ");
					CommentsReply commentDetails = modelmapper.map(commentsInput, CommentsReply.class);
					
					commentDetails.setReplyId(commentsInput.getReplyId());
					commentDetails.setReply(commentsInput.getReply());
					CommentsReply output = commentsreplyRepo.save(commentDetails);
					out.put("type", "S");
					out.put("message", "Comments updated successfully");
				}else{
					System.out.println("DEL DEL  DEL  DEL DEL ");		
					
					CommentsReply commentDetails = modelmapper.map(commentsInput, CommentsReply.class);			
					commentDetails.setReplyId(commentsInput.getReplyId());
					CommentsReply output = commentsreplyRepo.deleteByReplyId(commentDetails.getReplyId());
					out.put("type", "S");
					out.put("message", "Comments deleted successfully");
				 }
				
				return out;
			}
			
			@Override
			public Map<String, Object> deletePost(SMFileDTO Input) {		

				Optional<SMFile> upd = fileUploadRepo.findByFileid(Long.valueOf(Input.getFileid()));
				if(upd.isPresent()){
					SMFile updstatus = upd.get();
					updstatus.setStatus(0);
					fileUploadRepo.save(updstatus);
				}
					
			 Map<String, Object> out = new LinkedHashMap<>();	
				out.put("type", "S");
				out.put("message", "Posts deleted successfully");
				return out;
			}
			
			@Override
			public List<SMFileDTO> followingHomeListwithlimits(String userId, String length) {

				String Query= env.getProperty("followinghomelistquery");
				int numlength = Integer.parseInt(length);
				List<Object[]> orderlist = connutil.executequerybythreevariable(em,Query,userId,userId, numlength);
				
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
					 smFileDTO.setLikeid(String.valueOf(object[17]));
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					  /*smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));*/
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					smFileDTO.setLikestatus(String.valueOf(object[18]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					smFileDTO.setPauseswitch(true);
					//smFileDTO.setTotalhomelistlength(String.valueOf(object[17]));
//					  smFileDTO.setFollowCount(String.valueOf(object[17]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
		            
					smFileDTO.setTimedesc(connutil.getTimedesc(String.valueOf(object[19]),String.valueOf(object[20]) ) );
					smFileDTO.setDuration(connutil.datecalculation(String.valueOf(object[19]),String.valueOf(object[20]) ) );
					
		             orderList.add(smFileDTO);
		     }

				return orderList;
			}
			@Override
			public List<SMFileDTO> getSearchPost(String name,int langid, String userid) {
				
				List<SMFile> fileList = fileUploadRepo.findAll();
				String mQuery= env.getProperty("checkblockconditionQuery");
				String blockedids = "0";

				List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
				List<Object> blockconditionList = new ArrayList();
				 for (String object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null && !object.equals("") && object.length() > 0){
							//out.put("blockstatus", object);
							String[] blockedidss = object.split(",");
					    	if(blockedidss.length > 0){
					    		for(int i = 0; i< blockedidss.length; i++){
					    			blockedids = blockedidss[i] + ",";
					    		}
					    	}
					    	if(blockedids != null && blockedids.length()>0  ){
					    		blockedids = blockedids.substring(0, blockedids.length() - 1);
						    }
							
							
						}
				 }
				String Query= env.getProperty("AllPostQuery");
				
				List<Object[]> orderlist = connutil.executequerybyidswithintegerandSTrings(em, Query, name,langid,userid,blockedids);
				System.out.println(orderlist.size());
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
		           
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
//					 smFileDTO.setLikeid(String.valueOf(object[17]));
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					  smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					smFileDTO.setPauseswitch(true);
					smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
					
		             orderList.add(smFileDTO);
		     }

				return orderList;
			}
			
			@Override
			public List<SMFileDTO> homelistwithparticularid(String fileid, String userid) {
				
				List<SMFile> fileList = fileUploadRepo.findAll();
				
				String Query= env.getProperty("homelistwithparticularidquery");
				int numfileid = Integer.parseInt(fileid);
				List<Object[]> orderlist = connutil.executequerybyidswithinteger(em,Query,userid,numfileid);
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
		            
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
					 smFileDTO.setLikeid("0");
					 
//					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
//					 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[9])));
//					 smFileDTO.setFilePoster(String.valueOf(object[10]));
//					 smFileDTO.setFollowCount(String.valueOf(object[11]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					  smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					smFileDTO.setPauseswitch(true);
					smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
					smFileDTO.setIndices(String.valueOf(numfileid));
					smFileDTO.setLikestatus(String.valueOf(object[18]));
					smFileDTO.setFileFrom(String.valueOf(object[19]));
					smFileDTO.setDubsfileurl(String.valueOf(object[20]));
					String isprocessingcompleted = String.valueOf(object[21]);
					smFileDTO.setIsprocesscompleted(Integer.valueOf(isprocessingcompleted));
		             orderList.add(smFileDTO);
		             numfileid = numfileid + 1;
				 }

				return orderList;
			}
			
			
			@Override
			public List<SMFileDTO> searchpostdestination(String firstfileid, String searchkeyword, String limitstart, String userid) {
				if(searchkeyword.equals("0")) {
					searchkeyword = "";
				}else if(searchkeyword.equals("null")) {
					searchkeyword = "";
				}else if(searchkeyword == null) {
					searchkeyword = "";
				}
				String mQuery= env.getProperty("checkblockconditionQuery");
				String blockedids = "0";

				List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
				List<Object> blockconditionList = new ArrayList();
				 for (String object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null && !object.equals("") && object.length() > 0){
							//out.put("blockstatus", object);
							String[] blockedidss = object.split(",");
					    	if(blockedidss.length > 0){
					    		for(int i = 0; i< blockedidss.length; i++){
					    			blockedids = blockedidss[i] + ",";
					    		}
					    	}
					    	if(blockedids != null && blockedids.length()>0  ){
					    		blockedids = blockedids.substring(0, blockedids.length() - 1);
						    }
							
							
						}
				 }
				List<SMFile> fileList = fileUploadRepo.findAll();
				System.out.println("firstfileid"+firstfileid+"searchkeyword"+searchkeyword+"limitstart"+limitstart+"userid"+userid);
				String Query= env.getProperty("searchpostdestinationquery");
				int numfileid = Integer.parseInt(limitstart);
				List<Object[]> orderlist = connutil.executequerywithFiveparams(em,Query,userid,searchkeyword,firstfileid,numfileid,blockedids);
				
				System.out.println("orderlist length"+orderlist);
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
		            
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
					 smFileDTO.setLikeid("0");
					 
//					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
//					 smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[9])));
//					 smFileDTO.setFilePoster(String.valueOf(object[10]));
//					 smFileDTO.setFollowCount(String.valueOf(object[11]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					  smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					smFileDTO.setPauseswitch(true);
					smFileDTO.setTotalhomelistlength(String.valueOf(orderlist.size()));
					smFileDTO.setIndices(String.valueOf(numfileid));
					smFileDTO.setLikestatus(String.valueOf(object[18]));
					smFileDTO.setDubsfileurl(String.valueOf(object[19]));
					smFileDTO.setFileFrom(String.valueOf(object[20]));
		             orderList.add(smFileDTO);
		             numfileid = numfileid + 1;
				 }

				return orderList;
			}
			@Override
			public List<SMFileDTO> getlimitedPostList(int length,String userid) {

				String mQuery= env.getProperty("checkblockconditionQuery");
				String blockedids = "0";

				List<String> blockconditionlist = connutil.executequerysStringop(em, mQuery, String.valueOf(userid));
				List<Object> blockconditionList = new ArrayList();
				 for (String object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null && !object.equals("") && object.length() > 0){
							//out.put("blockstatus", object);
							String[] blockedidss = object.split(",");
					    	if(blockedidss.length > 0){
					    		for(int i = 0; i< blockedidss.length; i++){
					    			blockedids = blockedidss[i] + ",";
					    		}
					    	}
					    	if(blockedids != null && blockedids.length()>0  ){
					    		blockedids = blockedids.substring(0, blockedids.length() - 1);
						    }
							
							
						}
				 }
				List<SMFile> fileList = fileUploadRepo.findAll();
				
				String Query= env.getProperty("limitedPostQuery");
				
				List<Object[]> orderlist = connutil.executequerybyidl(em, Query, length,blockedids);
				System.out.println("getlimitedPostList "+length);
				List<SMFileDTO> orderList = new ArrayList();
				 for (Object[] object : orderlist) {
		           
					 SMFileDTO smFileDTO = new SMFileDTO();
					 smFileDTO.setFileid(Long.valueOf(String.valueOf(object[0])));
					 smFileDTO.setFilename(String.valueOf(object[1]));
					 smFileDTO.setFileurl(String.valueOf(object[2]));
					 smFileDTO.setType(String.valueOf(object[3]));
					 
					 smFileDTO.setUserid(String.valueOf(object[4]));
					// smFileDTO.setFilename(String.valueOf(object[5]));
					 smFileDTO.setCreatedat(String.valueOf(object[6]));
					 smFileDTO.setTotallikes(connutil.numbertoKformat(String.valueOf(object[7])));
					// smFileDTO.setLikeid(String.valueOf(object[17]));
					 
					 smFileDTO.setTotalcomments(connutil.numbertoKformat(String.valueOf(object[8])));
					 smFileDTO.setImageurl(String.valueOf(object[9]));
					  smFileDTO.setUsername(String.valueOf(object[10]));
					  smFileDTO.setFilecaption(connutil.decodeStringUrl(String.valueOf(object[11])));
					  smFileDTO.setFilePoster(String.valueOf(object[12]));
					  smFileDTO.setFollowCount(String.valueOf(object[13]));
					  smFileDTO.setDuration(String.valueOf(object[14]));
					smFileDTO.setTimedesc(String.valueOf(object[15]));
					smFileDTO.setFileuploadername(String.valueOf(object[16]));
					 /* smFileDTO.setCommentid(String.valueOf(object[11]));
					 smFileDTO.setComments(String.valueOf(object[12]));*/
					smFileDTO.setPauseswitch(true);
					smFileDTO.setTotalhomelistlength(String.valueOf(fileList.size()));
					
		             orderList.add(smFileDTO);
		     }

				return orderList;
			}

			@Override
			public Map<String, Object> updateCompleted(String userid) {
				// TODO Auto-generated method stub
				Map<String, Object> out = new LinkedHashMap<>();
				List<SMFile> fileList = fileUploadRepo.findByUseridAndIsprocesscompleted(userid,0);
				 for (SMFile object : fileList) {
					
					 String filename = object.getFileurl();     // full file name
					 String[] parts = filename.split("\\."); // String array, each element is text between dots

					 String beforeFirstDot = parts[0];  
					 System.out.println("PROCESSING "+beforeFirstDot);
					 boolean obj =  connutil.CHECKtranscodingUploadFile(beforeFirstDot);
					 System.out.println("PROCESSING "+obj);
					 if(obj){
						 System.out.println("PROCESSING ==="+obj);
						   Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(object.getFileid());
						   if(orgnUpd.isPresent()){
							   SMFile upd = orgnUpd.get();
							   upd.setIsprocesscompleted(1);
							   fileUploadRepo.save(upd);
							   connutil.deleteFileFromS3Bucket(upd.getFileurl());
							   out.put("Type", "S");
							   out.put("message","Your post" +upd.getFilename() +" processed");
						   }else{
							   out.put("Type", "F");
							   out.put("message","No post here");
						   }
					 }
				 }
				return out;
			}

			@Override
			public Map<String, Object> getLanguageList() {
				String Query= env.getProperty("languageQuery");
				List<Object[]> languagelist = connutil.executequerys(em, Query);
				List<Object> LanguageList = new ArrayList();
				 for (Object[] object : languagelist) {
						Map<String, Object> out = new LinkedHashMap<>();
						out.put("langid", String.valueOf(object[0]));
						out.put("langname", String.valueOf(object[1]));
						out.put("langdesc", String.valueOf(object[2]));				
						LanguageList.add(out);
				 }
				 Map<String, Object> out = new LinkedHashMap<>();
				 out.put("LanguageList", LanguageList);
				return out;
			}
			@Override
			public Map<String, Object> checkuserfileisprocessing(String userid) {
				
				String Query= env.getProperty("checkuserfileisprocessingQuery");
				System.out.println("query ======"+Query);
				String fileisprocessing = "0";
				List<BigInteger> blockconditionlist = connutil.executequerysdashboardById(em, Query, String.valueOf(userid));
				List<Object> blockconditionList = new ArrayList();
				 for (BigInteger object : blockconditionlist) {
						Map<String, Object> out = new LinkedHashMap<>();
						if(object != null ){
							fileisprocessing = String.valueOf(object);
						}
				 }
				
					 Map<String, Object> out = new LinkedHashMap<>();
					 out.put("fileisprocessing", fileisprocessing);	
					 
				return out;
			}
			

			
}
