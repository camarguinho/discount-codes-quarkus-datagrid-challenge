package com.redhat.challenge.discount.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

@RegisterForReflection
public class DiscountCode {

   private String name;
   private Integer amount;
   private String enterprise;
   private DiscountCodeType type;
   private Integer used;
   private Integer lifespanInSeconds;

   public DiscountCode() {
   }

   @ProtoFactory
   public DiscountCode(String name, Integer amount, String enterprise, DiscountCodeType type, Integer used, Integer lifespanInSeconds) {
      this.name = name;
      this.amount = amount;
      this.enterprise = enterprise;
      this.type = type;
      this.used = used;
      this.lifespanInSeconds = lifespanInSeconds;
   }

   @ProtoField(number = 1)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @ProtoField(number = 2)
   public Integer getAmount() {
      return amount;
   }

   public void setAmount(Integer amount) {
      this.amount = amount;
   }

   @ProtoField(number = 3)
   public String getEnterprise() {
      return enterprise;
   }

   public void setEnterprise(String enterprise) {
      this.enterprise = enterprise;
   }

   @ProtoField(number = 4)
   public DiscountCodeType getType() {
      return type;
   }

   public void setType(DiscountCodeType type) {
      this.type = type;
   }

   @ProtoField(number = 5)
   public Integer getUsed() {
      return used;
   }

   public void setUsed(Integer used) {
      this.used = used;
   }

   @ProtoField(number = 6)
   public Integer getLifespanInSeconds() {
      return lifespanInSeconds;
   }

   public void setLifespanInSeconds(Integer lifespanInSeconds) {
      this.lifespanInSeconds = lifespanInSeconds;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      DiscountCode that = (DiscountCode) o;
      return Objects.equals(name, that.name) && Objects.equals(amount, that.amount) && Objects
            .equals(enterprise, that.enterprise) && type == that.type && Objects.equals(used, that.used)
            && Objects.equals(lifespanInSeconds, that.lifespanInSeconds);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, amount, enterprise, type, used, lifespanInSeconds);
   }

   @Override
   public String toString() {
      return "DiscountCode{" + "name='" + name + '\'' + ", amount=" + amount + ", enterprise='" + enterprise + '\''
            + ", type=" + type + ", used=" + used + ", lifespanInSeconds="+ lifespanInSeconds + '}';
   }
}
