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
package com.github.glywood.casanotes.json;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportJson {

  @JsonProperty("duration")
  public Duration duration;

  @JsonProperty("successes")
  public String successes;

  @JsonProperty("concerns")
  public String concerns;

  @JsonProperty("selfesteem")
  public boolean selfesteem;

  @JsonProperty("trust")
  public boolean trust;

  @JsonProperty("cultural")
  public boolean cultural;

  @JsonProperty("experiences")
  public boolean experiences;

  @JsonProperty("educational")
  public boolean educational;

  @JsonProperty("extracurricular")
  public boolean extracurricular;

  @JsonProperty("healthy")
  public boolean healthy;

  @JsonProperty("milestones")
  public boolean milestones;
}
