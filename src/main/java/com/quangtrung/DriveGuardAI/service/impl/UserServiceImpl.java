package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.auth.LoginRequestDTO;
import com.quangtrung.DriveGuardAI.dto.auth.LoginResponseDTO;
import com.quangtrung.DriveGuardAI.dto.auth.RegisterRequestDTO;
import com.quangtrung.DriveGuardAI.entity.User;
import com.quangtrung.DriveGuardAI.enums.Role;
import com.quangtrung.DriveGuardAI.reponsitory.UserReponsitory;
import com.quangtrung.DriveGuardAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserReponsitory userReponsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userReponsitory.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + registerRequestDTO.getEmail());
        }

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setFullName(registerRequestDTO.getFullName());
        user.setRole(Role.TÀI_XẾ);

        userReponsitory.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
       User user = userReponsitory.findByEmail(loginRequestDTO.getEmail())
               .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        return LoginResponseDTO.builder()
                .message("Đăng nhập thành công!")
                .email(user.getEmail())
                .build();
    }


}
