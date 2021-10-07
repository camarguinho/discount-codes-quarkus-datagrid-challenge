package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;

import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("/discounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiscountCodesResource {

    @Inject
    @Remote(DiscountCodesCacheCreation.DISCOUNT_CODE_CACHE)
    RemoteCache<String, DiscountCode> discounts;

    @POST
    public Response create(DiscountCode discountCode) {
        if (!discounts.containsKey(discountCode.getName())) {
            discountCode.setUsed(0);
            if (discountCode.getLifespanInSeconds() != null) {
                discounts.put(discountCode.getName(), discountCode, discountCode.getLifespanInSeconds(), TimeUnit.SECONDS);
            } else {
                discounts.put(discountCode.getName(), discountCode);
            }
            return Response.created(URI.create(discountCode.getName())).build();
        }

        return Response.ok(URI.create(discountCode.getName())).build();
    }

    @GET
    @Path("/consume/{name}")
    public Response consume(@PathParam("name") String name) {
        DiscountCode discountCode = discounts.get(name);

        if(discountCode == null) {
            return Response.noContent().build();
        }

        discountCode.setUsed(discountCode.getUsed() + 1);
        discounts.put(name, discountCode);

        return Response.ok(discountCode).build();
    }

    @GET
    @Path("/{type}")
    public DiscountCodes getByType(@PathParam("type") DiscountCodeType type) {
        List<DiscountCode> discountCodes = discounts.values().stream().filter((code) -> code.getType() == type)
              .collect(Collectors.toList());
        return new DiscountCodes(discountCodes, discountCodes.size());
    }

}
