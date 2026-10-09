package com.anurag.gymMemberShip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class MemberDto {

      @NotBlank(message = "Full name is required")
      private String fullName;

      @NotBlank(message = "Phone number is required")
      @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
      private String phone;

      @Email(message = "Invalid email format")
      private String email;

      private String gender;

      private String address;

      private LocalDate dob;

      private Long planId;

      private LocalDate joinDate;

      public MemberDto() {
      }

      public MemberDto(String fullName, String phone, String email, String gender, String address, LocalDate dob, Long planId, LocalDate joinDate) {
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
            this.address = address;
            this.dob = dob;
            this.planId = planId;
            this.joinDate = joinDate;
      }

      public String getFullName() {
            return fullName;
      }

      public void setFullName(String fullName) {
            this.fullName = fullName;
      }

      public String getPhone() {
            return phone;
      }

      public void setPhone(String phone) {
            this.phone = phone;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
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

      public Long getPlanId() {
            return planId;
      }

      public void setPlanId(Long planId) {
            this.planId = planId;
      }

      public LocalDate getJoinDate() {
            return joinDate != null ? joinDate : LocalDate.now();
      }

      public void setJoinDate(LocalDate joinDate) {
            this.joinDate = joinDate;
      }
}
