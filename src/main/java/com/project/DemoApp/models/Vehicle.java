package com.project.DemoApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private String brand;
    private String licensePlate;
    private int costPerDay;
    private int costPerExtraDay;
    private String description;
    private boolean available;
}

enum VehicleType {
    CAR, SUV, VAN, MOTORCYCLE, TRUCK
}
