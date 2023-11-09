package com.lcwd.electronic.store.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.entities.Log4jRequest;
import com.lcwd.electronic.store.properties.LoggerProperties;
import com.lcwd.electronic.store.services.FileService;
import com.github.underscore.U;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
		
	@Autowired
	private LoggerProperties prop;
	
	@Autowired
	private FileService fileService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/log4json")
	public String getLogDetails() throws IOException {
		//String fullPath = prop.getLog4jFilePath() + "log4j2.xml";
		XML xml = new XMLDocument(prop.getResourceLog4j().getInputStream());
		String xmlString = xml.toString();
		return U.xmlToJson(xmlString);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/download/log")
	public void downloadLogFile(HttpServletResponse response) throws IOException{
		String logFileName = "appserver.log";
		if(logFileName != null && logFileName.length()>0) {
			InputStream inputStream = fileService.getResource(prop.getAppserverlogPath(), logFileName);
			response.setContentType("application/txt");
			response.setHeader("Content-Disposition", "attachment; filename=\"appserver.log\"");
			int nRead;
			   while ((nRead = inputStream.read()) != -1) {
			       response.getWriter().write(nRead);
			   }
			inputStream.close();
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/log")
	public ResponseEntity<ApiResponseMessage> deleteLogContent() throws FileNotFoundException {
		String fullpath = prop.getAppserverlogPath() + "appserver.log";
		PrintWriter writer = new PrintWriter(fullpath);
		writer.print("");
		writer.close();
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Appserver log content deleted successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/send/log")
	public ResponseEntity<ApiResponseMessage> convertJsonToXml(@RequestBody 
			Log4jRequest log4jRequest)throws IOException{
		String xmlString = U.jsonToXml(log4jRequest.getConfiguration());
		//System.out.println(xmlString);
		//String fullPath = prop.getLog4jFilePath() + "log4j2.xml";
		PrintWriter writer = new PrintWriter(prop.getResourceLog4j().getFile());
		writer.print(xmlString);
		writer.close();
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Log4j2.xml updated successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}

}
