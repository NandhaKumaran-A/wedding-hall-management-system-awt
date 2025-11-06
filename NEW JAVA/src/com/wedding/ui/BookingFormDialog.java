package com.wedding.ui;

import com.wedding.DataStore;
import com.wedding.models.Booking;
import com.wedding.models.Hall;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingFormDialog extends Dialog {
    private final DataStore dataStore;
    private final Booking editing;

    public BookingFormDialog(Frame owner, DataStore store, Booking editing) {
        super(owner, editing == null ? "New Booking" : "Edit Booking", true);
        this.dataStore = store;
        this.editing = editing;
        setSize(500, 400);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Panel form = new Panel(new GridLayout(9, 2, 8, 8));
        Choice hall = new Choice();
        for (Hall h : dataStore.getHalls()) hall.add(h.getName());
        TextField customer = new TextField();
        TextField phone = new TextField();
        TextField date = new TextField("2025-01-01");
        TextField start = new TextField("10:00");
        TextField end = new TextField("12:00");
        Label hours = new Label("0.0");
        Label total = new Label("0.0");

        form.add(new Label("Hall"));
        form.add(hall);
        form.add(new Label("Customer"));
        form.add(customer);
        form.add(new Label("Phone"));
        form.add(phone);
        form.add(new Label("Date (yyyy-MM-dd)"));
        form.add(date);
        form.add(new Label("Start (HH:mm)"));
        form.add(start);
        form.add(new Label("End (HH:mm)"));
        form.add(end);
        form.add(new Label("Hours"));
        form.add(hours);
        form.add(new Label("Total"));
        form.add(total);

        Panel actions = new Panel();
        Button calc = new Button("Calculate");
        Button save = new Button("Save");
        actions.add(calc);
        actions.add(save);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        calc.addActionListener(e -> {
            int idx = hall.getSelectedIndex();
            if (idx < 0) return;
            Hall selected = dataStore.getHalls().get(idx);
            try {
                LocalDate d = LocalDate.parse(date.getText().trim());
                LocalTime s = LocalTime.parse(start.getText().trim());
                LocalTime en = LocalTime.parse(end.getText().trim());
                double hrs = Booking.calcHours(s, en);
                hours.setText(String.format("%.2f", hrs));
                total.setText(String.format("%.2f", hrs * selected.getPricePerHour()));
            } catch (Exception ignored1) {
            }
        });

        save.addActionListener(e -> {
            int idx = hall.getSelectedIndex();
            if (idx < 0) return;
            Hall selected = dataStore.getHalls().get(idx);
            try {
                LocalDate d = LocalDate.parse(date.getText().trim());
                LocalTime s = LocalTime.parse(start.getText().trim());
                LocalTime en = LocalTime.parse(end.getText().trim());
                if (en.isBefore(s)) return;
                if (dataStore.hasOverlap(selected.getId(), d, s, en, editing == null ? null : editing.getId())) return;
                double hrs = Booking.calcHours(s, en);
                double tot = hrs * selected.getPricePerHour();
                Booking b;
                if (editing == null) {
                    b = new Booking(DataStore.newId(), customer.getText().trim(), phone.getText().trim(), selected.getId(), d, s, en, hrs, tot);
                } else {
                    b = new Booking(editing.getId(), customer.getText().trim(), phone.getText().trim(), selected.getId(), d, s, en, hrs, tot);
                }
                dataStore.addOrUpdateBooking(b);
                dispose();
            } catch (Exception ignored12) {
            }
        });

        if (editing != null) {
            Hall selected = null;
            for (int i = 0; i < dataStore.getHalls().size(); i++) if (dataStore.getHalls().get(i).getId().equals(editing.getHallId())) selected = dataStore.getHalls().get(i);
            if (selected != null) hall.select(selected.getName());
            customer.setText(editing.getCustomerName());
            phone.setText(editing.getPhone());
            date.setText(editing.getDate().toString());
            start.setText(editing.getStartTime().toString());
            end.setText(editing.getEndTime().toString());
            hours.setText(String.format("%.2f", editing.getHours()));
            total.setText(String.format("%.2f", editing.getTotalCost()));
        } else if (!dataStore.getHalls().isEmpty()) {
            hall.select(0);
        }
    }
}
