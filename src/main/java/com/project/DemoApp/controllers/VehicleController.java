package com.project.DemoApp.controllers;

import com.project.DemoApp.models.Reservation;
import com.project.DemoApp.models.Vehicle;
import com.project.DemoApp.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/vehicle")
public class VehicleController {

    VehicleService service;
    VehicleController(VehicleService service){
        this.service = service;
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles(){
        return new ResponseEntity<>(service.getVehicles(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(@RequestBody Vehicle vehicle){
        service.addVehicle(vehicle);
        return new ResponseEntity<>("Vehicle Added!!", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{licensePlate}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String licensePlate) {
        service.deleteVehicle(licensePlate);
        return new ResponseEntity<>("Vehicle Deleted!!", HttpStatus.OK);
    }

    @GetMapping("/all-reservation-details/{licensePlate}")
    public ResponseEntity<List<Reservation>> getReservationDetails(@PathVariable String licensePlate){
        return new ResponseEntity<>(service.getReservationDetails(licensePlate), HttpStatus.OK);
    }

    @GetMapping("/reservation-detail/{licensePlate}")
    public ResponseEntity<?> getCurrentReservation(@PathVariable String licensePlate){
        Reservation reservation = service.getCurrentReservation(licensePlate);

        if(reservation == null){
            return new ResponseEntity<>("No Current Reservations", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
}
