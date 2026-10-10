package com.hoainhi.staywise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "homestayDichVu")
public class HomeStayDichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHSDichVu")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    @JsonIgnore
    private HomeStay homeStay;

    @ManyToOne
    @JoinColumn(name = "maDichVu", nullable = false)
    private DichVu dichVu;
}
