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

import javax.ws.rs.client.WebTarget;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.glywood.casanotes.json.PersonJson;

public class PersonTestBase extends ResourceTestBase {

  private static int personId;

  @BeforeClass
  public static void createFakePerson() {
    PersonJson request = new PersonJson();
    request.name = "Geoff Lywood";
    PersonJson response = target().path("people").request().post(entity(request), PersonJson.class);
    personId = response.id;
  }

  @AfterClass
  public static void deleteFakePerson() {
    target().path("people/" + personId).request().delete();
  }

  protected static WebTarget person() {
    return target().path("people/" + personId);
  }
}
