package com.quangtrung.DriveGuardAI.service;

import com.quangtrung.DriveGuardAI.dto.auth.LoginRequestDTO;
import com.quangtrung.DriveGuardAI.dto.auth.LoginResponseDTO;
import com.quangtrung.DriveGuardAI.dto.auth.RegisterRequestDTO;

public interface UserService {
    void registerUser(RegisterRequestDTO registerRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
