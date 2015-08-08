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

import java.time.Clock;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;

import com.github.glywood.casanotes.db.generated.tables.records.PersonRecord;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

  private final DSLContext db;
  private final Clock clock;
  private final PersonRecord person;

  public PersonResource(DSLContext db, Clock clock, PersonRecord person) {
    this.db = db;
    this.clock = clock;
    this.person = person;
  }

  @DELETE
  public void delete() {
    person.delete();
  }

  @Path("activities")
  public ActivitiesResource activities() {
    return new ActivitiesResource(db, clock, person.getId());
  }

  @Path("reports")
  public ReportsResource reports() {
    return new ReportsResource(db, person.getId());
  }

  @Path("contacts")
  public ContactsResource contacts() {
    return new ContactsResource(db, person.getId());
  }
}
