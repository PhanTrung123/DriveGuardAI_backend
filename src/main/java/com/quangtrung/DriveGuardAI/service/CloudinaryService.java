package com.quangtrung.DriveGuardAI.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    public String uploadFile(MultipartFile file) throws IOException;
}
