package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class DiscountCodes {

   private int totalUsedDiscountCodes;
   private long totalCount;
   private List<DiscountCode> discountCodesList;

   public DiscountCodes() {
   }

   public DiscountCodes(List<DiscountCode> discountCodesList, long totalCount, int totalUsedDiscountCodes) {
      this.discountCodesList = discountCodesList;
      this.totalCount = totalCount;
      this.totalUsedDiscountCodes = totalUsedDiscountCodes;
   }

   public List<DiscountCode> getDiscountCodesList() {
      return discountCodesList;
   }

   public void setDiscountCodesList(List<DiscountCode> discountCodesList) {
      this.discountCodesList = discountCodesList;
   }

   public long getTotalCount() {
      return totalCount;
   }

   public void setTotalCount(long totalCount) {
      this.totalCount = totalCount;
   }

   public long getTotalUsedDiscountCodes() {
      return totalUsedDiscountCodes;
   }

   public void setTotalUsedDiscountCodes(int totalUsedDiscountCodes) {
      this.totalUsedDiscountCodes = totalUsedDiscountCodes;
   }
}
