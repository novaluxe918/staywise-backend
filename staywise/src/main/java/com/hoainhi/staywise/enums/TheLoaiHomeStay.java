package com.hoainhi.staywise.enums;

import lombok.Getter;

@Getter
public enum TheLoaiHomeStay {

    NGHI_DUONG("Nghỉ dưỡng"),
    GIA_DINH("HomeStay gia đình"),
    GAN_BIEN("HomeStay gần biển"),
    VUNG_NUI("HomeStay vùng núi"),
    TRUNG_TAM_THANH_PHO("HomeStay trung tâm thành phố"),
    NHA_VUON("Nhà vườn"),
    CAN_HO("Căn hộ"),
    NHA_GO("Nhà gỗ");

    private final String label;

    TheLoaiHomeStay(String label) {
        this.label = label;
    }
}