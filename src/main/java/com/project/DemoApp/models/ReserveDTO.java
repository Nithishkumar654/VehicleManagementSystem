package com.project.DemoApp.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReserveDTO {
    private String name;
    private String email;
    private String licensePlate;
    private LocalDate pickupDate;
    private int days;
}
