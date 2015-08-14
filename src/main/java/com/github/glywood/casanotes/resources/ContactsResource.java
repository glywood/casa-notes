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

import static com.github.glywood.casanotes.db.generated.Tables.CONTACT;

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

import com.github.glywood.casanotes.db.generated.tables.records.ContactRecord;
import com.github.glywood.casanotes.json.ContactJson;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactsResource {

  private final DSLContext db;
  private final int personId;

  public ContactsResource(DSLContext db, int personId) {
    this.db = db;
    this.personId = personId;
  }

  @GET
  public List<ContactJson> getAll() {
    return db.selectFrom(CONTACT)
        .where(CONTACT.PERSON_ID.eq(personId))
        .fetch(this::toJson);
  }

  @GET
  @Path("{id}")
  public ContactJson get(@PathParam("id") int id) {
    ContactRecord record = db.selectFrom(CONTACT)
        .where(CONTACT.PERSON_ID.eq(personId))
        .and(CONTACT.ID.eq(id))
        .fetchOne();
    if (record == null) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
    return toJson(record);
  }

  @POST
  public ContactJson save(@NotNull @Valid ContactJson json) {
    ContactRecord record = CONTACT.newRecord();
    record.setPersonId(personId);
    record.setName(json.name);
    record.setRole(json.role);
    record.setPhone(json.phone);
    record.setEmail(json.email);
    record.setMail(json.mail);
    if (json.id == null) {
      json.id = db.insertInto(CONTACT).set(record).returning(CONTACT.ID).fetchOne().getId();
    } else {
      record.setId(json.id);
      int updated = db.update(CONTACT).set(record).where(CONTACT.ID.eq(json.id))
          .and(CONTACT.PERSON_ID.eq(personId)).execute();
      if (updated == 0) {
        throw new WebApplicationException("Contact not found", Status.NOT_FOUND);
      }
    }
    return json;
  }

  @DELETE
  @Path("{id}")
  public void delete(@PathParam("id") int id) {
    db.deleteFrom(CONTACT).where(CONTACT.ID.eq(id)).and(CONTACT.PERSON_ID.eq(personId)).execute();
  }

  private ContactJson toJson(ContactRecord record) {
    ContactJson result = new ContactJson();
    result.id = record.getId();
    result.name = record.getName();
    result.role = record.getRole();
    result.phone = record.getPhone();
    result.email = record.getEmail();
    result.mail = record.getMail();
    return result;
  }
}
