package com.wedding.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private final String id;
    private String customerName;
    private String phone;
    private String hallId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private double hours;
    private double totalCost;

    public Booking(String id, String customerName, String phone, String hallId, LocalDate date, LocalTime startTime, LocalTime endTime, double hours, double totalCost) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.hallId = hallId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hours = hours;
        this.totalCost = totalCost;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getHallId() { return hallId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public double getHours() { return hours; }
    public double getTotalCost() { return totalCost; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setHallId(String hallId) { this.hallId = hallId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setHours(double hours) { this.hours = hours; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public static double calcHours(LocalTime start, LocalTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        return Math.max(0, minutes) / 60.0;
    }
}
