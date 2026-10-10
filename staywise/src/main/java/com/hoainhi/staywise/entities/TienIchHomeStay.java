package com.hoainhi.staywise.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tienIchHomeStay")
public class TienIchHomeStay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTienIchHS")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHomeStay", nullable = false)
    @JsonIgnore
    private HomeStay homeStay;

    @ManyToOne
    @JoinColumn(name = "matienich", nullable = false)
    private TienIch tienIch;

}
