package com.github.glywood.casanotes;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {
  // Default port the Grizzly HTTP server will listen on
  public static final int DEFAULT_PORT = 6454;

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
   * application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer(URI uri) throws IOException {
    // create a resource config that scans for JAX-RS resources and providers
    // in com.github.glywood.casanotes.resources package
    final ResourceConfig rc = new ResourceConfig()
        .packages("com.github.glywood.casanotes.resources")
        .property("jersey.config.server.wadl.disableWadl", "true");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application
    HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri.resolve("/api"), rc, false);
    
    CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Main.class.getClassLoader(), "/static/");
    staticHttpHandler.setFileCacheEnabled(false);
    server.getServerConfiguration().addHttpHandler(staticHttpHandler);
    
    server.start();
    return server;
  }

  private static URI generateAppUri(int port) {
    return URI.create("http://localhost:" + port);
  }

  /**
   * Main method.
   */
  public static void main(String[] args) throws IOException {
    URI uri = generateAppUri(DEFAULT_PORT);
    startServer(uri);
    Logger.getLogger(Main.class.getName()).info("Server running at " + uri);
    // Desktop.getDesktop().browse(uri);
  }
}
