package com.anurag.gymMemberShip.enums;

import java.time.LocalDate;

public enum MembershipStatus {
      ACTIVE,
      FROZEN,
      CANCELLED,
      EXPIRED;

      public static MembershipStatus of(LocalDate expiryDate, MembershipStatus currentStatus) {
            if (currentStatus == FROZEN || currentStatus == CANCELLED) {
                  return currentStatus;
            }
            if (expiryDate == null || expiryDate.isBefore(LocalDate.now())) {
                  return EXPIRED;
            }
            return ACTIVE;
      }
}
