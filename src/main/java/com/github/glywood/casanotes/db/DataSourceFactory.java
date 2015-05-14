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
package com.github.glywood.casanotes.db;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.glassfish.hk2.api.Factory;
import org.h2.jdbcx.JdbcDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory implements Factory<DataSource> {

  Logger logger = Logger.getLogger(DataSourceFactory.class.getName());

  private final String jdbcUrl;

  public DataSourceFactory(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  @Override
  public DataSource provide() {
    JdbcDataSource h2DataSource = new JdbcDataSource();
    h2DataSource.setUrl(jdbcUrl);

    HikariConfig configuration = new HikariConfig();
    configuration.setDataSource(h2DataSource);
    HikariDataSource hikariDataSource = new HikariDataSource(configuration);

    Flyway flyway = new Flyway();
    flyway.setDataSource(hikariDataSource);
    flyway.migrate();

    return hikariDataSource;
  }

  @Override
  public void dispose(DataSource dataSource) {
  }
}
