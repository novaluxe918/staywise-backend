package com.hoainhi.staywise.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NguoiDungDTO {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String hoTen;

    @Pattern(
            regexp = "^$|^0[0-9]{9}$",
            message = "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0"
    )
    private String soDienThoai;

    private MultipartFile anhDaiDien;

    private MultipartFile anhCCCDMatTruoc;

    private MultipartFile anhCCCDMatSau;

    private MultipartFile anhGiayPhepKinhDoanh;
}
