package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private String pwd = "admin123";  // hardcoded password

    public List<String> getUsers(String name) {
        List<String> l = new ArrayList<>();
        try {
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", pwd);
            Statement s = c.createStatement();
            // SQL injection vulnerability
            ResultSet r = s.executeQuery("SELECT * FROM users WHERE name = '" + name + "'");
            while (r.next()) {
                l.add(r.getString(1));
            }
        } catch (Exception e) {
            // swallowing exception
        }
        return l;
    }

    public double calculateDiscount(double price, int type) {
        // magic numbers everywhere
        if (type == 1) {
            return price * 0.1;
        } else if (type == 2) {
            return price * 0.2;
        } else if (type == 3) {
            return price * 0.3;
        } else if (type == 4) {
            return price * 0.15;
        } else if (type == 5) {
            return price * 0.25;
        }
        return 0;
    }

    public String processUser(String data) {
        // no null check
        String[] parts = data.split(",");
        String name = parts[0].trim();
        String email = parts[1].trim();
        String phone = parts[2].trim();

        // duplicated validation logic
        if (name.length() < 2) {
            return "error";
        }
        if (name.length() > 100) {
            return "error";
        }

        if (email.length() < 2) {
            return "error";
        }
        if (email.length() > 100) {
            return "error";
        }

        if (phone.length() < 2) {
            return "error";
        }
        if (phone.length() > 100) {
            return "error";
        }

        return "ok";
    }

    public void doStuff(Object o) {
        // empty method
    }

    public int calc(int a, int b, int c, int d, int e, int f, int g) {
        // too many parameters
        return a + b + c + d + e + f + g;
    }
}
