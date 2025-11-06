package com.wedding;

import com.wedding.models.Booking;
import com.wedding.models.Hall;
import com.wedding.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DataStore {
    private final List<Hall> halls;
    private final List<Booking> bookings;
    private final Path dataDir;
    private final Path hallsFile;
    private final Path bookingsFile;
    private final Path usersFile;
    private final List<User> users;

    public DataStore() {
        this.dataDir = Paths.get("data");
        this.hallsFile = dataDir.resolve("halls.csv");
        this.bookingsFile = dataDir.resolve("bookings.csv");
        this.usersFile = dataDir.resolve("users.csv");
        this.halls = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.users = new ArrayList<>();
        init();
    }

    private void init() {
        try {
            if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
            loadHalls();
            loadBookings();
            loadUsers();
        } catch (Exception ignored) {
        }
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addOrUpdateHall(Hall h) {
        int idx = -1;
        for (int i = 0; i < halls.size(); i++) if (halls.get(i).getId().equals(h.getId())) idx = i;
        if (idx >= 0) halls.set(idx, h); else halls.add(h);
        halls.sort(Comparator.comparing(Hall::getName));
    }

    public void removeHall(String hallId) {
        halls.removeIf(h -> h.getId().equals(hallId));
        bookings.removeIf(b -> b.getHallId().equals(hallId));
    }

    public void addOrUpdateBooking(Booking b) {
        int idx = -1;
        for (int i = 0; i < bookings.size(); i++) if (bookings.get(i).getId().equals(b.getId())) idx = i;
        if (idx >= 0) bookings.set(idx, b); else bookings.add(b);
        bookings.sort(Comparator.comparing(Booking::getDate).thenComparing(Booking::getStartTime));
    }

    public void removeBooking(String id) {
        bookings.removeIf(b -> b.getId().equals(id));
    }

    public boolean hasOverlap(String hallId, LocalDate date, LocalTime start, LocalTime end, String excludeBookingId) {
        for (Booking b : bookings) {
            if (!b.getHallId().equals(hallId)) continue;
            if (!b.getDate().equals(date)) continue;
            if (excludeBookingId != null && excludeBookingId.equals(b.getId())) continue;
            boolean overlap = !start.isAfter(b.getEndTime()) && !end.isBefore(b.getStartTime());
            if (overlap) return true;
        }
        return false;
    }

    public void saveAll() {
        saveHalls();
        saveBookings();
        saveUsers();
    }

    private void loadHalls() {
        halls.clear();
        if (!Files.exists(hallsFile)) return;
        try (BufferedReader br = new BufferedReader(new FileReader(hallsFile.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                Hall h = new Hall(p[0], p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3]));
                halls.add(h);
            }
        } catch (Exception ignored) {
        }
        halls.sort(Comparator.comparing(Hall::getName));
    }

    private void saveHalls() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(hallsFile.toFile()))) {
            for (Hall h : halls) {
                bw.write(String.join(",",
                        h.getId(),
                        escape(h.getName()),
                        Integer.toString(h.getCapacity()),
                        Double.toString(h.getPricePerHour())));
                bw.newLine();
            }
        } catch (Exception ignored) {
        }
    }

    private void loadBookings() {
        bookings.clear();
        if (!Files.exists(bookingsFile)) return;
        try (BufferedReader br = new BufferedReader(new FileReader(bookingsFile.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 9) continue;
                Booking b = new Booking(
                        p[0],
                        p[1],
                        p[2],
                        p[3],
                        LocalDate.parse(p[4]),
                        LocalTime.parse(p[5]),
                        LocalTime.parse(p[6]),
                        Double.parseDouble(p[7]),
                        Double.parseDouble(p[8])
                );
                bookings.add(b);
            }
        } catch (Exception ignored) {
        }
        bookings.sort(Comparator.comparing(Booking::getDate).thenComparing(Booking::getStartTime));
    }

    private void saveBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bookingsFile.toFile()))) {
            for (Booking b : bookings) {
                bw.write(String.join(",",
                        b.getId(),
                        escape(b.getCustomerName()),
                        escape(b.getPhone()),
                        b.getHallId(),
                        b.getDate().toString(),
                        b.getStartTime().toString(),
                        b.getEndTime().toString(),
                        Double.toString(b.getHours()),
                        Double.toString(b.getTotalCost())));
                bw.newLine();
            }
        } catch (Exception ignored) {
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    private void loadUsers() {
        users.clear();
        if (!Files.exists(usersFile)) return;
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 2) continue;
                users.add(new User(p[0], p[1]));
            }
        } catch (Exception ignored) {
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile.toFile()))) {
            for (User u : users) {
                bw.write(String.join(",",
                        escape(u.getUsername()),
                        escape(u.getPassword())));
                bw.newLine();
            }
        } catch (Exception ignored) {
        }
    }

    public boolean userExists(String username) {
        for (User u : users) if (u.getUsername().equalsIgnoreCase(username)) return true;
        return false;
    }

    public boolean addUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        if (userExists(username)) return false;
        users.add(new User(username.trim(), password.trim()));
        return true;
    }

    public boolean validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) return true;
        }
        return false;
    }
}
