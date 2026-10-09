package com.anurag.gymMemberShip.dto;

import java.time.LocalDate;

public class MemberResponseDto {

      private Long id;
      private Long userId;
      private String fullName;
      private String phone;
      private String email;
      private String gender;
      private String address;
      private LocalDate dob;
      private Long planId;
      private String planName;
      private LocalDate joinDate;
      private LocalDate expiryDate;
      private String status;
      private long daysRemaining;

      public MemberResponseDto() {
      }

      public MemberResponseDto(Long id, Long userId, String fullName, String phone, String email, String gender, String address, LocalDate dob, Long planId, String planName, LocalDate joinDate, LocalDate expiryDate, String status, long daysRemaining) {
            this.id = id;
            this.userId = userId;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
            this.address = address;
            this.dob = dob;
            this.planId = planId;
            this.planName = planName;
            this.joinDate = joinDate;
            this.expiryDate = expiryDate;
            this.status = status;
            this.daysRemaining = daysRemaining;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public Long getUserId() {
            return userId;
      }

      public void setUserId(Long userId) {
            this.userId = userId;
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

      public String getPlanName() {
            return planName;
      }

      public void setPlanName(String planName) {
            this.planName = planName;
      }

      public LocalDate getJoinDate() {
            return joinDate;
      }

      public void setJoinDate(LocalDate joinDate) {
            this.joinDate = joinDate;
      }

      public LocalDate getExpiryDate() {
            return expiryDate;
      }

      public void setExpiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
      }

      public String getStatus() {
            return status;
      }

      public void setStatus(String status) {
            this.status = status;
      }

      public long getDaysRemaining() {
            return daysRemaining;
      }

      public void setDaysRemaining(long daysRemaining) {
            this.daysRemaining = daysRemaining;
      }
}
