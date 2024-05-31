package com.example.demo.security;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AdminRepository adminRepository;

	@Autowired
	public UserDetailsServiceImpl(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Admin> adminOptional = adminRepository.findByEmail(email);
		Admin admin = adminOptional
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

		return UserDetailsImpl.build(admin);
	}
}
