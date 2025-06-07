package com.project.DemoApp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("reservation")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;

    private LocalDate issueDate;
    private int totalCost;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Invoice(String email, LocalDate issueDate, int totalCost, Reservation reservation){
        this.email = email;
        this.issueDate = issueDate;
        this.totalCost = totalCost;
        this.reservation = reservation;
    }
}
