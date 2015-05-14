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

import java.sql.Timestamp;
import java.time.Instant;

import org.jooq.Converter;

public class InstantConverter implements Converter<Timestamp, Instant> {
  private static final long serialVersionUID = -5180530371466039198L;

  @Override
  public Instant from(Timestamp ts) {
    return ts == null ? null : ts.toInstant();
  }

  @Override
  public Timestamp to(Instant ins) {
    return ins == null ? null : Timestamp.from(ins);
  }

  @Override
  public Class<Timestamp> fromType() {
    return Timestamp.class;
  }

  @Override
  public Class<Instant> toType() {
    return Instant.class;
  }
}
