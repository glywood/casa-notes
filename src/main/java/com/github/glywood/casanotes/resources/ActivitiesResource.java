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

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jooq.DSLContext;

import com.github.glywood.casanotes.db.generated.tables.records.ActivityRecord;
import com.github.glywood.casanotes.json.ActivityJson;
import com.github.glywood.casanotes.json.ActivitySummaryJson;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActivitiesResource {

  private final DSLContext db;
  private final Clock clock;
  private final int personId;

  public ActivitiesResource(DSLContext db, Clock clock, int personId) {
    this.db = db;
    this.clock = clock;
    this.personId = personId;
  }

  @GET
  public List<ActivitySummaryJson> getAll() {
    return db
        .select(ACTIVITY.ID, ACTIVITY.TYPE, ACTIVITY.DESCRIPTION, ACTIVITY.DATE, ACTIVITY.HOURS)
        .from(ACTIVITY)
        .where(ACTIVITY.PERSON_ID.eq(personId))
        .orderBy(ACTIVITY.DATE.desc())
        .fetch(record -> {
          ActivitySummaryJson json = new ActivitySummaryJson();
          json.id = record.value1();
          json.type = record.value2();
          json.description = record.value3();
          json.date = record.value4();
          json.hours = record.value5();
          return json;
    });
  }

  @GET
  @Path("{id}")
  public ActivityJson get(@PathParam("id") int id) {
    ActivityJson result = db.selectFrom(ACTIVITY).where(ACTIVITY.ID.eq(id))
        .and(ACTIVITY.PERSON_ID.eq(personId)).fetchOne(record -> {
          ActivityJson json = new ActivityJson();
          json.id = record.getId();
          json.description = record.getDescription();
          json.type = record.getType();
          json.date = record.getDate();
          json.hours = record.getHours();
          json.summary = record.getSummary();
          json.successes = record.getSuccesses();
          json.concerns = record.getConcerns();
          json.selfesteem = record.getSelfesteem();
          json.trust = record.getTrust();
          json.cultural = record.getCultural();
          json.experiences = record.getExperiences();
          json.educational = record.getEducational();
          json.extracurricular = record.getExtracurricular();
          json.healthy = record.getHealthy();
          json.milestones = record.getMilestones();
          return json;
        });
    if (result == null) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
    return result;
  }

  @POST
  public ActivityJson save(@NotNull @Valid ActivityJson json) {
    if (json.hours < 0) {
      throw new WebApplicationException("hours must be non-negative", Status.BAD_REQUEST);
    }

    Instant now = Instant.now(clock);
    ActivityRecord record = ACTIVITY.newRecord();
    record.setPersonId(personId);
    record.setType(json.type);
    record.setModifiedAt(now);
    record.setDescription(json.description);
    record.setDate(json.date);
    record.setHours(json.hours);
    record.setSummary(json.summary);
    record.setSuccesses(json.successes);
    record.setConcerns(json.concerns);
    record.setSelfesteem(json.selfesteem);
    record.setTrust(json.trust);
    record.setCultural(json.cultural);
    record.setExperiences(json.experiences);
    record.setEducational(json.educational);
    record.setExtracurricular(json.extracurricular);
    record.setHealthy(json.healthy);
    record.setMilestones(json.milestones);

    if (json.id == null) {
      record.setCreatedAt(now);
      json.id = db.insertInto(ACTIVITY).set(record).returning(ACTIVITY.ID).fetchOne().getId();
    } else {
      record.setId(json.id);
      int updated = db.update(ACTIVITY).set(record).where(ACTIVITY.ID.eq(json.id)).execute();
      if (updated == 0) {
        throw new WebApplicationException("Activity not found", Status.NOT_FOUND);
      }
    }
    return json;
  }

  @DELETE
  @Path("{id}")
  public void delete(@PathParam("id") int id) {
    db.deleteFrom(ACTIVITY).where(ACTIVITY.ID.eq(id)).and(ACTIVITY.PERSON_ID.eq(personId))
        .execute();
  }
}
