package com.github.glywood.casanotes.resources;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.jooq.impl.DSL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.github.glywood.casanotes.Main;

public class ResourceTestBase {

  private static final Logger logger = Logger.getLogger(ResourceTestBase.class.getName());

  private static HttpServer server;
  private static WebTarget target;

  @BeforeClass
  public static void beforeClass() throws Exception {
    int port;
    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("localhost", 0));
      port = socket.getLocalPort();
      socket.close();
    }
    URI uri = new URI("http://localhost:" + port);

    // start the server
    server = Main.startServer(uri, "jdbc:h2:mem:test");

    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(uri).path("api");
  }

  @Before
  public void setUp() throws Exception {
    logger.info("resetting database");
    DSL.using("jdbc:h2:mem:test").transaction(
        config -> {
          List<String> tableNames = DSL.using(config).select(DSL.field("TABLE_NAME"))
              .from("INFORMATION_SCHEMA.TABLES").where("TABLE_TYPE LIKE 'TABLE'")
              .fetch(DSL.field("TABLE_NAME", String.class));
          for (String tableName : tableNames) {
            DSL.using(config).truncate(tableName).execute();
          }
        });
  }

  @AfterClass
  public static void afterClass() throws Exception {
    server.shutdownNow();
  }

  protected WebTarget target() {
    return target;
  }

  protected static <T> Entity<T> entity(T json) {
    return Entity.entity(json, MediaType.APPLICATION_JSON_TYPE);
  }
}
