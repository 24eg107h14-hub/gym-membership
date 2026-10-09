package com.anurag.gymMemberShip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class UserProfileDto {

      private Long id;

      @NotBlank(message = "Name is required")
      private String name;

      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      private String email;

      private String phone;
      private String gender;
      private String address;
      private LocalDate dob;
      private String role;

      public UserProfileDto() {
      }

      public UserProfileDto(Long id, String name, String email, String phone, String gender, String address, LocalDate dob, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.gender = gender;
            this.address = address;
            this.dob = dob;
            this.role = role;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
      }

      public String getPhone() {
            return phone;
      }

      public void setPhone(String phone) {
            this.phone = phone;
      }

      public String getGender() {
            return gender;
      }

      public void setGender(String gender) {
            this.gender = gender;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public LocalDate getDob() {
            return dob;
      }

      public void setDob(LocalDate dob) {
            this.dob = dob;
      }

      public String getRole() {
            return role;
      }

      public void setRole(String role) {
            this.role = role;
      }
}
