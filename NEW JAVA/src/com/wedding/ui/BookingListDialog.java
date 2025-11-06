package com.wedding.ui;

import com.wedding.DataStore;
import com.wedding.models.Booking;
import com.wedding.models.Hall;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BookingListDialog extends Dialog {
    private final DataStore dataStore;
    private final List list;

    public BookingListDialog(Frame owner, DataStore store) {
        super(owner, "Bookings", true);
        this.dataStore = store;
        setSize(700, 450);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        list = new List();
        refresh();
        add(list, BorderLayout.CENTER);

        Panel actions = new Panel();
        Button edit = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        actions.add(edit);
        actions.add(deleteBtn);
        add(actions, BorderLayout.SOUTH);

        edit.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i >= 0 && i < dataStore.getBookings().size()) {
                Booking b = dataStore.getBookings().get(i);
                BookingFormDialog dlg = new BookingFormDialog(owner, dataStore, b);
                dlg.setVisible(true);
                refresh();
            }
        });

        deleteBtn.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i >= 0 && i < dataStore.getBookings().size()) {
                String id = dataStore.getBookings().get(i).getId();
                dataStore.removeBooking(id);
                refresh();
            }
        });
    }

    private void refresh() {
        list.removeAll();
        for (Booking b : dataStore.getBookings()) {
            Hall hall = null;
            for (Hall h : dataStore.getHalls()) if (h.getId().equals(b.getHallId())) hall = h;
            String hallName = hall == null ? "Unknown" : hall.getName();
            list.add(b.getDate() + " " + b.getStartTime() + "-" + b.getEndTime() + " | " + hallName + " | " + b.getCustomerName() + " | " + b.getTotalCost());
        }
    }
}
