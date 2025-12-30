package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    UserService svc = new UserService();  // not injected

    @GetMapping("/users")
    public String getUser(@RequestParam String n) {
        // XSS vulnerability - returning user input directly
        return "<html><body>Hello " + n + "</body></html>";
    }

    @PostMapping("/admin")
    public String admin(@RequestParam String password) {
        // hardcoded credential check
        if (password.equals("secret123")) {
            return "Welcome admin!";
        }
        return "Denied";
    }

    @GetMapping("/data")
    public Object getData() {
        // returning null
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        // no authorization check, no validation
        System.out.println("Deleting " + id);
    }
}
