package com.project.DemoApp.service;

import com.project.DemoApp.models.*;
import com.project.DemoApp.repository.CustomerRepository;
import com.project.DemoApp.repository.InvoiceRepository;
import com.project.DemoApp.repository.ReservationRepository;
import com.project.DemoApp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.Math.max;

@Service
public class CustomerService {

    CustomerRepository customerRepository;
    VehicleRepository vehicleRepository;
    ReservationRepository reservationRepository;
    InvoiceRepository invoiceRepository;
    NotificationService notificationService;

    @Autowired
    CustomerService(CustomerRepository customerRepository, VehicleRepository vehicleRepository, ReservationRepository reservationRepository, InvoiceRepository invoiceRepository, NotificationService notificationService){
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.reservationRepository = reservationRepository;
        this.invoiceRepository = invoiceRepository;
        this.notificationService = notificationService;
    }

    public List<Customer> getUsers() {
        return customerRepository.findAll();
    }

    public void addUser(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getUserByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    public void updateUser(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteUser(String email){
        customerRepository.deleteByEmail(email);
    }


    public Vehicle reserveVehicle(ReserveDTO reserveDTO) {

        Vehicle vehicle = vehicleRepository.findByLicensePlate(reserveDTO.getLicensePlate()).orElse(null);
        Customer customer = customerRepository.findByEmail(reserveDTO.getEmail()).orElse(null);

        if(customer == null || vehicle == null || !vehicle.isAvailable())
            return null;

        vehicle.setAvailable(false);
        Reservation reservation = new Reservation(
                reserveDTO.getPickupDate(),
                reserveDTO.getPickupDate().plusDays(reserveDTO.getDays()),
                (null),
                ReservationStatus.RESERVED,
                customer,
                vehicle
        );

        reservationRepository.save(reservation);
        vehicleRepository.save(vehicle);

        return vehicle;
    }

    public Vehicle cancelVehicle(CancelDTO cancelDTO) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(cancelDTO.getLicensePlate()).orElse(null);
        Reservation reservation = reservationRepository.findById(cancelDTO.getReservationId()).orElse(null);

        if(vehicle == null || reservation == null || reservation.getStatus() != ReservationStatus.RESERVED){
            return null;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        vehicle.setAvailable(true);
        vehicleRepository.save(vehicle);

        return vehicle;
    }

    public Invoice returnVehicle(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if(reservation == null)
            return null;

        Vehicle vehicle = reservation.getVehicle();

        if(vehicle.isAvailable())
            return null;

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setReturnDate(LocalDate.now());

        vehicle.setAvailable(true);
        vehicleRepository.save(vehicle);

        long days = ChronoUnit.DAYS.between(reservation.getReservedDate(), reservation.getDueDate());
        long extraDays = max(0L, ChronoUnit.DAYS.between(reservation.getDueDate(), reservation.getReturnDate()));
        int cost = (int) days * vehicle.getCostPerDay();
        cost += (int) extraDays * (vehicle.getCostPerDay() + vehicle.getCostPerExtraDay());

        Invoice invoice = new Invoice(
                reservation.getCustomer().getEmail(),
                LocalDate.now(),
                cost,
                reservation
        );

        reservationRepository.save(reservation);
        invoiceRepository.save(invoice);

        return invoice;
    }

    public List<Reservation> getReservations(String email) {
        return reservationRepository.getReservationsByEmail(email);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    @Scheduled(cron = "0 0 3 * * *") // send mails at 3 AM
    public void sendReserveReminders() {
        List<Reservation> reservations = reservationRepository.findReserved();

        for (Reservation r : reservations) {
            if (ChronoUnit.DAYS.between(r.getReservedDate(), LocalDate.now()) <= 1) {
                String email = r.getCustomer().getEmail();
                String subject = "Reminder: Vehicle Pickup Scheduled!";
                String body = String.format(
                        "Dear %s,\n\n"
                                + "This is a reminder that your reserved vehicle is scheduled for pickup on (%s).\n"
                                + "Please ensure to arrive on time at our pickup location.\n\n"
                                + "Reservation Details:\n"
                                + "- Vehicle: %s\n"
                                + "- Pickup Date: %s\n"
                                + "- Return Date: %s\n\n"
                                + "Thank you for choosing our service.\n\n"
                                + "Best regards,\n"
                                + "Vehicle Rental Team",
                        r.getCustomer().getName(),
                        r.getReservedDate(),
                        r.getVehicle().getBrand(),
                        r.getReservedDate(),
                        r.getDueDate()
                );

                notificationService.sendEmail(email, subject, body);
            }
        }
    }

//    @Scheduled(cron = "0 0 3 * * *") // send mails at 3 AM daily
    public void sendReturnReminders() {
        List<Reservation> reservations = reservationRepository.findDeliveredOrOverDue();

        for (Reservation r : reservations) {
            int diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), r.getDueDate());
            if (diff >= -1) {
                String email = r.getCustomer().getEmail();
                String subject = "Reminder: Vehicle Return Due Soon!";
                String body = String.format(
                        "Dear %s,\n\n"
                                + "This is a friendly reminder that your rented vehicle is due to be returned on (%s).\n"
                                + "Please ensure the vehicle is returned on time to avoid late fees.\n\n"
                                + "Reservation Details:\n"
                                + "- Vehicle: %s\n"
                                + "- Pickup Date: %s\n"
                                + "- Due Date: %s\n\n"
                                + "- Extra Fee Per Day: %s\n\n"
                                + "If you've already returned the vehicle, please ignore this message.\n\n"
                                + "Thank you for choosing our service!\n\n"
                                + "Best regards,\n"
                                + "Vehicle Rental Team",
                        r.getCustomer().getName(),
                        r.getDueDate(),
                        r.getVehicle().getBrand(),
                        r.getReservedDate(),
                        r.getDueDate(),
                        r.getVehicle().getCostPerExtraDay()
                );

                if(diff == 1){
                    r.setStatus(ReservationStatus.OVERDUE);
                    reservationRepository.save(r);
                }
                notificationService.sendEmail(email, subject, body);
            }
        }
    }

    public Vehicle pickUpVehicle(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if(reservation == null) return null;

        reservation.setStatus(ReservationStatus.DELIVERED);
        reservationRepository.save(reservation);

        return reservation.getVehicle();
    }
}
