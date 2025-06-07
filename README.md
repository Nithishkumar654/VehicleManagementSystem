# 🚗 Vehicle Rental System

A robust and user-friendly **Vehicle Rental Management System** built with **Spring Boot** that enables admins to manage vehicle inventory and reservations, and allows users to reserve, return, and receive email notifications for their bookings.

---

## 📋 Features

### 🧑‍💼 Admin
- Add, update, and remove vehicles
- View available and reserved vehicles
- Manage overdue and delivered reservations
- Extra cost for OverDue reservations

### 🙋‍♂️ User
- Reserve a vehicle by specifying the pickup date and duration
- Return a vehicle
- Cancel a reservation
- View reservation status

### ✉️ Notifications
- Email reminders for:
  - Upcoming vehicle pickups (1 day before)
  - Scheduled vehicle returns (on and after due date)

---

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **H2 / MySQL** (configurable)
- **Lombok**
- **Java Mail Sender**

