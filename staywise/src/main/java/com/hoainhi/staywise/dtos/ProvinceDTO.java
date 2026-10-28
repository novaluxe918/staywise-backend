package com.hoainhi.staywise.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProvinceDTO {
    private Integer code;
    private String name;
    private String codename;

    @JsonProperty("wards")
    private List<WardsDTO> wardsDTOList;
}
