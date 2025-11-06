package com.wedding;

import java.awt.EventQueue;
import com.wedding.ui.LoginDialog;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            DataStore store = new DataStore();
            LoginDialog login = new LoginDialog(null, store);
            login.setVisible(true);
            String user = login.getAuthenticatedUser();
            if (user == null) {
                System.exit(0);
                return;
            }
            MainWindow window = new MainWindow(store, user);
            window.setVisible(true);
        });
    }
}
