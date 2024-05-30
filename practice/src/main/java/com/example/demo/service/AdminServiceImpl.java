package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveAdmin(Admin admin) {
		String encodedPassword = passwordEncoder.encode(admin.getPassword());
		admin.setPassword(encodedPassword);
		adminRepository.save(admin);
		System.out.println("Admin saved successfully: " + admin.getEmail());
	}

	@Override
	public boolean authenticate(String email, String password) {
		Optional<Admin> admin = adminRepository.findByEmail(email);
		if (admin.isPresent()) {
			boolean isPasswordMatch = passwordEncoder.matches(password, admin.get().getPassword());
			if (isPasswordMatch) {
				System.out.println("Password matches for email: " + email);
			} else {
				System.out.println("Password does not match for email: " + email);
			}
			return isPasswordMatch;
		} else {
			System.out.println("Admin not found for email: " + email);
			return false;
		}
	}

	@Override
	public Admin findByEmail(String email) {
		Optional<Admin> adminOptional = adminRepository.findByEmail(email);
		return adminOptional.orElse(null);
	}

	@Override
	public void registerAdmin(String email, String password, String firstName, String lastName) {
		Admin admin = new Admin();
		admin.setEmail(email);
		admin.setPassword(password);
		admin.setFirstName(firstName);
		admin.setLastName(lastName);
		saveAdmin(admin);
	}

	@Override
	public boolean isEmailExist(String email) {
		return adminRepository.findByEmail(email).isPresent();
	}
}
