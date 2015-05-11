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

import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.github.glywood.casanotes.json.PersonJson;

public class PeopleResourceTest extends ResourceTestBase {

  @Test
  public void add() {
    PersonJson json = new PersonJson();
    json.name = "GEOFF LYWOOD";
    Response response = peopleResource().post(entity(json));

    assertEquals(204, response.getStatus());

    List<PersonJson> newResult = peopleResource().get(new GenericType<List<PersonJson>>() {});
    assertEquals(1, newResult.size());
  }

  @Test
  public void emptyList() {
    List<PersonJson> newResult = peopleResource().get(new GenericType<List<PersonJson>>() {});
    assertEquals(0, newResult.size());
  }

  private Invocation.Builder peopleResource() {
    return target().path("/people").request();
  }
}
