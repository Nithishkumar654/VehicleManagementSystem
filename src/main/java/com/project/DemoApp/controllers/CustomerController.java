package com.project.DemoApp.controllers;

import com.project.DemoApp.models.*;
import com.project.DemoApp.service.CustomerService;
import com.project.DemoApp.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {
    CustomerService customerService;
    VehicleService vehicleService;

    CustomerController(CustomerService customerService, VehicleService vehicleService){
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getUsers(){
        return new ResponseEntity<>(customerService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody Customer customer){
        Customer existingCustomer = customerService.getUserByEmail(customer.getEmail());
        if(existingCustomer != null)
            return new ResponseEntity<>("Email already Exist", HttpStatus.BAD_REQUEST);

        customerService.addUser(customer);
        return new ResponseEntity<>("User Added..!!", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Customer customer){
        customerService.updateUser(customer);
        return new ResponseEntity<>("User Updated!!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        customerService.deleteUser(email);
        return new ResponseEntity<>("User Deleted!!", HttpStatus.OK);
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        Customer customer = customerService.getUserByEmail(email);
        if(customer == null)
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/all-reservations/{email}")
    public ResponseEntity<List<Reservation>> getReservations(@PathVariable String email){
        return new ResponseEntity<>(customerService.getReservations(email), HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Vehicle> reserveVehicle(@RequestBody ReserveDTO reserveDTO){
        Vehicle vehicle = customerService.reserveVehicle(reserveDTO);

        if(vehicle == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }


    @PostMapping("/cancel")
    public ResponseEntity<Vehicle> cancelVehicle(@RequestBody CancelDTO cancelDTO){
        Vehicle vehicle = customerService.cancelVehicle(cancelDTO);

        if(vehicle == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }


    @GetMapping("/pickup/{reservationId}")
    public ResponseEntity<Vehicle> pickUpVehicle(@PathVariable int reservationId){
        Vehicle vehicle = customerService.pickUpVehicle(reservationId);

        if(vehicle == null)
            return new ResponseEntity<>(new Vehicle(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping("/return/{reservationId}")
    public ResponseEntity<?> returnVehicle(@PathVariable int reservationId){

        Invoice invoice = customerService.returnVehicle(reservationId);

        if(invoice == null){
            return new ResponseEntity<>("No Reservation Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/reserve-reminders")
    public ResponseEntity<?> sendReserveReminders(){
        customerService.sendReserveReminders();
        return new ResponseEntity<>("Sent", HttpStatus.OK);
    }

}
