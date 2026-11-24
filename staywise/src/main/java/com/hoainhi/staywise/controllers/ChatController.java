
package com.hoainhi.staywise.controllers;

import com.google.firebase.auth.FirebaseAuthException;
import com.hoainhi.staywise.response.FirebaseTokenResponse;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;

import com.hoainhi.staywise.services.firebase.FirebaseChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private  NguoiDungRepository nguoiDungRepository;

    @Autowired
    private FirebaseChatService firebaseChatService;




    @GetMapping("/firebase-token")
    public ResponseEntity<FirebaseTokenResponse> layFirebaseToken(
            Authentication authentication
    ) throws FirebaseAuthException {

        // 1. Kiểm tra tài khoản đăng nhập
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Bạn chưa đăng nhập!"
            );
        }

        // 2. Lấy email từ JWT
        String email = authentication.getName();

        // 3. Tìm người dùng trong MySQL
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy tài khoản!"
                ));

        // 4. Chỉ ADMIN và OWNER được sử dụng chat
        if (nguoiDung.getVaiTro() != VaiTro.ADMIN
                && nguoiDung.getVaiTro() != VaiTro.OWNER) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền sử dụng chức năng chat!"
            );
        }

        // 5. Tạo Firebase Custom Token
        String firebaseToken =
                firebaseChatService.taoFirebaseToken(nguoiDung);

        // 6. Tạo UID cố định từ ID MySQL
        String uid = "user_" + nguoiDung.getId();

        // 7. Trả kết quả về Frontend
        return ResponseEntity.ok(
                new FirebaseTokenResponse(
                        firebaseToken,
                        uid,
                        nguoiDung.getVaiTro().name()
                )
        );
    }
}
