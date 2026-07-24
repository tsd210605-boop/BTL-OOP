package com.example.workreport.controller;

import com.example.workreport.dto.ApiResponse;
import com.example.workreport.dto.LoginRequest;
import com.example.workreport.dto.LoginResponse;
import com.example.workreport.exception.AppException;
import com.example.workreport.model.GiangVien;
import com.example.workreport.model.SinhVien;
import com.example.workreport.model.User;
import com.example.workreport.repository.GiangVienRepository;
import com.example.workreport.repository.SinhVienRepository;
import com.example.workreport.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserRepository userRepository;
    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            UserRepository userRepository,
            SinhVienRepository sinhVienRepository,
            GiangVienRepository giangVienRepository,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.sinhVienRepository = sinhVienRepository;
        this.giangVienRepository = giangVienRepository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
            User user =
                    userRepository
                            .findByUsername(request.getUsername())
                            .orElseThrow(() ->
                                    new AppException("Không tìm thấy user", "404", 404)
                            );

            LoginResponse response = new LoginResponse();
            response.setUserID(user.getUserID());
            response.setFullName(user.getFullname());
            response.setRole(user.getRole());

            if ("SINH_VIEN".equals(user.getRole())) {
                SinhVien sv = sinhVienRepository.findByUser_UserID(user.getUserID()).orElse(null);
                if (sv != null) {
                    response.setSinhVienID(sv.getSinhVienID());
                }
            }

            if ("GIANG_VIEN".equals(user.getRole()))
             {
                GiangVien gv = giangVienRepository.findByUser_UserID(user.getUserID()).orElse(null);
                if (gv != null) {
                    response.setGiangVienID(gv.getGiangVienID());
                }
            }
            return ResponseEntity.ok(ApiResponse.ok(response, "Đăng nhập thành công"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(ApiResponse.unauthorized("Sai tài khoản hoặc mật khẩu"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.serverError(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok("Đăng xuất thành công"));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<LoginResponse>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.unauthorized("Chưa đăng nhập"));
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException("Không tìm thấy user", "404", 404));
        LoginResponse response = new LoginResponse();
        response.setUserID(user.getUserID());
        response.setFullName(user.getFullname());
        response.setRole(user.getRole());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}