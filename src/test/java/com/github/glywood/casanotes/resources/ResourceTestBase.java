package com.github.glywood.casanotes.resources;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
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
import com.github.glywood.casanotes.json.ser.ObjectMapperProvider;

public class ResourceTestBase {

  private static final Logger LOGGER = Logger.getLogger(ResourceTestBase.class.getName());

  private static Clock clock;
  private static HttpServer server;
  private static Client client;
  private static WebTarget target;

  @BeforeClass
  public static void bringUpServer() throws Exception {
    int port;
    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(new InetSocketAddress("localhost", 0));
      port = socket.getLocalPort();
      socket.close();
    }
    URI uri = new URI("http://localhost:" + port);

    clock = Clock.fixed(Instant.parse("2015-03-22T13:45:23Z"), ZoneOffset.UTC);

    // start the server
    server = Main.startServer(uri, "jdbc:h2:mem:test", clock);

    // create the client
    client = ClientBuilder.newClient().register(ObjectMapperProvider.class);
    target = client.target(uri).path("api");
  }

  @Before
  public void resetDatabase() throws Exception {
    LOGGER.info("resetting database");
    DSL.using("jdbc:h2:mem:test").transaction(
        config -> {
          List<String> tableNames = DSL.using(config).select(DSL.field("TABLE_NAME"))
              .from("INFORMATION_SCHEMA.TABLES").where("TABLE_TYPE LIKE 'TABLE'")
              .fetch(DSL.field("TABLE_NAME", String.class));
          for (String tableName : tableNames) {
            if (!tableName.equalsIgnoreCase("schema_version")) {
              DSL.using(config).truncate(tableName).execute();
            }
          }
        });
  }

  @AfterClass
  public static void shutdownServer() throws Exception {
    server.shutdownNow();
  }

  protected static Client client() {
    return client;
  }

  protected static WebTarget target() {
    return target;
  }

  protected static <T> Entity<T> entity(T json) {
    return Entity.entity(json, MediaType.APPLICATION_JSON_TYPE);
  }

  protected static Clock clock() {
    return clock;
  }
}
