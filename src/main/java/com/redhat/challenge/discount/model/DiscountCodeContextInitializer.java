package com.redhat.challenge.discount.model;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses = { DiscountCode.class, DiscountCodeType.class }, schemaPackageName = "discount_code")
interface DiscountCodeContextInitializer extends SerializationContextInitializer {
}