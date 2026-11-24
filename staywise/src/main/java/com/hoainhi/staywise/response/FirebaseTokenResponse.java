package com.hoainhi.staywise.response;

public record FirebaseTokenResponse(
        String firebaseToken,
        String uid,
        String vaiTro
) {
}