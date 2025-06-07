package com.project.DemoApp.service;

import com.project.DemoApp.models.*;
import com.project.DemoApp.repository.CustomerRepository;
import com.project.DemoApp.repository.ReservationRepository;
import com.project.DemoApp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    VehicleRepository vehicleRepository;
    CustomerRepository customerRepository;
    ReservationRepository reservationRepository;

    @Autowired
    VehicleService(VehicleRepository vehicleRepository, CustomerRepository customerRepository, ReservationRepository reservationRepository){
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    public void addVehicle(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    public void deleteVehicle(String licensePlate){
        vehicleRepository.deleteByLicensePlate(licensePlate);
    }

    public List<Reservation> getReservationDetails(String licensePlate) {
        return reservationRepository.getReservationsByLicensePlate(licensePlate);
    }


    public Reservation getCurrentReservation(String licensePlate){
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate).orElse(null);

        if(vehicle == null || vehicle.isAvailable()){
            return null;
        }

        return reservationRepository.getCurrentReservation(licensePlate);
    }
}
