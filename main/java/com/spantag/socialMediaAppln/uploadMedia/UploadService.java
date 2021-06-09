
package com.spantag.socialMediaAppln.uploadMedia;

import java.util.List;
import java.util.Map;

import org.bytedeco.javacpp.presets.opencv_core.Str;

public interface UploadService {
	public List<SMFileDTO> getMyGalleryList(String hid, String length);
    public List<SMFileDTO> getMyhomelist(String userid,int langid);
    public List<SMFileDTO> Enduserhomelist(String userid,String fileid);
	public List<SMFileDTO> getMyhomelistwithLimit(String length, String userid,int langid);
	public List<SMFileDTO> Enduserhomelistwithlimit(String length, String userid,String fileid);
	public List<SMFileDTO> EnduserhomelistwithlimitHome(String length, String userid,String fileid);
	public List<SMFileDTO> getMyVideolist(String videoid);
	public Map<String, Object> addComments(VideoCommentsDTO commentsInput);
	public Map<String, Object> getCommentsByVideoId(int videoId);
	public Map<String, Object> addlikes(videolikes commentsInput);
	public List<SMFileDTO> getComments(String videoid, String length, String type, String commentid);
	public Map<String, Object> addCommentsReply(CommentsReply commentsInput);
	public List<SMFileDTO> getCommentDetails(String commentid, String length, String type, String replyid);
	public List<SMFileDTO> getCommentMainDetails(String commentid);
	public Map<String, Object> saveCommentsReply(CommentsReply commentsInput);
	public List<SMFileDTO> getMyprofileGallery(String userid);
	public Map<String, Object> addfollowing(FollowDTO input);
	public Map<String, Object> getfollowersdetails(String id,String userid);
	public Map<String, Object> getfollowingdetails(int id);
	public List<SMFileDTO> followingHomeList(String userId);
	public List<SMFileDTO> followingHomeListwithlimits(String userId, String length);
	public Map<String, Object> checkfollowingdetails(String userid,String follwingid);
	public Map<String, Object> unfollow(String userid, String follwingid);
	public Map<String, Object> getlikesByfileid(int id,int userid);
	public Map<String, Object> updateVideocomments(VideoCommentsDTO commentsInput);
	public Map<String, Object> updateCommentsReply(CommentsReplyDTO commentsInput);
	public Map<String, Object> deletePost(SMFileDTO posts);
	public List<SMFileDTO> getSearchPost(String name,int langid, String userid);
	public List<SMFileDTO> homelistwithparticularid(String fileid, String userid);
	public List<SMFileDTO> searchpostdestination(String firstfileid, String searchkeyword, String limitstart, String userid);
	public List<SMFileDTO> getlimitedPostList(int length, String userid);
	public Map<String, Object> updateCompleted(String userid);
	public Map<String, Object> getLanguageList();
	public List<SMFileDTO> videolistById(String videoid);
	public Map<String, Object> acceptrequest(FollowDTO input);
	public Map<String, Object> confirmAcceptRequest(FollowDTO input);
	public Map<String, Object> getfriendstotaglist(int id);
	public Map<String, Object> removeblock(FollowDTO input);
	public Map<String, Object> blockperson(FollowDTO input);
	public Map<String, Object> checkuserfileisprocessing(String userid);
    public Map<String, Object> checkblockconditions(String userid,String follwingid);
}
