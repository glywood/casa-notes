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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.github.glywood.casanotes.json.ContactJson;

public class ContactsResourceTest extends PersonTestBase {

  @Test
  public void addEmptyName() {
    ContactJson json = new ContactJson();
    json.role = "Lawyer";
    Response response = contacts().post(entity(json));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void addEmptyRole() {
    ContactJson json = new ContactJson();
    json.name = "Geoff";
    Response response = contacts().post(entity(json));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void saveNull() {
    Response response = contacts().post(entity(""));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void save() {
    ContactJson json = fullJson();
    ContactJson response = contacts().post(entity(json), ContactJson.class);
    assertNotNull(response.id);
    assertEquals("Geoff", response.name);
    assertEquals("Software Engineer", response.role);
    assertEquals("333-444-5555", response.phone);
    assertEquals("glywood@gmail.com", response.email);
    assertEquals("123 Fake Street\nAnytown, USA", response.mail);
  }

  @Test
  public void overwrite() {
    ContactJson json = validJson();
    ContactJson saved = contacts().post(entity(json), ContactJson.class);
    saved.name = "Michelle";
    Response response = contacts().post(entity(saved));
    assertEquals(200, response.getStatus());
    ContactJson finalJson = contact(saved.id).get(ContactJson.class);
    assertEquals("Michelle", finalJson.name);
  }

  @Test
  public void saveWrongId() {
    ContactJson json = validJson();
    json = contacts().post(entity(json), ContactJson.class);
    json.id = -1;
    Response response = contacts().post(entity(json));
    assertEquals(404, response.getStatus());
  }

  private ContactJson validJson() {
    ContactJson json = new ContactJson();
    json.name = "Geoff";
    json.role = "Software Engineer";
    return json;
  }

  private ContactJson fullJson() {
    ContactJson json = validJson();
    json.email = "glywood@gmail.com";
    json.phone = "333-444-5555";
    json.mail = "123 Fake Street\nAnytown, USA";
    return json;
  }

  private Invocation.Builder contacts() {
    return person().path("contacts").request();
  }

  private Invocation.Builder contact(int id) {
    return person().path("contacts").path(Integer.toString(id)).request();
  }
}
