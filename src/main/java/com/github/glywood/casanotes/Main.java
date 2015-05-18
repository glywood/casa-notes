/**
 * Copyright (C) 2015 Geoff Lywood.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.github.glywood.casanotes;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.time.Clock;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jooq.DSLContext;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.glywood.casanotes.db.DataSourceFactory;
import com.github.glywood.casanotes.db.DslContextFactory;
import com.github.glywood.casanotes.json.ser.ObjectMapperProvider;


/**
 * Main class.
 */
public class Main {
  // Default port the Grizzly HTTP server will listen on
  public static final int DEFAULT_PORT = 6454;

  public static final String JDBC_URL = "jdbc:h2:~/.casanotes/notes";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
   * application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer(URI uri, String jdbcUrl, Clock clock, String webDir)
      throws IOException {
    if (!SLF4JBridgeHandler.isInstalled()) {
      SLF4JBridgeHandler.removeHandlersForRootLogger();
      SLF4JBridgeHandler.install();
      BasicConfigurator.configure();
    }

    AbstractBinder binder = new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(new DataSourceFactory(jdbcUrl)).to(DataSource.class).in(Singleton.class);
        bindFactory(DslContextFactory.class).to(DSLContext.class).in(Singleton.class);
        bind(clock).to(Clock.class);
      }
    };

    // create a resource config that scans for JAX-RS resources and providers
    // in com.github.glywood.casanotes.resources package
    final ResourceConfig rc = new ResourceConfig()
        .packages("com.github.glywood.casanotes.resources")
        .register(binder)
        .register(LoggingExceptionMapper.class)
        .register(ObjectMapperProvider.class)
        .property("jersey.config.server.wadl.disableWadl", "true");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application
    HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri.resolve("/api"), rc, false);

    if (webDir != null) {
      StaticHttpHandler staticHttpHandler = new StaticHttpHandler(webDir);
      staticHttpHandler.setFileCacheEnabled(false);
      server.getServerConfiguration().addHttpHandler(staticHttpHandler);
    }

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
    try {
      startServer(uri, JDBC_URL, Clock.systemUTC(), args[0]);
      Logger.getLogger(Main.class.getName()).info("Server running at " + uri);
    } catch (IOException e) {
      Logger.getLogger(Main.class.getName()).info("Something already running on " + uri);
    }
    Desktop.getDesktop().browse(uri);
  }
}
