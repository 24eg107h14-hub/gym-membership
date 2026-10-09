package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.LoginRequestDto;
import com.anurag.gymMemberShip.dto.LoginResponseDto;
import com.anurag.gymMemberShip.dto.RegisterRequestDto;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.User;
import com.anurag.gymMemberShip.enums.Role;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private MemberRepository memberRepository;

      @Autowired
      private PasswordEncoder passwordEncoder;

      @Autowired
      private JwtService jwtService;

      @Transactional
      public void register(RegisterRequestDto dto) {
            String email = dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : "";
            if (email.isEmpty()) {
                  throw new IllegalArgumentException("Email is required");
            }

            if (userRepository.existsByEmail(email)) {
                  throw new IllegalArgumentException("Email is already registered. Please login or use a different email.");
            }

            String phone = dto.getPhone();
            if (phone != null && !phone.trim().isEmpty()) {
                  phone = phone.trim();
                  if (memberRepository.existsByPhone(phone)) {
                        throw new IllegalArgumentException("Phone number is already registered with another account.");
                  }
            } else {
                  // Fallback phone if not supplied in registration form
                  phone = "9" + String.format("%09d", Math.abs(email.hashCode() % 1000000000L));
            }

            User user = new User();
            user.setName(dto.getName() != null ? dto.getName().trim() : "Member");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(Role.MEMBER);

            User savedUser = userRepository.save(user);

            Member member = new Member();
            member.setUser(savedUser);
            member.setFullName(user.getName());
            member.setEmail(email);
            member.setPhone(phone);
            member.setGender(dto.getGender() != null ? dto.getGender() : "Other");
            member.setAddress(dto.getAddress() != null ? dto.getAddress().trim() : "");
            member.setDob(dto.getDob());

            memberRepository.save(member);
      }

      public LoginResponseDto login(LoginRequestDto dto) {
            String email = dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : "";
            String password = dto.getPassword() != null ? dto.getPassword() : "";

            User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                  throw new BadCredentialsException("Invalid email or password");
            }

            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
            return new LoginResponseDto(token, user.getName(), user.getRole().name());
      }
}
