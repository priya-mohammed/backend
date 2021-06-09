package com.spantag.socialMediaAppln.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

public class ViewImagesServlet  extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	private String filePath;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewImagesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		    String pathInfo = request.getPathInfo();
			String columnNames = pathInfo;
			StringTokenizer columnToken = new StringTokenizer(columnNames,"/");
			String form="",type="",id="",filename="";
			ArrayList<String> ar = new ArrayList<String>();
			while(columnToken.hasMoreTokens())
			{
				String columnName = columnToken.nextToken();
				ar.add(columnName);
			}
			form = ar.get(1);
			type = ar.get(2);
			filename = ar.get(3);
			StringTokenizer fileToken = new StringTokenizer(filename,".");
			ArrayList<String> arListFile = new ArrayList<String>();
			while(fileToken.hasMoreTokens())
			{
				String columnName = fileToken.nextToken();
				arListFile.add(columnName);
			}
			id=arListFile.get(0);
			
			String FileType = arListFile.get(1);
			
			if(FileType.equals("jpg")){
				response.setContentType("image/jpeg"); 
			}
			else{
				response.setContentType("application/docx"); 
			}
			
			int intid = 0;

			if(id != null)
			{
				intid = Integer.parseInt(id);
			}
			
			String modulename = form.concat(File.separator).concat(type);
			
			String path=request.getRealPath("/");
			
			File fserverpath = new File(path);
			String rootpath = (fserverpath.getParent());
			path=rootpath.concat(File.separator).concat("documents").concat(File.separator).concat(modulename);
			
			System.out.println("path============"+path);
			
			path = path.concat(File.separator).concat(id+"."+FileType);
			System.out.println("path1============"+path);
			if(!new File(path).exists())
			{
				if(form != null && !form.equalsIgnoreCase("productimport"))
				{
					
					path = request.getRealPath("/").concat(File.separator).concat("resources").concat(File.separator).concat("images").
							concat(File.separator).concat("noimage.png");	
				}
				else if(form != null && !form.equalsIgnoreCase("gallery"))
				{
				
					
					
					path = request.getRealPath("/").concat(File.separator).concat("resources").concat(File.separator).concat("galleryimage").
							concat(File.separator).concat("noimage.png");	
				}
				else
				{
					
					path = request.getRealPath("/").concat(File.separator).concat(File.separator).concat("resources").concat(File.separator).concat("images").
							concat(File.separator).concat("excel.png");
				}
			}
			

			 
		    ServletOutputStream out;  
		    out = response.getOutputStream();  
		    FileInputStream fin = new FileInputStream(path);  
		      
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

		
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		}

}
