package com.github.glywood.casanotes;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MyResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    int port;
    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("localhost", 0));
      port = socket.getLocalPort();
      socket.close();
    }
    URI uri = new URI("http://localhost:" + port);

    // start the server
    server = Main.startServer(uri);
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(uri).path("api");
  }

  @After
  public void tearDown() throws Exception {
    server.shutdownNow();
  }

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  @Test
  public void testGetIt() {
    Map<String, String> responseMsg = target.path("myresource").request()
        .get(new GenericType<Map<String, String>>() {});
    assertEquals(ImmutableMap.of("hello", "Got it!"), responseMsg);
  }
}
