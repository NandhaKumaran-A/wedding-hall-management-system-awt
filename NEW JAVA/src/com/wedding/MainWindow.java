package com.wedding;

import com.wedding.models.Booking;
import com.wedding.models.Hall;
import com.wedding.ui.BookingFormDialog;
import com.wedding.ui.BookingListDialog;
import com.wedding.ui.HallManagementDialog;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainWindow extends Frame {
    private final DataStore dataStore;
    private final String username;

    public MainWindow(DataStore store, String username) {
        super("Wedding Hall Booking System");
        this.dataStore = store;
        this.username = username;
        setSize(900, 600);
        centerWindow();
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dataStore.saveAll();
                dispose();
                System.exit(0);
            }
        });
        setMenuBar(buildMenu());
        Panel p = new Panel(new BorderLayout());
        p.add(new Label("Welcome, " + username + " — Wedding Hall Booking System", Label.CENTER), BorderLayout.NORTH);
        Panel actions = new Panel();
        Button hallsBtn = new Button("Manage Halls");
        Button newBookingBtn = new Button("New Booking");
        Button viewBookingsBtn = new Button("View Bookings");
        actions.add(hallsBtn);
        actions.add(newBookingBtn);
        actions.add(viewBookingsBtn);
        p.add(actions, BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);

        hallsBtn.addActionListener(e -> new HallManagementDialog(this, dataStore).setVisible(true));
        newBookingBtn.addActionListener(e -> {
            java.util.List<Hall> hs = dataStore.getHalls();
            if (hs.isEmpty()) return;
            BookingFormDialog dlg = new BookingFormDialog(this, dataStore, null);
            dlg.setVisible(true);
        });
        viewBookingsBtn.addActionListener(e -> new BookingListDialog(this, dataStore).setVisible(true));
    }

    private void centerWindow() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    private MenuBar buildMenu() {
        MenuBar mb = new MenuBar();

        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        save.addActionListener(e -> dataStore.saveAll());
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> {
            dataStore.saveAll();
            dispose();
            System.exit(0);
        });
        file.add(save);
        file.addSeparator();
        file.add(exit);

        Menu halls = new Menu("Halls");
        MenuItem manageHalls = new MenuItem("Manage Halls");
        manageHalls.addActionListener(e -> new HallManagementDialog(this, dataStore).setVisible(true));
        halls.add(manageHalls);

        Menu bookings = new Menu("Bookings");
        MenuItem newBooking = new MenuItem("New Booking");
        newBooking.addActionListener(e -> {
            List<Hall> hs = dataStore.getHalls();
            if (hs.isEmpty()) return;
            BookingFormDialog dlg = new BookingFormDialog(this, dataStore, null);
            dlg.setVisible(true);
        });
        MenuItem viewBookings = new MenuItem("View Bookings");
        viewBookings.addActionListener(e -> new BookingListDialog(this, dataStore).setVisible(true));
        bookings.add(newBooking);
        bookings.add(viewBookings);

        mb.add(file);
        mb.add(halls);
        mb.add(bookings);
        return mb;
    }
}
