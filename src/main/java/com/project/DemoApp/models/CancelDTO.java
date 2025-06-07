package com.project.DemoApp.models;

import lombok.Data;

@Data
public class CancelDTO {
    private String name;
    private String email;
    private String licensePlate;
    private int reservationId;
}
