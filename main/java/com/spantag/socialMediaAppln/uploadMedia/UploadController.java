package com.spantag.socialMediaAppln.uploadMedia;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterOutputStream;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;








import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.spantag.socialMediaAppln.login.SignUpDTO;
import com.spantag.socialMediaAppln.login.SignUpRequest;
import com.spantag.socialMediaAppln.login.User;
import com.spantag.socialMediaAppln.login.UserRegisterRepository;
import com.spantag.socialMediaAppln.login.UserService;
import com.spantag.socialMediaAppln.utils.ConnUtil;
import com.spantag.socialMediaAppln.utils.ResponseInfo;
import com.spantag.socialMediaAppln.utils.ResponseInfo.ResponseType;
import com.spantag.socialMediaAppln.utils.commonUtils;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.ICodec;












import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




























//import sun.misc.BASE64Decoder;
import java.util.Base64;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "UPLOAD MODULE", description = "Uploading images and video files for the socialMedia app")
public class UploadController {
	
	@Autowired
	UploadService uploadService;
	
	@Autowired
	private FileUploadRepository fileUploadRepo;
	
	@Autowired
	private UserRegisterRepository userRepo;
	
	@Autowired
	private AuthenticationManager authenticationManager;


	@Autowired
	private ConnUtil connutil;
	
	@Autowired
    private AmazonS3 s3client;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private Environment env;


	
	ResponseEntity<Object> response = null;
	
	@PostMapping(value = "/compareDuetVideo", consumes = "multipart/form-data")
    public String compareDuetVideo(@RequestParam MultipartFile file,@RequestParam String userid,
    		@RequestParam String module,@RequestParam String filename, HttpSession session,
    		@RequestParam String filecontenttype,HttpServletRequest httpRequest,
    		@RequestParam String filecaption,@RequestParam String createdAt,
    		@RequestParam String modulefrom,@RequestParam String originalFileUrl,
    		@RequestParam String username,
    		@RequestParam int langid,@RequestParam String orginalfileid) {
		 String fileName = filename; 
		 String outfileName = commonUtils.getserverfilepath(httpRequest)+File.separator+"duet"+filename;
		  String rootLocation = commonUtils.getserverfilepath(httpRequest);
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			  fileName = rootLocation+File.separator+fileName;
			  System.out.println("fileName==="+fileName);
		   byte barr[];
		   try {
			barr = file.getBytes();
			  File uploadPath = new File(rootLocation.toString());
			  if(!uploadPath.exists())
		        {
		        	uploadPath.mkdirs();
		        }
			  BufferedOutputStream bout11=new BufferedOutputStream(  
		                 new FileOutputStream(outfileName));  
		        
		        bout11.write(barr);  
		        bout11.flush();  
		        bout11.close(); 
		       
		        Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(orginalfileid));
		        String[] cmd1={"ffmpeg","-i",commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl(),"-i",outfileName ,"-filter_complex"," nullsrc=size=960x854 [base];[0:v] setpts=PTS-STARTPTS, scale=480:854 [left]; [1:v] setpts=PTS-STARTPTS, scale=480:854 [right]; [base][left] overlay=shortest=1 [tmp1]; [tmp1][right] overlay=x=480","-f","mpegts","-codec:a","copy",fileName};
		        Process p= Runtime.getRuntime().exec(cmd1);
				   
				 InputStream in = p.getErrorStream();
				 int c;
				 while ((c = in.read()) != -1)
				 {
				     System.out.print((char)c);
				 }
				 in.close();
				 File deleteFile = new File(outfileName);
				 deleteFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				return orginalfileid;
		
	}
	
	@PostMapping(value = "/uploadDuetVideo", consumes = "multipart/form-data")
    public String uploadDuetVideo(@RequestParam MultipartFile file,@RequestParam String userid,
    		@RequestParam String module,@RequestParam String filename, HttpSession session,
    		@RequestParam String filecontenttype,HttpServletRequest httpRequest,
    		@RequestParam String filecaption,@RequestParam String createdAt,
    		@RequestParam String posterimage,@RequestParam String modulefrom,@RequestParam String originalFileUrl,
    		@RequestParam String username,
    		@RequestParam int langid,@RequestParam String orginalfileid) {
		   String rootLocation = null;
		   String fileName = filename; 
		   String logoFileName = filename;
		   String fileurl = null,posterurl =null,ffmpegvide = null,outpuffmpegvideologo=null;
			String posterImagelocation = null;
			long fileid = 0;
			
		    rootLocation = commonUtils.getserverfilepath(httpRequest);
		
			posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			posterurl = posterImagelocation;
			fileurl = rootLocation;
			String onlyFileName = filename;
			
			try {
			
				   File posterUploadPath = new File(posterImagelocation.toString());
					  System.out.println("posterUploadPath  :: " + posterUploadPath); 
					  System.out.println("posterUploadPath  :: " + posterUploadPath.exists());
				        if(!posterUploadPath.exists())
				        {
				        	posterUploadPath.mkdirs();
				        }
				        fileName = rootLocation+File.separator+fileName;
				        ffmpegvide =ffmpegvide +File.separator+fileName;
				        
				      
				     
						 
						   Optional<User> fileusername = userRepo.findByUserid(Long.valueOf(userid));
						   String userHandlename = "";
						   if(fileusername.get().getFullName() !=null && fileusername.get().getFullName().length() > 0 && fileusername.get().getFullName() !=""){
					        	 userHandlename = fileusername.get().getFullName();
					         }else{
					        	 userHandlename=fileusername.get().getUsername();
					         }
					        String tokenId = "@"+userHandlename;
					        String logoImagename = connutil.writeLogo(httpRequest,tokenId,userid);
					        String logoFilename = commonUtils.getserverfilepath(httpRequest)+File.separator+logoFileName;
//					    	  String[] cmd1={"ffmpeg","-i",fileName,"-ignore_loop","0","-i",commonUtils.getserverfilepath(httpRequest) + File.separator+"dingdong1.gif","-filter_complex","[1][0]scale2ref=w='iw*50/100':h='ow/mdar'[wm][vid];[vid][wm] overlay=-22:10 overlay=x=-100:y=-22:shortest=1,drawtext='text="+tokenId+":fontsize=(h/40):x=100:y=100-th:fontcolor=white:x=w-tw-10:y=10","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename};
					        String[] cmd1={"ffmpeg","-i",fileName,"-i",logoImagename,"-filter_complex","[1][0]scale2ref=w='iw*15/100':h='ow/mdar'[wm][vid];[vid][wm]overlay=10:10 overlay=x=-40:y=5","-f","mpegts","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename}; 
						    	System.out.println("priya === height less"+cmd1);
									 Process p1= Runtime.getRuntime().exec(cmd1);
								   
									 InputStream ins = p1.getErrorStream();
									 int c1;
									 while ((c1 = ins.read()) != -1)
									 {
									     System.out.print((char)c1);
									 }
									 ins.close();
									  File outputNmae = new File(logoFilename);
					         String filenamess = connutil.uploadFileToS3Bucket(outputNmae);
					         connutil.transcodingUploadFile(filenamess);
					         
					         outputNmae.delete();
					         File writelogopath = new File(logoImagename);
						        writelogopath.delete();
						        String pathsave[] = fileName.split("media");
						        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
						        LocalDateTime now = LocalDateTime.now();  
						        System.out.println("logo only");
						        SMFile fileupload = new SMFile();
						        fileupload.setFilename(onlyFileName);
						        //fileupload.setFileurl(pathsave[1]);
						        fileupload.setFileurl(filenamess);
						        fileupload.setType(module);
						        fileupload.setUserid(userid);
						        fileupload.setFilecaption(connutil.encodeStringUrl(filecaption));
						        fileupload.setFilecontenttype(filecontenttype);
						        fileupload.setCreatedAt(dtf.format(now));
						        fileupload.setStatus(1);
						        fileupload.setCreatedby(userid);
						        fileupload.setFileFrom("dubsmash");
						        fileupload.setLangid(langid);
						       
						        	fileupload.setIsprocesscompleted(0);
						     
						        SMFile outPut = fileUploadRepo.save(fileupload);
						        File fileExtension = new File(fileName);
				            	
				            	String fileNameExtension = fileExtension.getName();
				            	 System.out.println("fileNameExtension"+fileNameExtension);
				               
				                String extension= fileNameExtension.substring(fileNameExtension.lastIndexOf(".")+1);
				                System.out.println("fileNameExtension"+extension);
								Path yourFile = Paths.get(fileName);
								 System.out.println("yourFile"+yourFile);
								Files.move(yourFile, yourFile.resolveSibling(outPut.getFileid()+"."+extension));
								Optional<SMFile> upd = fileUploadRepo.findByFileid(outPut.getFileid());
								if(upd.isPresent()){
									fileid = outPut.getFileid();
									SMFile updFile = upd.get();
									fileurl=fileurl+File.separator+outPut.getFileid()+"."+extension;
									posterurl=posterurl+File.separator+outPut.getFileid()+"."+"png";
									 String updpathsave[] = fileurl.split("documents");
									 String posterupdpathsave[] = posterurl.split("documents");
									 String fileNameposter = posterurl;
									
									BufferedImage image = null;
									byte[] imageByte;

									//@SuppressWarnings("restriction")
//									BASE64Decoder decoder = new BASE64Decoder();
				//
//									imageByte = decoder.decodeBuffer(posterimage);
									imageByte = Base64.getDecoder().decode(posterimage);
									
									ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
									image = ImageIO.read(bis);
									bis.close();

									// write the image to a file
									File outputfile = new File(posterImagelocation.toString()+File.separator+"image.png");
									ImageIO.write(image, "png", outputfile);
							
									byte barrposter[];
									barrposter = Files.readAllBytes(outputfile.toPath());

							        fileNameposter = posterurl;
							        
							        System.out.println("ProfileImage  :: " + fileNameposter); 
							        File posterfiles = new File(fileNameposter);
							        File postyeruploadPath = new File(posterImagelocation.toString());

								    if(!postyeruploadPath.exists())
								        {
								        	postyeruploadPath.mkdirs();
								        }
							        if (posterfiles.exists()){
							        	posterfiles.delete();
							        }  
							        BufferedOutputStream boutposter=new BufferedOutputStream(  
							                 new FileOutputStream(fileNameposter));  
							        
							        boutposter.write(barrposter);  
							        boutposter.flush();  
							        boutposter.close(); 
							        File outputPoster = new File(fileNameposter);
							        String filenamessPOster = connutil.uploadFileToS3Bucket(outputPoster);
							        updFile.setFileurl(filenamess);
									updFile.setFilePoster(filenamessPOster);
									fileUploadRepo.save(updFile);

							    	posterfiles.delete();
							    	File videoname = new File(fileName);
							    	videoname.delete();
							    	File ffmpegvideo = new File(ffmpegvide);
							    	ffmpegvideo.delete();
							    	

									
								}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return orginalfileid;
		
	}
	@GetMapping("/getfriendstotaglist/{id}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> getfriendstotaglist(@PathVariable("id") int id) throws Exception {
		Map<String, Object> out = uploadService.getfriendstotaglist(id);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String uploadImage(@RequestParam MultipartFile file,@RequestParam MultipartFile audiofile, @RequestParam String userid,
    		@RequestParam String module,@RequestParam String audiosound,@RequestParam String micestatus,@RequestParam String filename, HttpSession session,
    		@RequestParam String filecontenttype,HttpServletRequest httpRequest,
    		@RequestParam String filecaption,@RequestParam String createdAt,
    		@RequestParam String posterimage,@RequestParam String filefrom,@RequestParam String modulefrom,@RequestParam String username,
    		@RequestParam int langid,@RequestParam String orginalfileid) {
		System.out.println("welcome to upload");
		System.out.println("inside"+filecontenttype);
		
		String rootLocation = null;
		String fileurl = null,posterurl =null,ffmpegvide = null,outpuffmpegvideologo=null;
		String posterImagelocation = null;
		  String logoFileName = filename;
		long fileid = 0;
       String audioPath = null;
       String videoPath = null;
		if(module.equals("image")){  
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			fileurl = rootLocation;
			posterurl = posterImagelocation;
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
		}
		else if(module.equals("video")){    
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			ffmpegvide =rootLocation;
			posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			posterurl = posterImagelocation;
			fileurl = rootLocation;
			
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
		}
		else {
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			rootLocation= rootLocation+File.separator+"images"+File.separator+module;
			posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
			fileurl = rootLocation;
			posterurl = posterImagelocation;
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/images"));
		}
		
		
		String nameExtension[] = file.getContentType().split("/");  
		String fileName = filename; 
		ffmpegvide=ffmpegvide+File.separator+fileName;
		String fileNameposter ; 
		String onlyFileName = filename;
		System.out.println("ProfileImage  :: " + fileName); 
		
		byte barr[];
		
		try {
			File files = null ;
			if(audiosound.equals("notmanualaudio")){
				if(micestatus.equals("true")){
					barr = file.getBytes();
					  //barr = compress(barr);

					  File uploadPath = new File(rootLocation.toString());
					  System.out.println("uploadPath  :: " + uploadPath); 
					  System.out.println("uploadPath  :: " + uploadPath.exists());
				        if(!uploadPath.exists())
				        {
				        	uploadPath.mkdirs();
				        }
				        File posterUploadPath = new File(posterImagelocation.toString());
						  System.out.println("posterUploadPath  :: " + posterUploadPath); 
						  System.out.println("posterUploadPath  :: " + posterUploadPath.exists());
					        if(!posterUploadPath.exists())
					        {
					        	posterUploadPath.mkdirs();
					        }
				        fileName = rootLocation+File.separator+fileName;
				         files = new File(fileName);
				        if (files.exists()){
				        	files.delete();
				        }  
				      /*  BufferedOutputStream bout=new BufferedOutputStream(  
				                 new FileOutputStream(ffmpegvide));  
				        
				        bout.write(barr);  
				        bout.flush();  
				        bout.close(); 
				        System.out.println("video uploaded ffmpegvide"+ffmpegvide);*/
				        
				        BufferedOutputStream bout11=new BufferedOutputStream(  
				                 new FileOutputStream(fileName));  
				        
				        bout11.write(barr);  
				        bout11.flush();  
				        bout11.close(); 
				        System.out.println("video uploaded "+fileName);
				}else{
					  barr = file.getBytes();
				         videoPath = commonUtils.getserverfilepath(httpRequest)+File.separator+"video"+logoFileName;
						 BufferedOutputStream bout1video=new BufferedOutputStream(  
				                 new FileOutputStream(videoPath)); 	
						 bout1video.write(barr);  
						 bout1video.flush();  
						 bout1video.close(); 
						   fileName = rootLocation+File.separator+fileName;
					    	IContainer container = IContainer.make();

							String videofilename = videoPath;
							System.out.println("fileexist.filename"+videofilename);
							if (container.open(videofilename, IContainer.Type.READ, null) < 0)
							  throw new IllegalArgumentException("could not open file: " + videofilename);

							
							int numStreams = container.getNumStreams();

							
							int videoStreamId = -1;
							IStreamCoder videoCoder = null;
							for (int i = 0; i < numStreams; i++) {
							
							  IStream stream = container.getStream(i);
							 
							  IStreamCoder coder = stream.getStreamCoder();

							  if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
							    videoStreamId = i;
							    videoCoder = coder;
							    break;
							  }
							}
							if (videoStreamId == -1)
							  throw new RuntimeException("could not find video stream in container: "
							      +filename);

							if (videoCoder.open() < 0)
							  throw new RuntimeException("could not open video decoder for container: "
							      + filename);

						
						
							long duration = container.getDuration();
							long seconds = (duration / 1000000);
							Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(orginalfileid));
							 String filepath = "";
							if(orgnUpd.get().getFileFrom().equals("upload")){
								  filepath = commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl();
					    	}else{
					    		 String dubsfilename = orgnUpd.get().getFileurl();     // full file name
								 String[] parts = dubsfilename.split("\\."); // String array, each element is text between dots

								 String beforeFirstDot = parts[0];  
					    		filepath="https://diingdong-assets.s3.ap-south-1.amazonaws.com/outputs/"+beforeFirstDot+"/"+beforeFirstDot+".m3u8";
					    	}
							
							  String[] cmd1={"ffmpeg","-i",videoPath,"-i", filepath,"-map","0:v","-map","1:a","-c","copy", "-t",String.valueOf(seconds),"-preset","ultrafast",fileName};
							  Process p= Runtime.getRuntime().exec(cmd1);
							   
								 InputStream in = p.getErrorStream();
								 int c;
								 while ((c = in.read()) != -1)
								 {
								     System.out.print((char)c);
								 }
								 in.close();
				}
			
			}else{
				 Random rand = new Random(); 
				 int rand_int1 = rand.nextInt(1000); 
				barr = audiofile.getBytes();
				 audioPath = commonUtils.getserverfilepath(httpRequest)+File.separator+"audio"+rand_int1+".mp3";
				 BufferedOutputStream bout11=new BufferedOutputStream(  
		                 new FileOutputStream(audioPath));  
		        
		        bout11.write(barr);  
		        bout11.flush();  
		        bout11.close(); 
				
		        barr = file.getBytes();
		         videoPath = commonUtils.getserverfilepath(httpRequest)+File.separator+"video"+logoFileName;
				 BufferedOutputStream bout1video=new BufferedOutputStream(  
		                 new FileOutputStream(videoPath));  
		        
				 bout1video.write(barr);  
				 bout1video.flush();  
				 bout1video.close(); 
				   fileName = rootLocation+File.separator+fileName;
			    	IContainer container = IContainer.make();

					String videofilename = videoPath;
					System.out.println("fileexist.filename"+videofilename);
					if (container.open(videofilename, IContainer.Type.READ, null) < 0)
					  throw new IllegalArgumentException("could not open file: " + videofilename);

					
					int numStreams = container.getNumStreams();

					
					int videoStreamId = -1;
					IStreamCoder videoCoder = null;
					for (int i = 0; i < numStreams; i++) {
					
					  IStream stream = container.getStream(i);
					 
					  IStreamCoder coder = stream.getStreamCoder();

					  if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
					    videoStreamId = i;
					    videoCoder = coder;
					    break;
					  }
					}
					if (videoStreamId == -1)
					  throw new RuntimeException("could not find video stream in container: "
					      +filename);

					if (videoCoder.open() < 0)
					  throw new RuntimeException("could not open video decoder for container: "
					      + filename);

				
				
					long duration = container.getDuration();
					long seconds = (duration / 1000000);
				
				  String[] cmd1={"ffmpeg","-i",videoPath,"-i", audioPath, "-t",String.valueOf(seconds),"-preset","ultrafast",fileName};
				  Process p= Runtime.getRuntime().exec(cmd1);
				   
					 InputStream in = p.getErrorStream();
					 int c;
					 while ((c = in.read()) != -1)
					 {
					     System.out.print((char)c);
					 }
					 in.close();
					 File videoPathfile = new File(videoPath);
				        videoPathfile.delete();
			}
			
		       
		        String outputffmpegvideo,filenamess = "";

		        if(audiosound.equals("manualaudio")){
		        File audiopathfile = new File(audioPath);
		        audiopathfile.delete();
		        File videoPathfile = new File(videoPath);
		        videoPathfile.delete();
		        }
		        Optional<User> fileusername = userRepo.findByUserid(Long.valueOf(userid));
		        String userHandlename = "";
		        if(fileusername.get().getFullName() !=null && fileusername.get().getFullName().length() > 0 && fileusername.get().getFullName() !=""){
		        	 userHandlename = fileusername.get().getFullName();
		         }else{
		        	 userHandlename=fileusername.get().getUsername();
		         }
		        String tokenId = "@"+userHandlename;
		        String logoImagename = connutil.writeLogo(httpRequest,tokenId,userid);
		        String logoFilename = commonUtils.getserverfilepath(httpRequest)+File.separator+logoFileName;
		    	  //String[] cmd1={"ffmpeg","-i",fileName,"-ignore_loop","0","-i",commonUtils.getserverfilepath(httpRequest) + File.separator+"dingdong1.gif","-filter_complex","[1][0]scale2ref=w='iw*50/100':h='ow/mdar'[wm][vid];[vid][wm] overlay=-22:10 overlay=x=-100:y=-22:shortest=1,drawtext='text="+tokenId+":fontsize=(h/40):x=100:y=100-th:fontcolor=white:x=w-tw-10:y=10","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename};
		        String[] cmd1={"ffmpeg","-i",fileName,"-i",logoImagename,"-filter_complex","[1][0]scale2ref=w='iw*15/100':h='ow/mdar'[wm][vid];[vid][wm]overlay=10:10 overlay=x=-40:y=5","-f","mpegts","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename}; 
			    	System.out.println("priya === height less"+cmd1);
						 Process p1= Runtime.getRuntime().exec(cmd1);
					   
						 InputStream ins = p1.getErrorStream();
						 int c1;
						 while ((c1 = ins.read()) != -1)
						 {
						     System.out.print((char)c1);
						 }
						 ins.close();
		        
						   File outputNmae = new File(logoFilename);
		         filenamess = connutil.uploadFileToS3Bucket(outputNmae);
		        /*streamingVideo(filenamess);*/
		        System.out.println("command filenamess "+filenamess);
		      if(filefrom.equals("dubsmash")){
		    	  connutil.transcodingUploadFile(filenamess);
		    	 
		      }
		      outputNmae.delete();
		      File writelogopath = new File(logoImagename);
		        writelogopath.delete();
		        String pathsave[] = fileName.split("media");
		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		        LocalDateTime now = LocalDateTime.now();  
		        System.out.println("logo only");
		        SMFile fileupload = new SMFile();
		        fileupload.setFilename(onlyFileName);
		        //fileupload.setFileurl(pathsave[1]);
		        fileupload.setFileurl(filenamess);
		        fileupload.setType(module);
		        fileupload.setUserid(userid);
		        fileupload.setFilecaption(connutil.encodeStringUrl(filecaption));
		        fileupload.setFilecontenttype(filecontenttype);
		        fileupload.setCreatedAt(dtf.format(now));
		        fileupload.setStatus(1);
		        fileupload.setCreatedby(userid);
		        fileupload.setFileFrom(filefrom);
		        fileupload.setLangid(langid);
		        if(modulefrom.equals("upload")) {
		        	fileupload.setIsprocesscompleted(1);
		        }else {
		        	fileupload.setIsprocesscompleted(0);
		        }
		        SMFile outPut = fileUploadRepo.save(fileupload);
		        File fileExtension = new File(fileName);
            	
            	String fileNameExtension = fileExtension.getName();
            	 System.out.println("fileNameExtension"+fileNameExtension);
               
                String extension= fileNameExtension.substring(fileNameExtension.lastIndexOf(".")+1);
                System.out.println("fileNameExtension"+extension);
				Path yourFile = Paths.get(fileName);
				 System.out.println("yourFile"+yourFile);
				Files.move(yourFile, yourFile.resolveSibling(outPut.getFileid()+"."+extension));
				Optional<SMFile> upd = fileUploadRepo.findByFileid(outPut.getFileid());
				if(upd.isPresent()){
					fileid = outPut.getFileid();
					SMFile updFile = upd.get();
					fileurl=fileurl+File.separator+outPut.getFileid()+"."+extension;
					posterurl=posterurl+File.separator+outPut.getFileid()+"."+"png";
					 String updpathsave[] = fileurl.split("documents");
					 String posterupdpathsave[] = posterurl.split("documents");
					
					
					BufferedImage image = null;
					byte[] imageByte;

					//@SuppressWarnings("restriction")
//					BASE64Decoder decoder = new BASE64Decoder();
//
//					imageByte = decoder.decodeBuffer(posterimage);
					imageByte = Base64.getDecoder().decode(posterimage);
					
					ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
					image = ImageIO.read(bis);
					bis.close();

					// write the image to a file
					File outputfile = new File(posterImagelocation.toString()+File.separator+"image.png");
					ImageIO.write(image, "png", outputfile);
			
					byte barrposter[];
					barrposter = Files.readAllBytes(outputfile.toPath());

			        fileNameposter = posterurl;
			        
			        System.out.println("ProfileImage  :: " + fileNameposter); 
			        File posterfiles = new File(fileNameposter);
			        File postyeruploadPath = new File(posterImagelocation.toString());

				    if(!postyeruploadPath.exists())
				        {
				        	postyeruploadPath.mkdirs();
				        }
			        if (posterfiles.exists()){
			        	posterfiles.delete();
			        }  
			        BufferedOutputStream boutposter=new BufferedOutputStream(  
			                 new FileOutputStream(fileNameposter));  
			        
			        boutposter.write(barrposter);  
			        boutposter.flush();  
			        boutposter.close(); 
			        File outputPoster = new File(fileNameposter);
			        String filenamessPOster = connutil.uploadFileToS3Bucket(outputPoster);
			        updFile.setFileurl(filenamess);
					updFile.setFilePoster(filenamessPOster);
					fileUploadRepo.save(updFile);

			    	posterfiles.delete();
			    	File videoname = new File(fileName);
			    	videoname.delete();
			       File deleteFilepath = new File(videoPath);
			       deleteFilepath.delete();

					
				}
				 
					
				
		} catch (IOException e) {
			
			//e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = " ";
		try {
			jsonString = mapper.writeValueAsString("["+fileid+"]");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//MultipartEntity reqEntity = new MultipartEntity();
		System.out.println("output  "+jsonString);
		return jsonString;
	}  
	
	
	
	@PostMapping(value = "/uploadbase64", consumes = "multipart/form-data")
    public String uploadbase64(@RequestParam String file, @RequestParam String userid,
    		@RequestParam String module,@RequestParam String filename, HttpSession session,
    		@RequestParam String filecontenttype,@RequestParam String filecaption,  @RequestParam int langid,
    		@RequestParam String createdAt, HttpServletRequest httpRequest) {
		System.out.println("inside"+filecontenttype);
		
		String rootLocation = null;
		String fileurl = null;
		String posterImagelocation = null;
		if(module.equals("image")){  
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			fileurl = rootLocation;
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
		}
		else if(module.equals("video")){    
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			posterImagelocation = rootLocation+File.separator+"media";
			rootLocation= rootLocation+File.separator+"media"+File.separator+module;
			
			fileurl = rootLocation;
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
		}
		else {
			rootLocation = commonUtils.getserverfilepath(httpRequest);
			rootLocation= rootLocation+File.separator+"images"+File.separator+module;
			fileurl = rootLocation;
			//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/images"));
		}
		
		
		// create a buffered image
				BufferedImage image = null;
				byte[] imageByte;

				//@SuppressWarnings("restriction")
				//BASE64Decoder base64Decoder = new BASE64Decoder();
				//BASE64Decoder decoder = base64Decoder;
				try {
					//imageByte = base64Decoder.decodeBuffer(file);
					imageByte = Base64.getDecoder().decode(file);
					ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
					image = ImageIO.read(bis);
					bis.close();

					// write the image to a file
					File outputfile = new File(rootLocation.toString()+File.separator+"image.png");
					ImageIO.write(image, "png", outputfile);
		
				String fileName = filename; 
				String onlyFileName = filename;
				System.out.println("ProfileImage  :: " + fileName); 
				
				byte barr[];
				barr = Files.readAllBytes(outputfile.toPath());
			//barr = file.getBytes();
			  //barr = compress(barr);

			  File uploadPath = new File(rootLocation.toString());
			  System.out.println("uploadPath  :: " + uploadPath); 
			  System.out.println("uploadPath  :: " + uploadPath.exists());
		        if(!uploadPath.exists())
		        {
		        	uploadPath.mkdirs();
		        }
		       
		        fileName = rootLocation+File.separator+fileName;
		        File files = new File(fileName);
		        if (files.exists()){
		        	files.delete();
		        }  
		        BufferedOutputStream bout=new BufferedOutputStream(  
		                 new FileOutputStream(fileName));  
		        
		        bout.write(barr);  
		        bout.flush();  
		        bout.close(); 
		        File outputNmae = new File(fileName);
		        String filenamess = connutil.uploadFileToS3Bucket(outputNmae);
		      
		       
		        String pathsave[] = fileName.split("media");
		       
		        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		        LocalDateTime now = LocalDateTime.now();  
		        System.out.println("logo only");
		        SMFile fileupload = new SMFile();
		        fileupload.setFilename(onlyFileName);
		        //fileupload.setFileurl(pathsave[1]);
		        fileupload.setFileurl(filenamess);
		        fileupload.setType(module);
		        fileupload.setUserid(userid);
		        fileupload.setFilecaption(connutil.encodeStringUrl(filecaption));
		        fileupload.setFilecontenttype(filecontenttype);
		        fileupload.setCreatedAt(dtf.format(now));
		        fileupload.setStatus(1);
		        fileupload.setCreatedby(userid);
		        fileupload.setIsprocesscompleted(1);
		        fileupload.setLangid(langid);
		        SMFile outPut = fileUploadRepo.save(fileupload);
		/*        File fileExtension = new File(fileName);
            	
            	String fileNameExtension = fileExtension.getName();
            	 System.out.println("fileNameExtension"+fileNameExtension);
               
                String extension= fileNameExtension.substring(fileNameExtension.lastIndexOf(".")+1);
                System.out.println("fileNameExtension"+extension);
				Path yourFile = Paths.get(fileName);
				 System.out.println("yourFile"+yourFile);
				Files.move(yourFile, yourFile.resolveSibling(outPut.getFileid()+"."+extension));
				Optional<SMFile> upd = fileUploadRepo.findByFileid(outPut.getFileid());
				if(upd.isPresent()){
					SMFile updFile = upd.get();
					fileurl=fileurl+File.separator+outPut.getFileid()+"."+extension;
					 String updpathsave[] = fileurl.split("documents");
					updFile.setFileurl(filenamess);
					if(module.equals("video")){
						 System.out.println("fileurl  "+fileurl);

						
					}
					fileUploadRepo.save(fileupload);
				}*/
				 files.delete();	 
					
				
		} catch (IOException e) {
			
			e.printStackTrace();
		}  
		
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = " ";
		try {
			jsonString = mapper.writeValueAsString("['status' : 'File Uploaded Successfully']");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//MultipartEntity reqEntity = new MultipartEntity();
		System.out.println("output  "+jsonString);
		
		return jsonString;
	}  
/*	public  String streamingVideo(String filename){
		Runtime rt = Runtime.getRuntime();
		String[] commands = {"system.exe", "transcoding "+filename};
		Process proc;
		try {
			proc = rt.exec(commands);
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(proc.getInputStream()));

				BufferedReader stdError = new BufferedReader(new 
				     InputStreamReader(proc.getErrorStream()));

				// Read the output from the command
				System.out.println("Here is the standard output of the command:\n");
				String s = null;
				while ((s = stdInput.readLine()) != null) {
				    System.out.println(s);
				}

				// Read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
				    System.out.println(s);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return filename;
		
		
	}*/
	public static byte[] compress(byte[] in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream defl = new DeflaterOutputStream(out);
            defl.write(in);
            defl.flush();
            defl.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }
    
	@GetMapping("/mygallery/{userid}/{length}")
    @ApiOperation(value = "mygallery")
        public String getMyGalleryList(@PathVariable("userid") String userid,
                        @PathVariable("length") String length) throws Exception {
                List<SMFileDTO> output = uploadService.getMyGalleryList(userid,length);
            response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
            ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(response.getBody());
                System.out.println("jsonString  "+jsonString);
            return jsonString; 
          
          }
	
	@GetMapping("/getVideoDetails/{fileId}")
    @ApiOperation(value = "mygallery")
	public String getVideoDetails(@PathVariable("fileId") String fileId,HttpServletRequest httpRequest) throws Exception {
	    String durationInSeconds=null;
		Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(fileId));
		if(orgnUpd.isPresent()){
			System.out.println("inside list ++++++++++++++");
			 File fileexist1 = new File(commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl());
			    if(!fileexist1.exists()){
			    	if(orgnUpd.get().getFileFrom().equals("upload")){
			    		 connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFileurl());
			    	}else{
			    		 String filename = orgnUpd.get().getFileurl();     // full file name
						 String[] parts = filename.split("\\."); // String array, each element is text between dots

						 String beforeFirstDot = parts[0]; 
			    		  //String[] cmd2={"ffmpeg","-i","https://diingdong-assets.s3.ap-south-1.amazonaws.com/outputs/"+beforeFirstDot+"/"+beforeFirstDot+".m3u8","-codec","copy",commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl()};
						 String[] cmd2={"ffmpeg","-i","https://diingdong-assets.s3.ap-south-1.amazonaws.com/outputs/"+beforeFirstDot+"/"+beforeFirstDot+".m3u8","-c","copy","-bsf:a","aac_adtstoasc",commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl()};
						 System.out.println("priya ==="+cmd2);
							 Process p;
							try {
								p = Runtime.getRuntime().exec(cmd2);
								 InputStream in = p.getErrorStream();
								 int c;
								 while ((c = in.read()) != -1)
								 {
								     System.out.print((char)c);
								 }
								 in.close();
							} catch (IOException e) {
								
							}
						   
							
			    	}
		       
			    }
			String path = commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl();
			System.out.println("inside list 2222222222 ++++++++++++++"+path);
			durationInSeconds=connutil.getvideoDuration(path);
			
		}
	    return durationInSeconds; 
	  
	  }
	
	@GetMapping("/homelist/{userid}/{langid}")
    @ApiOperation(value = "homelist")
	public String getMyhomelist(@PathVariable("userid") String userid,@PathVariable("langid") int langid) throws Exception {
		List<SMFileDTO> output = uploadService.getMyhomelist(userid,langid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	
	@GetMapping("/Enduserhomelist/{userid}/{fileid}")
    @ApiOperation(value = "Enduserhomelist")
	public String Enduserhomelist(@PathVariable("userid") String userid,@PathVariable("fileid") String fileid) throws Exception {
		List<SMFileDTO> output = uploadService.Enduserhomelist(userid,fileid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	
	
	@GetMapping("/homelistwithlimit/{length}/{userid}/{langid}")
	    @ApiOperation(value = "homelistwithlimit")
		public String homelistwithlimit(@PathVariable("length") String length, @PathVariable("userid") String userid,@PathVariable("langid") int langid) throws Exception {
			List<SMFileDTO> output = uploadService.getMyhomelistwithLimit(length,userid,langid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
	
	@GetMapping("/Enduserhomelistwithlimit/{length}/{userid}/{fileid}")
    @ApiOperation(value = "homelistwithlimit")
	public String Enduserhomelistwithlimit(@PathVariable("length") String length, @PathVariable("userid") String userid, @PathVariable("fileid") String fileid) throws Exception {
		List<SMFileDTO> output = uploadService.Enduserhomelistwithlimit(length,userid,fileid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	@GetMapping("/EnduserhomelistwithlimitHome/{length}/{userid}/{fileid}")
    @ApiOperation(value = "homelistwithlimit")
	public String EnduserhomelistwithlimitHome(@PathVariable("length") String length, @PathVariable("userid") String userid, @PathVariable("fileid") String fileid) throws Exception {
		List<SMFileDTO> output = uploadService.EnduserhomelistwithlimitHome(length,userid,fileid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	
	@GetMapping("/videolist/{videoid}")
    @ApiOperation(value = "videolist")
	public String getMyVideolist(@PathVariable("videoid") String videoid) throws Exception {
		System.out.println("videoid    "+videoid);
		List<SMFileDTO> output = uploadService.getMyVideolist(videoid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	
	@GetMapping("/videolistById/{videoid}")
    @ApiOperation(value = "videolist")
	public String videolistById(@PathVariable("videoid") String videoid) throws Exception {
		System.out.println("videoid    "+videoid);
		List<SMFileDTO> output = uploadService.videolistById(videoid);
	    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
	    ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
	    return jsonString; 
	  
	  }
	
	
	@GetMapping("/getFile/{fileid}") 
	  @ApiOperation(value = "videolist")
    public String getImage(
    		@PathVariable("fileid") String fileid, 
    		HttpSession session, HttpServletResponse response,HttpServletRequest httpRequest) {
		//System.out.println("welcome to streaming");
	    String encoded = null;
		   String rootLocation = null; 
		   Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(fileid));
		   
		   System.out.println("content type   "+orgnUpd.get().getFilecontenttype());
		   
          // path = Paths.get(session.getServletContext().getRealPath("/resources/media/").concat(module).concat(File.separator).concat(filename));  
//		   rootLocation = commonUtils.getserverfilepath(httpRequest);
//			rootLocation= rootLocation+File.separator+orgnUpd.get().getFileurl();
//			
//	        System.out.println("rootLocation  ==  " + rootLocation);  
	        response.setContentType(orgnUpd.get().getFilecontenttype()); 
	        ServletOutputStream out;  
	        String name =commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl();
	        System.out.println("content type name   "+name);
	        File existfile = new File(name);
	        if(!existfile.exists()){
	        	connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFileurl());
	        }
		   try {
			   String filepath = commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl();
				out = response.getOutputStream();
				 FileInputStream fin = new FileInputStream(filepath);  
			      
				    BufferedInputStream bin = new BufferedInputStream(fin);  
				    BufferedOutputStream bout = new BufferedOutputStream(out);  
				    int ch =0; ;  
				    while((ch=bin.read())!=-1)  
				    {  
				    bout.write(ch);  
				    } 
				  
				      
				    bin.close();  
				    fin.close();  
				    bout.close();  
				    out.close();  
				      
			} catch (IOException e) {
				// TODO Auto-generated catch block
				  System.out.println("content type error");
				
			}
	return null;  
	
		  
                
    }  
	
	
	@PostMapping(value = "/uploadProfile", consumes = "multipart/form-data")
	public String uploadProfile(@RequestParam MultipartFile file, @RequestParam String userid,
			@RequestParam String module,@RequestParam String filename,
			@RequestParam String filecontenttype,HttpServletRequest httpRequest) {
		System.out.println("inside"+filecontenttype);
		
		
		
		String rootLocation=commonUtils.getuploadfilepath(httpRequest, "profile", 0,0);
		
	
		String fileName = filename; 
		String path = null; 
		System.out.println("ProfileImage  :: " + fileName); 
		
		byte barr[];
		
		try {
			barr = file.getBytes();
			  File uploadPath = new File(rootLocation);
			  System.out.println("uploadPath  :: " + uploadPath); 
			  System.out.println("uploadPath  :: " + uploadPath.exists());
				if(!uploadPath.exists())
				{
					uploadPath.mkdirs();
				}
			   
				fileName = rootLocation+File.separator+fileName;
				File files = new File(fileName);
				if (files.exists()){
					files.delete();
				}  
				BufferedOutputStream bout=new BufferedOutputStream(  
						 new FileOutputStream(fileName));  
				
				bout.write(barr);  
				bout.flush();  
				bout.close(); 

		        File outputNmae = new File(fileName);
		         path = connutil.uploadFileToS3Bucket(outputNmae);
		         files.delete();
				/*String updpathsave[] = fileName.split("documents");
				path = updpathsave[1];*/
				System.out.println("filepath  "+path);
					 
					
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = " ";
		try {
			jsonString = mapper.writeValueAsString(""+path+"");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//MultipartEntity reqEntity = new MultipartEntity();
		System.out.println("output  "+jsonString);
		return jsonString;
	}  	
	
	
	
	@PostMapping("/addComments")
	@ApiOperation(value ="comments add for the particular video")
	public String addComments(@RequestBody VideoCommentsDTO commentsInput) throws Exception {
		
		Map<String, Object> output = uploadService.addComments(commentsInput);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	@PostMapping("/addCommentsReply")
	@ApiOperation(value ="comments add for the particular video")
	public String addCommentsReply(@RequestBody CommentsReply commentsInput) throws Exception {
		
		Map<String, Object> output = uploadService.addCommentsReply(commentsInput);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@PostMapping("/saveCommentsReply")
	@ApiOperation(value ="comments add for the particular video")
	public String saveCommentsReply(@RequestBody CommentsReply commentsInput) throws Exception {
		
		Map<String, Object> output = uploadService.saveCommentsReply(commentsInput);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@GetMapping("/getCommentsByVideoId/{videoId}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> getCommentsByVideoId(@PathVariable("videoId") int videoId) throws Exception {
		Map<String, Object> out = uploadService.getCommentsByVideoId(videoId);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	@PostMapping("/addlikes")
	@ApiOperation(value ="likes for the particular video")
	public String addlikes(@RequestBody videolikes commentsInput) throws Exception {
		
		Map<String, Object> output = uploadService.addlikes(commentsInput);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
//	@PostMapping(value = "/compress", consumes = "multipart/form-data")
//    public String compress(@RequestParam MultipartFile file
//    		) {
//		System.out.println("inside");
//		
//		FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
//		FFprobe ffprobe = new FFprobe("/path/to/ffprobe");
//
//		FFmpegBuilder builder = new FFmpegBuilder()
//
//		  .setInput("input.mp4")     // Filename, or a FFmpegProbeResult
//		  .overrideOutputFiles(true) // Override the output if it exists
//
//		  .addOutput("output.mp4")   // Filename for the destination
//		    .setFormat("mp4")        // Format is inferred from filename, or can be set
//		    .setTargetSize(250_000)  // Aim for a 250KB file
//
//		    .disableSubtitle()       // No subtiles
//
//		    .setAudioChannels(1)         // Mono audio
//		    .setAudioCodec("aac")        // using the aac codec
//		    .setAudioSampleRate(48_000)  // at 48KHz
//		    .setAudioBitRate(32768)      // at 32 kbit/s
//
//		    .setVideoCodec("libx264")     // Video using x264
//		    .setVideoFrameRate(24, 1)     // at 24 frames per second
//		    .setVideoResolution(640, 480) // at 640x480 resolution
//
//		    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
//		    .done();
//
//		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//
//		// Run a one-pass encode
//		executor.createJob(builder).run();
//
//		// Or run a two-pass encode (which is better quality at the cost of being slower)
//		executor.createTwoPassJob(builder).run();
//		return null;
//		
//
//	}  
	
	@GetMapping("/getComments/{videoid}/{length}/{type}/{commentid}")
	@ApiOperation(value = "getComments")
	public String getComments(@PathVariable("videoid") String videoid,
			@PathVariable("length") String length, @PathVariable("type") String type,
			@PathVariable("commentid") String commentid)
					throws Exception {
		System.out.println("videoid    "+videoid);
		List<SMFileDTO> output = uploadService.getComments(videoid,length,type,commentid);
		response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
		return jsonString; 
	  
	  }
	
	
	@GetMapping("/getCommentDetails/{commentid}/{length}/{type}/{replyid}")
	@ApiOperation(value = "getCommentDetails")
	public String getCommentDetails(@PathVariable("commentid") String commentid, 
			@PathVariable("length") String length,@PathVariable("type") String type,
			@PathVariable("replyid") String replyid) throws Exception {
		System.out.println("commentid    "+commentid);
		List<SMFileDTO> output = uploadService.getCommentDetails(commentid,length,type,replyid);
		response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
		return jsonString; 
	  
	  }
	
	@GetMapping("/getCommentMainDetails/{commentid}")
	@ApiOperation(value = "getCommentDetails")
	public String getCommentMainDetails(@PathVariable("commentid") String commentid) throws Exception {
		System.out.println("commentid    "+commentid);
		List<SMFileDTO> output = uploadService.getCommentMainDetails(commentid);
		response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
		return jsonString; 
	  
	  }
	
	@GetMapping("/getPosterImage/{fileid}/{type}")  
    public String getPosterImage(
    		@PathVariable("fileid") String fileid, @PathVariable("type") String type, 
    		HttpSession session, HttpServletResponse response,HttpServletRequest httpRequest) {
		   String rootLocation = null,contenttype = null; 
		   Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(fileid));
		   
		   System.out.println("content type   "+type);
		   
          // path = Paths.get(session.getServletContext().getRealPath("/resources/media/").concat(module).concat(File.separator).concat(filename));  
		   rootLocation = commonUtils.getserverfilepath(httpRequest);
		   System.out.println("type"+orgnUpd.get().getFilePoster());
		   if(type.equals("image")){
			   String filename = rootLocation+File.separator+orgnUpd.get().getFileurl();
			   File ifexist = new File(filename);
			   if(!ifexist.exists()){
				   connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFileurl()); 
			   }
			 
			rootLocation= rootLocation+File.separator+orgnUpd.get().getFileurl();
			contenttype = orgnUpd.get().getFilecontenttype();
		   }else{
			   String filename = rootLocation+File.separator+orgnUpd.get().getFilePoster();
			   File ifexist = new File(filename);
			   if(!ifexist.exists()){
			   connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFilePoster());
			   }
			   rootLocation= rootLocation+File.separator+orgnUpd.get().getFilePoster();
			   contenttype = "image/png";
		   }
	        System.out.println("rootLocation  ==  " + rootLocation);  
	        response.setContentType(contenttype); 
	        ServletOutputStream out;  
		    try {
				out = response.getOutputStream();
				 FileInputStream fin = new FileInputStream(rootLocation);  
			      
				    BufferedInputStream bin = new BufferedInputStream(fin);  
				    BufferedOutputStream bout = new BufferedOutputStream(out);  
				    int ch =0; ;  
				    while((ch=bin.read())!=-1)  
				    {  
				    bout.write(ch);  
				    }  
				      
				    bin.close();  
				    fin.close();  
				    bout.close();  
				    out.close();  
				      
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
		return "File uploaded successfully."; 
                
    } 
	
	@GetMapping("/getImageurl/{userid}")  
	public void getImageurl(@PathVariable("userid") long fileid, HttpServletResponse response,HttpServletRequest httpRequest){
		   String rootLocation = null; 
		   Optional<User> orgnUpd = userRepo.findByUserid(fileid);
		   
		  // path = Paths.get(session.getServletContext().getRealPath("/resources/media/").concat(module).concat(File.separator).concat(filename));  
		   rootLocation = commonUtils.getserverfilepath(httpRequest);
			//rootLocation= rootLocation+File.separator+orgnUpd.get().getFileurl();
			 
			response.setContentType(orgnUpd.get().getFiletype()); 
			connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getProfilePath());
			//String destination = rootLocation+File.separator+"profile"+File.separator+"image"+File.separator+orgnUpd.get().getProfilePath();
			String destination = rootLocation+File.separator+orgnUpd.get().getProfilePath();
			ServletOutputStream out;  
			try {
				out = response.getOutputStream();
				File file = new File(destination);
				if(file.exists()) {
					
				
				 FileInputStream fin = new FileInputStream(destination);  
				  
					BufferedInputStream bin = new BufferedInputStream(fin);  
					BufferedOutputStream bout = new BufferedOutputStream(out);  
					int ch =0; ;  
					while((ch=bin.read())!=-1)  
					{  
					bout.write(ch);  
					}  
					  
					bin.close();  
					fin.close();  
					bout.close();  
					out.close();  
				}  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}  
			
		//return "File uploaded successfully."; 
				
	}  
	@GetMapping("/getDuetvideo/{filename}")  
	public void getDuetvideo(@PathVariable("filename") String filename, HttpServletResponse response,HttpServletRequest httpRequest){
		System.out.println("filename= ===="+filename);
		String rootLocation=commonUtils.getserverfilepath(httpRequest)+File.separator+"media"+File.separator+"video";
		
			String destination = rootLocation+File.separator+filename+".mp4";
			System.out.println("destination= ===="+destination);
			ServletOutputStream out;  
			try {
				out = response.getOutputStream();
				 FileInputStream fin = new FileInputStream(destination);  
				  
					BufferedInputStream bin = new BufferedInputStream(fin);  
					BufferedOutputStream bout = new BufferedOutputStream(out);  
					int ch =0; ;  
					while((ch=bin.read())!=-1)  
					{  
						System.out.println("ch====="+ch);
					bout.write(ch);  
					}  
					  System.out.println("END");
					bin.close();  
					fin.close();  
					bout.close();  
					out.close();  
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
				
	}  
	@GetMapping("/getMyprofileGallery/{userid}")
	@ApiOperation(value = "getMyprofileGallery")
	public String getMyprofileGallery(@PathVariable("userid") String userid) throws Exception {
		
		List<SMFileDTO> output = uploadService.getMyprofileGallery(userid);
		response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		System.out.println("jsonString  "+jsonString);
		return jsonString; 
	  
	  }
	  
	@PostMapping("/addfollowing")
	@ApiOperation(value =" add for the particular video")
	public String addfollowing(@RequestBody FollowDTO input) throws Exception {
		
		Map<String, Object> output = uploadService.addfollowing(input);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@PostMapping("/acceptrequest")
	@ApiOperation(value =" add for the particular video")
	public String acceptrequest(@RequestBody FollowDTO input) throws Exception {
		
		Map<String, Object> output = uploadService.acceptrequest(input);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	

	@PostMapping("/confirmAcceptRequest")
	@ApiOperation(value =" add for the particular video")
	public String confirmAcceptRequest(@RequestBody FollowDTO input) throws Exception {
		
		Map<String, Object> output = uploadService.confirmAcceptRequest(input);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, output), HttpStatus.OK);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(response.getBody());
		return jsonString;
	}
	
	@GetMapping("/getfollowersdetails/{id}/{userid}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> getfollowersdetails(@PathVariable("id") String id,@PathVariable("userid") String userid) throws Exception {
		Map<String, Object> out = uploadService.getfollowersdetails(id,userid);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
	
	  
	@GetMapping("/getfollowingdetails/{id}")
	@ApiOperation(value = "get user details")
	public ResponseEntity<Object> getfollowingdetails(@PathVariable("id") int id
			) throws Exception {
		Map<String, Object> out = uploadService.getfollowingdetails(id);
		response = new ResponseEntity<Object>(new ResponseInfo(
				ResponseType.SUCCESS, out), HttpStatus.OK);

		return response;
	}
		@GetMapping("/followingHomeList/{userId}")
	    @ApiOperation(value = "homelist")
		public String followingHomeList(@PathVariable("userId") String userId) throws Exception {
			List<SMFileDTO> output = uploadService.followingHomeList(userId);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
		
		@GetMapping("/followingHomeListwithlimits/{userId}/{length}")
	    @ApiOperation(value = "homelist")
		public String followingHomeListwithlimits(@PathVariable("userId") String userId, 
				@PathVariable("length") String length) throws Exception {
			List<SMFileDTO> output = uploadService.followingHomeListwithlimits(userId,length);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
		
		@GetMapping("/checkfollowingdetails/{userid}/{follwingid}")
		@ApiOperation(value = "get user details")
		public ResponseEntity<Object> checkfollowingdetails(@PathVariable("userid") String userid,@PathVariable("follwingid") String follwingid) throws Exception {
			Map<String, Object> out = uploadService.checkfollowingdetails(userid,follwingid);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, out), HttpStatus.OK);

			return response;
		}
		
		@GetMapping("/unfollow/{userid}/{follwingid}")
		@ApiOperation(value = "get user details")
		public ResponseEntity<Object> unfollow(@PathVariable("userid") String userid,@PathVariable("follwingid") String follwingid) throws Exception {
			Map<String, Object> out = uploadService.unfollow(userid,follwingid);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, out), HttpStatus.OK);

			return response;
		}
		
		@GetMapping("/getlikesByfileid/{id}/{userid}")
		@ApiOperation(value = "get user details")
		public ResponseEntity<Object> getlikesByfileid(@PathVariable("id") int id,@PathVariable("userid") int userid) throws Exception {
			Map<String, Object> out = uploadService.getlikesByfileid(id,userid);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, out), HttpStatus.OK);

			return response;
		}
		
		@PostMapping("/updateVideocomments")
		@ApiOperation(value ="edit or delete comments added for the particular video")
		public String updateVideocomments(@RequestBody VideoCommentsDTO commentsInput) throws Exception {
			
			Map<String, Object> output = uploadService.updateVideocomments(commentsInput);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, output), HttpStatus.OK);
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			return jsonString;
		}
		
		
		@PostMapping("/updateCommentsReply")
		@ApiOperation(value ="edit or delete reply added for the particular comment")
		public String updateCommentsReply(@RequestBody CommentsReplyDTO commentsInput) throws Exception {
			
			Map<String, Object> output = uploadService.updateCommentsReply(commentsInput);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, output), HttpStatus.OK);
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			return jsonString;
		}
		@PostMapping("/deletePost")
		@ApiOperation(value =" deletePost")
		public String deletePost(@RequestBody SMFileDTO commentsInput) throws Exception {
			
			Map<String, Object> output = uploadService.deletePost(commentsInput);
			response = new ResponseEntity<Object>(new ResponseInfo(
					ResponseType.SUCCESS, output), HttpStatus.OK);
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			return jsonString;
		}
		
		@GetMapping("/getSearchPost/{name}/{langid}/{userid}")
	    @ApiOperation(value = "getSearchPost")
		public String getSearchPost(@PathVariable("name") String name,
				@PathVariable("langid") int langid,
				@PathVariable("userid") String userid) throws Exception {
			List<SMFileDTO> output = uploadService.getSearchPost(name,langid,userid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
		
		@GetMapping("/homelistwithparticularid/{fileid}/{userid}")
	    @ApiOperation(value = "homelistwithlimit")
		public String homelistwithparticularid(@PathVariable("fileid") String fileid, @PathVariable("userid") String userid) throws Exception {
			List<SMFileDTO> output = uploadService.homelistwithparticularid(fileid,userid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }

		@GetMapping("/searchpostdestination/{firstfileid}/{searchkeyword}/{limitstart}/{userid}")
	    @ApiOperation(value = "searchpostdestination")
		public String searchpostdestination(@PathVariable("firstfileid") String firstfileid, 
				@PathVariable("searchkeyword") String searchkeyword, @PathVariable("limitstart") String limitstart,
				@PathVariable("userid") String userid) throws Exception {
			List<SMFileDTO> output = uploadService.searchpostdestination(firstfileid,searchkeyword,limitstart,userid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
		
		@GetMapping("/getlimitedPostList/{length}/{userid}")
	    @ApiOperation(value = "getlimitedPostList")
		public String getlimitedPostList(@PathVariable("length") int length,
				@PathVariable("userid") String userid) throws Exception {
			List<SMFileDTO> output = uploadService.getlimitedPostList(length,userid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return jsonString; 
		  
		  }
		
		@GetMapping("/updateCompleted/{userid}")
	    @ApiOperation(value = "getlimitedPostList")
		public ResponseEntity<Object> updateCompleted(@PathVariable("userid") String userid) throws Exception {
			Map<String, Object> output = uploadService.updateCompleted(userid);
		    response = new ResponseEntity<Object>(new ResponseInfo(ResponseType.SUCCESS, output), HttpStatus.OK);
		    ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(response.getBody());
			System.out.println("jsonString  "+jsonString);
		    return response; 
		  
		  }
		 @GetMapping("/getLanguageList")
			@ApiOperation(value = "getLanguageList")
			public ResponseEntity<Object> getLanguageList() throws Exception {
				Map<String, Object> out = uploadService.getLanguageList();
				response = new ResponseEntity<Object>(new ResponseInfo(
						ResponseType.SUCCESS, out), HttpStatus.OK);

				return response;
			}
		 
		 @GetMapping("/getFileDownload/{fileid}/{dubsfileurl}/{username}") 
		  @ApiOperation(value = "videolist")
	    public String getFileDownload(
	    		@PathVariable("fileid") String fileid,@PathVariable("dubsfileurl") String dubsfileurl,@PathVariable("username") String username, 
	    		HttpSession session, HttpServletResponse response,HttpServletRequest httpRequest) {
			System.out.println("welcome to streaming");
		    String encoded = null;
			   String rootLocation = null; 
			   Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(fileid));
			   
			   System.out.println("content type   "+orgnUpd.get().getFilecontenttype());
				
			   Optional<User> fileusername = userRepo.findByUserid(Long.valueOf(orgnUpd.get().getUserid()));

//		        System.out.println("rootLocation  ==  " + rootLocation);  
		        response.setContentType(orgnUpd.get().getFilecontenttype()); 
		        ServletOutputStream out;  
		        File fileexist1 = new File(commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl());
			    if(!fileexist1.exists()){
			    	if(orgnUpd.get().getFileFrom().equals("upload")){
			    		 connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFileurl());
			    	}else{
			    		  String[] cmd2={"ffmpeg","-i","https://diingdong-assets.s3.ap-south-1.amazonaws.com/outputs/"+dubsfileurl+"/"+dubsfileurl+".m3u8","-codec","copy",commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl()};
							System.out.println("priya ==="+cmd2);
							 Process p;
							try {
								p = Runtime.getRuntime().exec(cmd2);
								 InputStream in = p.getErrorStream();
								 int c;
								 while ((c = in.read()) != -1)
								 {
								     System.out.print((char)c);
								 }
								 in.close();
							} catch (IOException e) {
								
							}
						   
							
			    	}
		       
			    }
			   try {
				   String filepath = commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl();
					System.out.println("filepathT==="+filepath);
				   out = response.getOutputStream();
				   
				   
				   
			  
				    String finsloutfilepath = commonUtils.getserverfilepath(httpRequest) + File.separator +fileid+".mp4";
				    File finsafileexist = new File(finsloutfilepath);
			    if(!finsafileexist.exists()){
				    	

				    		
				        String[] cmd2={"ffmpeg","-i",filepath,"-i",commonUtils.getserverfilepath(httpRequest) + File.separator+"mojvideo.mp4","-f","lavfi","-i","color","-filter_complex","[2][0]scale2ref[canvas][vid1];[canvas][1]scale2ref='max(iw,main_w)':'max(ih,main_h)'[canvas][vid2];[canvas]split=2[canvas1][canvas2];[canvas1][vid1]overlay=x='(W-w)/2':y='(H-h)/2':shortest=1[vid1];[canvas2][vid2]overlay=x='(W-w)/2':y='(H-h)/2':shortest=1[vid2];[vid1][vid2]concat=n=2:v=1:a=0,setsar=1" ,finsloutfilepath};
				    	System.out.println("priya === height less"+cmd2);
							 Process p= Runtime.getRuntime().exec(cmd2);
						   
							 InputStream in = p.getErrorStream();
							 int c;
							 while ((c = in.read()) != -1)
							 {
							     System.out.print((char)c);
							 }
							 in.close();
				    }
				
						
//				    
						 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
		return "merged successfully";  
		
			  
	                
	    }  
		 @GetMapping("/downloadallfile/{fileid}") 
		  @ApiOperation(value = "videolist")
	    public String downloadallfile(
	    		@PathVariable("fileid") String fileid,
	    		HttpSession session, HttpServletResponse response,HttpServletRequest httpRequest) {
			System.out.println("welcome to downloadallfile");
		    String encoded = null;
			   String rootLocation = null; 
			   Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(fileid));
			   
			   System.out.println("content type   "+orgnUpd.get().getFilecontenttype());
			   
	      
		        response.setContentType(orgnUpd.get().getFilecontenttype()); 
		        ServletOutputStream out;  
		        File fileexist1 = new File(commonUtils.getserverfilepath(httpRequest) + File.separator + orgnUpd.get().getFileurl());
			    if(!fileexist1.exists()){
		        connutil.downloadFileFromS3Bucket(httpRequest,orgnUpd.get().getFileurl());
			    }
			   try {
				   String filepath = commonUtils.getserverfilepath(httpRequest) + File.separator + fileid+".mp4";
					System.out.println("FILE PATH FOR S3 OBJECT==="+filepath);
				   out = response.getOutputStream();
					 FileInputStream fin = new FileInputStream(filepath);  
				      
					    BufferedInputStream bin = new BufferedInputStream(fin);  
					    BufferedOutputStream bout = new BufferedOutputStream(out);  
					    int ch =0; ;  
					    while((ch=bin.read())!=-1)  
					    {  
					    bout.write(ch);  
					    } 
					  
					      
					    bin.close();  
					    fin.close();  
					    bout.close();  
					    out.close();  
					  
					    
						 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
		return null;  
		
			  
	                
	    }  
		 @PostMapping(value = "/uploadVideo", consumes = "multipart/form-data")
		    public String uploadVideo(@RequestParam MultipartFile file,@RequestParam String userid,
		    		@RequestParam String module,@RequestParam String filename, HttpSession session,
		    		@RequestParam String filecontenttype,HttpServletRequest httpRequest,
		    		@RequestParam String filecaption,@RequestParam String createdAt,
		    		@RequestParam String posterimage,@RequestParam String filefrom,@RequestParam String modulefrom,@RequestParam String username,
		    		@RequestParam int langid,@RequestParam String orginalfileid) {
				System.out.println("welcome to uploadVideo");
				System.out.println("inside"+filecontenttype);
				
				String rootLocation = null;
				String fileurl = null,posterurl =null,ffmpegvide = null;
				String posterImagelocation = null;
				String logoFileName = filename;
				long fileid = 0;
		   
				if(module.equals("image")){  
					rootLocation = commonUtils.getserverfilepath(httpRequest);
					posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
					rootLocation= rootLocation+File.separator+"media"+File.separator+module;
					fileurl = rootLocation;
					posterurl = posterImagelocation;
					//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
				}
				else if(module.equals("video")){    
					rootLocation = commonUtils.getserverfilepath(httpRequest);
					ffmpegvide =rootLocation;
					posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
					rootLocation= rootLocation+File.separator+"media"+File.separator+module;
					posterurl = posterImagelocation;
					fileurl = rootLocation;
					
					//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/media/"+module));
				}
				else {
					rootLocation = commonUtils.getserverfilepath(httpRequest);
					rootLocation= rootLocation+File.separator+"images"+File.separator+module;
					posterImagelocation = rootLocation+File.separator+"media"+File.separator+"posterimage";
					fileurl = rootLocation;
					posterurl = posterImagelocation;
					//rootLocation = Paths.get(session.getServletContext().getRealPath("/resources/images"));
				}
				
				
				String nameExtension[] = file.getContentType().split("/");  
				String fileName = filename; 
				ffmpegvide=ffmpegvide+File.separator+fileName;
				String fileNameposter ; 
				String onlyFileName = filename;
				System.out.println("ProfileImage  :: " + fileName); 
				
				byte barr[];
				
				try {
					File files = null ;
					
						barr = file.getBytes();
						  //barr = compress(barr);

						  File uploadPath = new File(rootLocation.toString());
						  System.out.println("uploadPath  :: " + uploadPath); 
						  System.out.println("uploadPath  :: " + uploadPath.exists());
					        if(!uploadPath.exists())
					        {
					        	uploadPath.mkdirs();
					        }
					        File posterUploadPath = new File(posterImagelocation.toString());
							  System.out.println("posterUploadPath  :: " + posterUploadPath); 
							  System.out.println("posterUploadPath  :: " + posterUploadPath.exists());
						        if(!posterUploadPath.exists())
						        {
						        	posterUploadPath.mkdirs();
						        }
					        fileName = rootLocation+File.separator+fileName;
					         files = new File(fileName);
					        if (files.exists()){
					        	files.delete();
					        }  
					     
					        
					        BufferedOutputStream bout11=new BufferedOutputStream(  
					                 new FileOutputStream(fileName));  
					        
					        bout11.write(barr);  
					        bout11.flush();  
					        bout11.close(); 
					        System.out.println("video uploaded "+fileName);
					
				       
				        String filenamess = "";
				        Optional<User> fileusername = userRepo.findByUserid(Long.valueOf(userid));
				        String userHandlename = "";
				         if(fileusername.get().getFullName() !=null && fileusername.get().getFullName().length() > 0 && fileusername.get().getFullName() !=""){
				        	 userHandlename = fileusername.get().getFullName();
				         }else{
				        	 userHandlename=fileusername.get().getUsername();
				         }
				        String tokenId = "@"+userHandlename;
				        String logoImagename = connutil.writeLogo(httpRequest,tokenId,userid);
				        String logoFilename = commonUtils.getserverfilepath(httpRequest)+File.separator+logoFileName;
				    	  //String[] cmd1={"ffmpeg","-i",fileName,"-ignore_loop","0","-i",commonUtils.getserverfilepath(httpRequest) + File.separator+"dingdong1.gif","-filter_complex","[1][0]scale2ref=w='iw*50/100':h='ow/mdar'[wm][vid];[vid][wm] overlay=-22:10 overlay=x=-100:y=-22:shortest=1,drawtext='text="+tokenId+":fontsize=(h/40):x=100:y=100-th:fontcolor=white:x=w-tw-10:y=10","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename};
				        String[] cmd1={"ffmpeg","-i",fileName,"-i",logoImagename,"-filter_complex","[1][0]scale2ref=w='iw*15/100':h='ow/mdar'[wm][vid];[vid][wm]overlay=10:10 overlay=x=-40:y=5","-codec:a","copy","-preset","fast","-async","1","-qscale","0",logoFilename};
					    	System.out.println("priya === height less"+cmd1);
								 Process p1= Runtime.getRuntime().exec(cmd1);
							   
								 InputStream ins = p1.getErrorStream();
								 int c1;
								 while ((c1 = ins.read()) != -1)
								 {
								     System.out.print((char)c1);
								 }
								 ins.close();
				        
								   File outputNmae = new File(logoFilename);
				     
				        
				         filenamess = connutil.uploadFileToS3Bucket(outputNmae);
				        /*streamingVideo(filenamess);*/
				        System.out.println("command filenamess "+filenamess);
				        connutil.transcodingUploadFile(filenamess);
				      
				        outputNmae.delete();
				        File writelogopath = new File(logoImagename);
				        writelogopath.delete();
				        String pathsave[] = fileName.split("media");
				        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
				        LocalDateTime now = LocalDateTime.now();  
				        System.out.println(dtf.format(now));  
				        System.out.println("logo only");
				        SMFile fileupload = new SMFile();
				        fileupload.setFilename(onlyFileName);
				        //fileupload.setFileurl(pathsave[1]);
				        fileupload.setFileurl(filenamess);
				        fileupload.setType(module);
				        fileupload.setUserid(userid);
				        fileupload.setFilecaption(connutil.encodeStringUrl(filecaption));
				        fileupload.setFilecontenttype(filecontenttype);
				        fileupload.setCreatedAt(dtf.format(now));
				        fileupload.setStatus(1);
				        fileupload.setCreatedby(userid);
				        fileupload.setFileFrom(filefrom);
				        fileupload.setLangid(langid);
				        if(modulefrom.equals("upload")) {
				        	fileupload.setIsprocesscompleted(1);
				        }else {
				        	fileupload.setIsprocesscompleted(0);
				        }
				        SMFile outPut = fileUploadRepo.save(fileupload);
				        File fileExtension = new File(fileName);
		            	
		            	String fileNameExtension = fileExtension.getName();
		            	 System.out.println("fileNameExtension"+fileNameExtension);
		               
		                String extension= fileNameExtension.substring(fileNameExtension.lastIndexOf(".")+1);
		                System.out.println("fileNameExtension"+extension);
						Path yourFile = Paths.get(fileName);
						 System.out.println("yourFile"+yourFile);
						Files.move(yourFile, yourFile.resolveSibling(outPut.getFileid()+"."+extension));
						Optional<SMFile> upd = fileUploadRepo.findByFileid(outPut.getFileid());
						if(upd.isPresent()){
							fileid = outPut.getFileid();
							SMFile updFile = upd.get();
							fileurl=fileurl+File.separator+outPut.getFileid()+"."+extension;
							posterurl=posterurl+File.separator+outPut.getFileid()+"."+"png";
							 String updpathsave[] = fileurl.split("documents");
							 String posterupdpathsave[] = posterurl.split("documents");
							
							
							BufferedImage image = null;
							byte[] imageByte;


							imageByte = Base64.getDecoder().decode(posterimage);
							
							ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
							image = ImageIO.read(bis);
							bis.close();

							// write the image to a file
							File outputfile = new File(posterImagelocation.toString()+File.separator+"image.png");
							ImageIO.write(image, "png", outputfile);
					
							byte barrposter[];
							barrposter = Files.readAllBytes(outputfile.toPath());

					        fileNameposter = posterurl;
					        
					        System.out.println("ProfileImage  :: " + fileNameposter); 
					        File posterfiles = new File(fileNameposter);
					        File postyeruploadPath = new File(posterImagelocation.toString());

						    if(!postyeruploadPath.exists())
						        {
						        	postyeruploadPath.mkdirs();
						        }
					        if (posterfiles.exists()){
					        	posterfiles.delete();
					        }  
					        BufferedOutputStream boutposter=new BufferedOutputStream(  
					                 new FileOutputStream(fileNameposter));  
					        
					        boutposter.write(barrposter);  
					        boutposter.flush();  
					        boutposter.close(); 
					        File outputPoster = new File(fileNameposter);
					        String filenamessPOster = connutil.uploadFileToS3Bucket(outputPoster);
					        updFile.setFileurl(filenamess);
							updFile.setFilePoster(filenamessPOster);
							fileUploadRepo.save(updFile);
				
					    	posterfiles.delete();
					    	File videoname = new File(fileName);
					    	videoname.delete();
					       
			
							
						}
						 
							
						
				} catch (IOException e) {
					
					//e.printStackTrace();
				}
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = " ";
				try {
					jsonString = mapper.writeValueAsString("["+fileid+"]");
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//MultipartEntity reqEntity = new MultipartEntity();
				System.out.println("output  "+jsonString);
				return jsonString;
			}  
			
		 @PostMapping("/removeblock")
			@ApiOperation(value =" add for the particular video")
			public String removeblock(@RequestBody FollowDTO input) throws Exception {
				
				Map<String, Object> output = uploadService.removeblock(input);
				response = new ResponseEntity<Object>(new ResponseInfo(
						ResponseType.SUCCESS, output), HttpStatus.OK);
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(response.getBody());
				return jsonString;
			}
			
			@PostMapping("/blockperson")
			@ApiOperation(value =" add for the particular video")
			public String blockperson(@RequestBody FollowDTO input) throws Exception {
				
				Map<String, Object> output = uploadService.blockperson(input);
				response = new ResponseEntity<Object>(new ResponseInfo(
						ResponseType.SUCCESS, output), HttpStatus.OK);
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(response.getBody());
				return jsonString;
			}
			@GetMapping("/checkblockconditions/{userid}/{follwingid}")
			@ApiOperation(value = "get user details")
			public ResponseEntity<Object> checkblockconditions(@PathVariable("userid") String userid,@PathVariable("follwingid") String follwingid) throws Exception {
				Map<String, Object> out = uploadService.checkblockconditions(userid,follwingid);
				response = new ResponseEntity<Object>(new ResponseInfo(
						ResponseType.SUCCESS, out), HttpStatus.OK);

				return response;
			}
			@GetMapping("/checkuserfileisprocessing/{userid}")
			@ApiOperation(value = "check user file is processing")
			public ResponseEntity<Object> checkuserfileisprocessing(@PathVariable("userid") String userid) throws Exception {
				Map<String, Object> out = uploadService.checkuserfileisprocessing(userid);
				response = new ResponseEntity<Object>(new ResponseInfo(
						ResponseType.SUCCESS, out), HttpStatus.OK);

				return response;
			}
}
