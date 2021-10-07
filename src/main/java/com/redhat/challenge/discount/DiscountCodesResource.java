package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;

import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Objects;
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
        Integer totalUsedDicountCodes = discountCodes.stream().mapToInt(d -> d.getUsed()).sum();
        return new DiscountCodes(discountCodes, discountCodes.size(), totalUsedDicountCodes);
    }

    @DELETE
    public Response clearCache() {
        discounts.clear();
        return Response.noContent().build();
    }

    // Concurrent version
    @POST
    @Path("/concurrent")
    public Response createConcurrent(DiscountCode discountCode) {

        // workarround for putIfAbsent issue, this is not very good because now we have two rountrips to the cache,
        // and we cannot tell if value has been created or not in certain race condition situations 
        // for example when 'discountCodeDoesNotExists'=true, but putIfAbsent detects a valid current value
        Boolean discountCodeDoesNotExists = !discounts.containsKey(discountCode.getName());

        discountCode.setUsed(0);
        DiscountCode previousValue = null;
         if (discountCode.getLifespanInSeconds() != null) {
            // putIfAbsent call is not returning previous value
            previousValue = discounts.putIfAbsent(discountCode.getName(), discountCode, discountCode.getLifespanInSeconds(), TimeUnit.SECONDS );
        } else {
            // putIfAbsent call is not returning previous value
            previousValue = discounts.putIfAbsent(discountCode.getName(), discountCode);
        }

        // putIfAbsent call is not returning previous value, but is functioning properly on the cache operation, javadocs states that:
        // Returns:
        //   the previous value associated with the specified key, or null if there was no mapping for the key.
        //   (A null return can also indicate that the map previously associated null with the key, if the implementation supports null values.)
        // Another Source:
        // RemoteCache extends Basic Cache: https://docs.jboss.org/infinispan/12.1/apidocs/org/infinispan/client/hotrod/RemoteCache.html
        // Which contains the definition: https://docs.jboss.org/infinispan/12.1/apidocs/org/infinispan/commons/api/BasicCache.html#putIfAbsent(K,V,long,java.util.concurrent.TimeUnit)
        // Returns:
        //   the value being replaced, or null if nothing is being replaced.
    
        // '&& discountCodeDoesNotExists' overwrites 'Objects.isNull(previousValue)' condition for workarround
        if(Objects.isNull(previousValue) && discountCodeDoesNotExists) {
            return Response.created(URI.create(discountCode.getName())).build();
        } else {
            return Response.ok(URI.create(discountCode.getName())).build();
        }
        
    }

    // Concurrent version
    @GET
    @Path("/concurrent/consume/{name}")
    public Response consumeConcurrent(@PathParam("name") String name) {

        DiscountCode discountCode = null;
        discountCode = discounts.computeIfPresent(name, (String key, DiscountCode value) -> { 
            value.setUsed(value.getUsed() + 1); 
            return value;
        });

        if(discountCode == null) {
            return Response.noContent().build();
        } else {
            return Response.ok(discountCode).build();
        }
        
    }

    // Concurrent version - doesn't change because values() is lazy, evaluated at infinispan server
    @GET
    @Path("/concurrent/{type}")
    public DiscountCodes getByTypeConcurrent(@PathParam("type") DiscountCodeType type) {
        List<DiscountCode> discountCodes = discounts.values().stream().filter((code) -> code.getType() == type)
              .collect(Collectors.toList());
        Integer totalUsedDicountCodes = discountCodes.stream().mapToInt(d -> d.getUsed()).sum();
        return new DiscountCodes(discountCodes, discountCodes.size(), totalUsedDicountCodes);
    }
}
