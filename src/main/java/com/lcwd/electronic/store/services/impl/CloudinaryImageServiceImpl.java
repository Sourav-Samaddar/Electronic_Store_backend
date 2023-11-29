package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.lcwd.electronic.store.services.CloudinaryImageService;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService{
	
	@Autowired
	private Cloudinary cloudinary;

	@Override
	public Map uploadImage(MultipartFile file) {
		try {
			Map data = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
				    "folder", "ElectronicStore/product/"));
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void deleteImage(String assetId) {
		try {
			ApiResponse response = cloudinary.api().resourceByAssetID(assetId, ObjectUtils.emptyMap());
			String publicId = (String) response.get("public_id");
			this.cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ApiResponse getImage(String assetId) {
		try {
//			return this.cloudinary.api().resource(publicId, ObjectUtils.asMap(
//				    "folder", "ElectronicStore/product/"));
			return cloudinary.api().resourceByAssetID(assetId, ObjectUtils.emptyMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
