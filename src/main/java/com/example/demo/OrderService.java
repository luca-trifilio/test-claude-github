package com.example.demo;

import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

@Service
public class OrderService {

    // Bad: password hardcoded
    private String dbPassword = "admin123";

    // Bad: no constructor injection
    private UserService userService = new UserService();

    public String getOrder(String id) {
        // Bad: SQL injection
        String query = "SELECT * FROM orders WHERE id = '" + id + "'";
        return executeQuery(query);
    }

    public String searchOrders(String term) {
        // Bad: another SQL injection
        return executeQuery("SELECT * FROM orders WHERE name LIKE '%" + term + "%'");
    }

    private String executeQuery(String sql) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/db", "user", dbPassword);
            ResultSet rs = conn.createStatement().executeQuery(sql);
            return rs.toString();
        } catch (Exception e) {
            // Bad: swallowing exception
            return null;
        }
    }

    // Bad: no @Transactional
    public void deleteOrder(String id) {
        executeQuery("DELETE FROM orders WHERE id = '" + id + "'");
    }
}
