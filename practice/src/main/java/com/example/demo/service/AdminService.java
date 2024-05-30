package com.example.demo.service;

import com.example.demo.entity.Admin;

public interface AdminService {

    void saveAdmin(Admin admin);

    boolean authenticate(String email, String password);

    Admin findByEmail(String email);

    void registerAdmin(String email, String password, String firstName, String lastName);

    boolean isEmailExist(String email);
}
