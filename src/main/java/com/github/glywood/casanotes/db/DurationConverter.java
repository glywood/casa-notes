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

import java.time.Duration;

import org.jooq.Converter;

public class DurationConverter implements Converter<String, Duration> {
  private static final long serialVersionUID = -1998208035074410456L;

  @Override
  public Duration from(String duration) {
    return duration == null ? null : Duration.parse(duration);
  }

  @Override
  public String to(Duration duration) {
    return duration == null ? null : duration.toString();
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Class<Duration> toType() {
    return Duration.class;
  }
}
