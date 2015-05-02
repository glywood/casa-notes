package com.github.glywood.casanotes.resources;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableMap;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, String> getIt() {
    return ImmutableMap.of("hello", "Got it!");
  }
}
