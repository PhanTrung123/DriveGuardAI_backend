package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.dto.auth.LoginRequestDTO;
import com.quangtrung.DriveGuardAI.dto.auth.LoginResponseDTO;
import com.quangtrung.DriveGuardAI.dto.auth.RegisterRequestDTO;
import com.quangtrung.DriveGuardAI.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            userService.registerUser(registerRequestDTO);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi đăng ký: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO , HttpServletResponse response) {
        try{
            LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO);
            // 2. Tạo Cookie chứa Token hoặc Session ID (Thực tế nên dùng JWT Token)
            // Ví dụ này lưu tạm username hoặc token giả định
            String tokenOrValue = "Bearer-Token-Or-Session-Id-For-" + loginResponseDTO.getEmail();

            Cookie authCookie = new Cookie("ACCESS_TOKEN", tokenOrValue);

            // 3. Cấu hình bảo mật chuẩn cho Cookie
            authCookie.setHttpOnly(true); // Chống tấn công XSS (JS không đọc được)
            authCookie.setSecure(false);  // Đổi thành true nếu chạy HTTPS (production)
            authCookie.setPath("/");      // Có hiệu lực toàn bộ ứng dụng
            authCookie.setMaxAge(24 * 60 * 60); // Thời gian sống của cookie (ví dụ: 1 ngày tính bằng giây)

            // 4. Đưa cookie vào Header của Response
            response.addCookie(authCookie);
            return ResponseEntity.ok(loginResponseDTO.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Lỗi đăng nhập: " + e.getMessage());
        }
    }
}
