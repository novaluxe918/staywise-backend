package com.hoainhi.staywise.dtos;

public record FirebaseTokenResponse(
        String firebaseToken,
        String uid,
        String vaiTro
) {
}