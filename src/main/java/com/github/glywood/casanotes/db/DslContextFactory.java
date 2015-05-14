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

import javax.inject.Inject;
import javax.sql.DataSource;

import org.glassfish.hk2.api.Factory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

public class DslContextFactory implements Factory<DSLContext> {

  private final DataSource dataSource;

  @Inject
  public DslContextFactory(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public DSLContext provide() {
    Settings settings = new Settings().withRenderSchema(false);
    return DSL.using(dataSource, SQLDialect.H2, settings);
  }

  @Override
  public void dispose(DSLContext dbi) {
  }
}
