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

import static com.github.glywood.casanotes.db.generated.Tables.PERSON;

import java.time.Clock;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import com.github.glywood.casanotes.db.generated.tables.records.PersonRecord;
import com.github.glywood.casanotes.json.PersonJson;


@Path("/people")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResource {

  private final DSLContext db;
  private final Clock clock;

  @Inject
  public PeopleResource(DSLContext db, Clock clock) {
    this.db = db;
    this.clock = clock;
  }

  @GET
  public List<PersonJson> getAll() {
    return db.transactionResult(configuration -> {
      return DSL.using(configuration).selectFrom(PERSON).fetch(record -> {
        PersonJson json = new PersonJson();
        json.id = record.getId();
        json.name = record.getName();
        return json;
      });
    });
  }

  @POST
  public PersonJson save(PersonJson json) {
    PersonRecord record = new PersonRecord();
    record.setName(json.name);
    if (json.id == null) {
      json.id = db.insertInto(PERSON).set(record).returning(PERSON.ID).fetchOne().getId();
    } else {
      record.setId(json.id);
      int updated = db.update(PERSON).set(record).execute();
      if (updated != 1) {
        throw new WebApplicationException("Person not found", Status.NOT_FOUND);
      }
    }
    return json;
  }

  @Path("{personId}")
  public PersonResource personResource(@PathParam("personId") int personId) {
    return new PersonResource(db, clock, personId);
  }
}
