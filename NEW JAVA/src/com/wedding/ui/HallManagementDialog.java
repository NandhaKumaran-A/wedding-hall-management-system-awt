package com.wedding.ui;

import com.wedding.DataStore;
import com.wedding.models.Hall;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HallManagementDialog extends Dialog {
    private final DataStore dataStore;
    private final List list;

    public HallManagementDialog(Frame owner, DataStore store) {
        super(owner, "Manage Halls", true);
        this.dataStore = store;
        setSize(600, 400);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        list = new List();
        refresh();
        add(list, BorderLayout.CENTER);

        Panel form = new Panel(new GridLayout(4, 2, 8, 8));
        TextField name = new TextField();
        TextField capacity = new TextField();
        TextField price = new TextField();
        form.add(new Label("Name"));
        form.add(name);
        form.add(new Label("Capacity"));
        form.add(capacity);
        form.add(new Label("Price/hr"));
        form.add(price);
        Button addBtn = new Button("Add/Update");
        Button deleteBtn = new Button("Delete Selected");
        form.add(addBtn);
        form.add(deleteBtn);
        add(form, BorderLayout.SOUTH);

        list.addItemListener(e -> {
            int i = list.getSelectedIndex();
            if (i >= 0 && i < dataStore.getHalls().size()) {
                Hall h = dataStore.getHalls().get(i);
                name.setText(h.getName());
                capacity.setText(Integer.toString(h.getCapacity()));
                price.setText(Double.toString(h.getPricePerHour()));
            }
        });

        addBtn.addActionListener(e -> {
            String n = name.getText().trim();
            String c = capacity.getText().trim();
            String p = price.getText().trim();
            if (n.isEmpty()) return;
            int cap;
            double pr;
            try { cap = Integer.parseInt(c); pr = Double.parseDouble(p); } catch (Exception ex) { return; }
            int sel = list.getSelectedIndex();
            if (sel >= 0 && sel < dataStore.getHalls().size()) {
                Hall h = dataStore.getHalls().get(sel);
                h.setName(n);
                h.setCapacity(cap);
                h.setPricePerHour(pr);
                dataStore.addOrUpdateHall(h);
            } else {
                Hall h = new Hall(com.wedding.DataStore.newId(), n, cap, pr);
                dataStore.addOrUpdateHall(h);
            }
            refresh();
        });

        deleteBtn.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0 && sel < dataStore.getHalls().size()) {
                String id = dataStore.getHalls().get(sel).getId();
                dataStore.removeHall(id);
                refresh();
            }
        });
    }

    private void refresh() {
        list.removeAll();
        for (Hall h : dataStore.getHalls()) list.add(h.toString());
    }
}
