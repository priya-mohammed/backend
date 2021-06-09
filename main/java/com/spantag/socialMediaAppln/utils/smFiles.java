//
//
//package  com.spantag.socialMediaAppln.utils;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.StringTokenizer;
//
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceUnit;
//import javax.persistence.Query;
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.spantag.socialMediaAppln.uploadMedia.FileUploadRepository;
//import com.spantag.socialMediaAppln.uploadMedia.SMFile;
//
//
//@WebServlet("/getFile/*")
//public class smFiles extends HttpServlet{
//	private EntityManager entityManager;
//
//    @PersistenceContext
//    public void setEntityManager(EntityManager entityManager){ 
//        this.entityManager = entityManager; 
//    }
//	
//	 @Override
//	    public void doGet(HttpServletRequest httpRequest, HttpServletResponse response
//	    		) throws IOException {
//		 System.out.println("welcome to files");
//		
//		
////		        EntityManager em = emf.createEntityManager();
//		        queryTo(httpRequest,response);
//	    }
//	
//		private  void queryTo(HttpServletRequest httpRequest, 
//				HttpServletResponse response) {
//	    	 String pathInfo = httpRequest.getPathInfo();
//			 System.out.println("welcome to files"+pathInfo);
//		     pathInfo = pathInfo.replace("/", ""); 
//		     System.out.println("welcome to files"+pathInfo);
//			 String rootLocation = null; 
//			 //EntityManager em = null;
//			
//			 
//			 Query que = entityManager.createNativeQuery("SELECT * FROM sm_file_upload WHERE sm_fileid="+pathInfo); 
//				List<Object[]> resultList = que.getResultList();
//				for (Object[] object : resultList) {
//					   System.out.println("content type   "+object[2]);
//					   
//			           // path = Paths.get(session.getServletContext().getRealPath("/resources/media/").concat(module).concat(File.separator).concat(filename));  
//					   rootLocation = commonUtils.getserverfilepath(httpRequest);
//						rootLocation= rootLocation+File.separator+"media"+object[2];
//						
//				        System.out.println("rootLocation  ==  " + rootLocation);  
//				        response.setContentType(object[4].toString()); 
//				        ServletOutputStream out;  
//					    try {
//							out = response.getOutputStream();
//							 FileInputStream fin = new FileInputStream(rootLocation);  
//						      
//							    BufferedInputStream bin = new BufferedInputStream(fin);  
//							    BufferedOutputStream bout = new BufferedOutputStream(out);  
//							    int ch =0; ;  
//							    while((ch=bin.read())!=-1)  
//							    {  
//							    bout.write(ch);  
//							    }  
//							      
//							    bin.close();  
//							    fin.close();  
//							    bout.close();  
//							    out.close();  
//							      
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}  	
//					
//		     }
//			   //Optional<SMFile> orgnUpd = fileUploadRepo.findByFileid(Long.valueOf(pathInfo));
//			   
//			   
//	    }
//}
