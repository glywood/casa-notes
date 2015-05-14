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

import java.sql.Date;
import java.time.LocalDate;

import org.jooq.Converter;

public class LocalDateConverter implements Converter<Date, LocalDate> {
  private static final long serialVersionUID = 6307467431812048826L;

  @Override
  public LocalDate from(Date date) {
    return date == null ? null : date.toLocalDate();
  }

  @Override
  public Date to(LocalDate ld) {
    return ld == null ? null : Date.valueOf(ld);
  }

  @Override
  public Class<Date> fromType() {
    return Date.class;
  }

  @Override
  public Class<LocalDate> toType() {
    return LocalDate.class;
  }
}
