package com.quangtrung.DriveGuardAI.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quangtrung.DriveGuardAI.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Upload file và nhận kết quả trả về từ Cloudinary
        String originalFilename = file.getOriginalFilename();
        String publicId = originalFilename != null ?
                originalFilename.substring(0, originalFilename.lastIndexOf('.')) : "file_" + System.currentTimeMillis();

        Map<String, Object> options = ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "auto"
        );
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        // Trả về URL an toàn (https) của ảnh/file vừa upload
        return uploadResult.get("secure_url").toString();
    }
}
