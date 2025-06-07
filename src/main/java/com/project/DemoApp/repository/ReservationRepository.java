package com.project.DemoApp.repository;

import com.project.DemoApp.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("Select r from Reservation r WHERE " +
            "r.vehicle.licensePlate = :licensePlate")
    List<Reservation> getReservationsByLicensePlate(String licensePlate);

    @Query("Select r from Reservation r WHERE " +
            "r.vehicle.licensePlate = :licensePlate AND " +
            "r.status != com.project.DemoApp.models.ReservationStatus.COMPLETED")
    Reservation getCurrentReservation(String licensePlate);

    @Query("Select r from Reservation r WHERE " +
            "r.customer.email = :email")
    List<Reservation> getReservationsByEmail(String email);

    @Query("Select r from Reservation r WHERE " +
            "r.status = com.project.DemoApp.models.ReservationStatus.RESERVED")
    List<Reservation> findReserved();

    @Query("Select r from Reservation r WHERE " +
            "(r.status = com.project.DemoApp.models.ReservationStatus.DELIVERED " +
            "OR r.status = com.project.DemoApp.models.ReservationStatus.OVERDUE)")
    List<Reservation> findDeliveredOrOverDue();
}
