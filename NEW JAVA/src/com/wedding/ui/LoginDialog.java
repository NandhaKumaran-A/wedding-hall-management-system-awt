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

public class LoginDialog extends Dialog {
    private final DataStore dataStore;
    private String authenticatedUser;

    public LoginDialog(Frame owner, DataStore store) {
        super(owner, "Login", true);
        this.dataStore = store;
        setSize(400, 220);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        TextField username = new TextField();
        TextField password = new TextField();
        password.setEchoChar('*');
        Label message = new Label(" ");

        form.add(new Label("Username"));
        form.add(username);
        form.add(new Label("Password"));
        form.add(password);
        form.add(new Label(" "));
        form.add(message);

        Panel actions = new Panel();
        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");
        Button cancelBtn = new Button("Cancel");
        actions.add(loginBtn);
        actions.add(signUpBtn);
        actions.add(cancelBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String u = username.getText().trim();
            String p = password.getText().trim();
            if (dataStore.validateUser(u, p)) {
                authenticatedUser = u;
                dispose();
            } else {
                message.setText("Invalid credentials");
            }
        });

        signUpBtn.addActionListener(e -> {
            SignUpDialog s = new SignUpDialog(owner, dataStore);
            s.setVisible(true);
            // no auto-login; allow user to login after sign up
        });

        cancelBtn.addActionListener(e -> {
            authenticatedUser = null;
            dispose();
        });
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }
}
