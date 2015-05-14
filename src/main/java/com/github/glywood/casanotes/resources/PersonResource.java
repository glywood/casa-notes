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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

  private final DSLContext db;
  private final Clock clock;
  private final int personId;

  public PersonResource(DSLContext db, Clock clock, int personId) {
    this.db = db;
    this.clock = clock;
    this.personId = personId;
  }

  @DELETE
  public void delete() {
    db.delete(PERSON).where(PERSON.ID.eq(personId)).execute();
  }

  @Path("activities")
  public ActivitiesResource activities() {
    return new ActivitiesResource(db, clock, personId);
  }
}
