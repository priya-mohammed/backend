
package com.spantag.socialMediaAppln.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.crypto.spec.SecretKeySpec;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;

import com.spantag.socialMediaAppln.exceptions.ErrorResponse;
import com.spantag.socialMediaAppln.login.User;
import com.spantag.socialMediaAppln.uploadMedia.FileUploadRepository;
import com.spantag.socialMediaAppln.uploadMedia.SMFile;
import com.spantag.socialMediaAppln.uploadMedia.SMFileDTO;


import org.apache.commons.codec.binary.Base64;
@Configuration
@PropertySource("classpath:SocialMediaApplnQueryList.properties")
public class commonUtils {

	@Autowired
	EntityManager em;

	@Autowired
	ConnUtil conn;

	
	
	
	LinkedHashMap<String, Object> error = new LinkedHashMap<String, Object>();

	ResponseEntity<Object> response = null;

	public List<LinkedHashMap<String, Object>> isEmpty() {

		error.put("errorCode", "001");
		error.put("errorMessage", "No records Found for the Entered details");
		List<LinkedHashMap<String, Object>> outList = new ArrayList<>();
		outList.add(error);
		return outList;
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public List convertToList(ErrorResponse error) {

 		List li = new ArrayList<>();
		li.add(error);
		return li;
	}

	
	public static String getuploadfilepath(HttpServletRequest httpRequest,String form,int type,int id)
	{
		String modulename = form;
		String path=httpRequest.getRealPath("/");
		File fserverpath = new File(path);
		String rootpath = (fserverpath.getParent());
		path=rootpath.concat(File.separator).concat("documents").concat(File.separator).concat(modulename);
		path = path.concat(File.separator).concat("temp");
		//log.info("path   "+path);
		return path;
	}
	public static String getdelprofilefilepath(HttpServletRequest httpRequest,String form,int type,int id)
	{
		String modulename = form;
		String path=httpRequest.getRealPath("/");
		File fserverpath = new File(path);
		String rootpath = (fserverpath.getParent());
		path=rootpath.concat(File.separator).concat("documents").concat(File.separator).concat(modulename);
		path = path.concat(File.separator).concat("image");
		//log.info("path   "+path);
		return path;
	}
	public static String getuploadfilename(String filename,int id,String fileextension)
	{
	/*if(id == 0)
	  {*/
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder builder = new StringBuilder();
		int count = 15;
		while (count-- != 0) {
		int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
		builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		filename = builder.toString()+fileextension;
	  /*}*/
	return filename;
	}
	public static String getserverfilepath(HttpServletRequest httpRequest)
	{
		String path=httpRequest.getRealPath("/");
		File fserverpath = new File(path);
		String rootpath = (fserverpath.getParent());
		path=rootpath.concat(File.separator).concat("documents");
		return path;
	}
	
	public static String getrootpath(HttpServletRequest httpRequest)
	{
		String path=httpRequest.getRealPath("/");
		File fserverpath = new File(path);
		String rootpath = (fserverpath.getParent());
		
		return rootpath;
	}
	public static String getmodulename(String form,int type)
	{
		String modulename = null;
		if(form != null && form.equalsIgnoreCase("customer"))
		{
			if(type == 1)
			{
				modulename = "profile".concat(File.separator).concat("image");
			}
				
		}
		
		if(form != null && form.equalsIgnoreCase("jewellerytype"))
		{
			if(type == 1)
			{
				modulename = "order".concat(File.separator).concat("image");
			}
				
		}
		return modulename;
	}
	public static String filepath(String path) {
		int documentsIndex = path.indexOf("documents");
		String filePath = (path.substring(documentsIndex,path.length()));
		filePath = "lambyafiles".concat(File.separator).concat(filePath);
		
		return filePath;
	}
	
	public static String encrypt(String key, String initVector, String value) {
	    try {
	        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

	        byte[] encrypted = cipher.doFinal(value.getBytes());
	     

	        return Base64.encodeBase64String(encrypted);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }

	    return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
	    try {
	        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

	        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

	        return new String(original);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }

	    return null;
	}
	
	
}
