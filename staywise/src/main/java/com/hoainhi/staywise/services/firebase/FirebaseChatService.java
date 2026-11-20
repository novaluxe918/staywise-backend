package com.hoainhi.staywise.services.firebase;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.hoainhi.staywise.entities.NguoiDung;
import org.springframework.stereotype.Service;

@Service
public class FirebaseChatService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseChatService(FirebaseApp firebaseApp) {
        this.firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
    }

    public String taoFirebaseToken(NguoiDung nguoiDung)
            throws FirebaseAuthException {

        // Tạo UID ổn định từ ID người dùng trong MySQL
        String uid = "user_" + nguoiDung.getId();

        return firebaseAuth.createCustomToken(
                uid,
                java.util.Map.of(
                        "role", nguoiDung.getVaiTro().name()
                )
        );
    }
}