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
package com.github.glywood.casanotes.resources;

import static com.github.glywood.casanotes.db.generated.Tables.ACTIVITY;

import java.time.Duration;
import java.time.LocalDate;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.jooq.DSLContext;
import org.jooq.Result;

import com.github.glywood.casanotes.db.generated.tables.records.ActivityRecord;
import com.github.glywood.casanotes.json.ReportJson;

public class ReportsResource {

  private final DSLContext db;
  private final int personId;

  public ReportsResource(DSLContext db, int personId) {
    this.db = db;
    this.personId = personId;
  }

  @GET
  public ReportJson generateReport(@QueryParam("start") String start, @QueryParam("end") String end) {
    if (start == null || end == null) {
      throw new WebApplicationException("Must specify both start and end", Status.BAD_REQUEST);
    }
    LocalDate startDate = LocalDate.parse(start);
    LocalDate endDate = LocalDate.parse(end);

    Result<ActivityRecord> records = db.selectFrom(ACTIVITY).where(ACTIVITY.PERSON_ID.eq(personId))
        .and(ACTIVITY.DATE.between(startDate, endDate)).orderBy(ACTIVITY.DATE).fetch();

    Duration duration = Duration.ZERO;
    StringBuilder successes = new StringBuilder();
    StringBuilder concerns = new StringBuilder();
    boolean selfesteem = false;
    boolean trust = false;
    boolean cultural = false;
    boolean experiences = false;
    boolean educational = false;
    boolean extracurricular = false;
    boolean healthy = false;
    boolean milestones = false;
    for (ActivityRecord record : records) {
      duration = duration.plus(record.getDuration());

      if (!record.getSuccesses().trim().isEmpty()) {
        successes.append(record.getSuccesses().trim());
        successes.append("\r\n\r\n");
      }

      if (!record.getConcerns().trim().isEmpty()) {
        concerns.append(record.getConcerns().trim());
        concerns.append("\r\n\r\n");
      }

      selfesteem |= record.getSelfesteem();
      trust |= record.getTrust();
      cultural |= record.getCultural();
      experiences |= record.getExperiences();
      educational |= record.getEducational();
      extracurricular |= record.getExtracurricular();
      healthy |= record.getHealthy();
      milestones |= record.getMilestones();
    }

    ReportJson json = new ReportJson();
    json.duration = duration;
    json.successes = successes.toString().trim();
    json.concerns = concerns.toString().trim();
    json.selfesteem = selfesteem;
    json.trust = trust;
    json.cultural = cultural;
    json.experiences = experiences;
    json.educational = educational;
    json.extracurricular = extracurricular;
    json.healthy = healthy;
    json.milestones = milestones;
    return json;
  }
}
