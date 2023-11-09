package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService{

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		String originalFileName = file.getOriginalFilename();
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNewGeneratedName = UUID.randomUUID().toString();
		String newFileNameWithExtension = fileNewGeneratedName + fileExtension;
		String newFileNameWithPath = path + File.separator + newFileNameWithExtension;
		
		if(fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpg") 
				|| fileExtension.equalsIgnoreCase(".jpeg")) {
		
			File folder = new File(path);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			//file copy
			
			Files.copy(file.getInputStream(), Paths.get(newFileNameWithPath));
			file.getInputStream().close();
			
			return newFileNameWithExtension;
		}else {
			throw new BadApiRequest("File with " + fileExtension + " extension is not allowed !!");
		}
		
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
		InputStream is = new FileInputStream(fullPath);
		return is;
	}

}
