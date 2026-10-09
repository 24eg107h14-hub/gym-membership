package com.anurag.gymMemberShip.dto;

import jakarta.validation.constraints.NotBlank;

public class BranchDto {

      private Long id;

      @NotBlank(message = "Branch name is required")
      private String name;

      @NotBlank(message = "Address is required")
      private String address;

      private String city;
      private String state;
      private String phone;
      private String email;
      private boolean active = true;

      public BranchDto() {
      }

      public BranchDto(Long id, String name, String address, String city, String state, String phone, String email, boolean active) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.city = city;
            this.state = state;
            this.phone = phone;
            this.email = email;
            this.active = active;
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

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public String getCity() {
            return city;
      }

      public void setCity(String city) {
            this.city = city;
      }

      public String getState() {
            return state;
      }

      public void setState(String state) {
            this.state = state;
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

      public boolean isActive() {
            return active;
      }

      public void setActive(boolean active) {
            this.active = active;
      }
}
