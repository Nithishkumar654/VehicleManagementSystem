package com.project.DemoApp.models;

import lombok.Data;

@Data
public class CancelDTO {
    private String name;
    private String email;
    private String licensePlate;
    private int reservationId;

    public String getEmail() {
        return email;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getReservationId() {
        return reservationId;
    }
}
