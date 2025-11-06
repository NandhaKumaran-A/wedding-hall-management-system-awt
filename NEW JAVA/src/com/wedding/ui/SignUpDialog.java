package com.wedding.ui;

import com.wedding.DataStore;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUpDialog extends Dialog {
    private final DataStore dataStore;

    public SignUpDialog(Frame owner, DataStore store) {
        super(owner, "Sign Up", true);
        this.dataStore = store;
        setSize(420, 240);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Panel form = new Panel(new GridLayout(4, 2, 8, 8));
        TextField username = new TextField();
        TextField password = new TextField();
        password.setEchoChar('*');
        TextField confirm = new TextField();
        confirm.setEchoChar('*');
        Label message = new Label(" ");

        form.add(new Label("Username"));
        form.add(username);
        form.add(new Label("Password"));
        form.add(password);
        form.add(new Label("Confirm Password"));
        form.add(confirm);
        form.add(new Label(" "));
        form.add(message);

        Panel actions = new Panel();
        Button createBtn = new Button("Create Account");
        Button cancelBtn = new Button("Cancel");
        actions.add(createBtn);
        actions.add(cancelBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> {
            String u = username.getText().trim();
            String p1 = password.getText().trim();
            String p2 = confirm.getText().trim();
            if (u.isEmpty() || p1.isEmpty()) { message.setText("Enter username and password"); return; }
            if (!p1.equals(p2)) { message.setText("Passwords do not match"); return; }
            if (dataStore.userExists(u)) { message.setText("Username already exists"); return; }
            if (dataStore.addUser(u, p1)) {
                dataStore.saveAll();
                message.setText("Account created. You can login.");
                dispose();
            } else {
                message.setText("Failed to create account");
            }
        });

        cancelBtn.addActionListener(e -> dispose());
    }
}
