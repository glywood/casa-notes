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

import java.time.LocalDate;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.github.glywood.casanotes.json.ActivityJson;
import com.github.glywood.casanotes.json.ActivitySummaryJson;

public class ActivitiesResourceTest extends PersonTestBase {

  @Test
  public void addUnfinished() {
    ActivityJson toCreate = new ActivityJson();
    Response response = activities().post(entity(toCreate));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void addEmptyName() {
    ActivityJson toCreate = new ActivityJson();
    toCreate.name = "";
    toCreate.date = LocalDate.now(clock());
    toCreate.summary = "blarg";
    toCreate.successes = "none";
    toCreate.concerns = "nothing";
    Response response = activities().post(entity(toCreate));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void addNegativeHours() {
    ActivityJson toCreate = new ActivityJson();
    toCreate.name = "fdsa";
    toCreate.date = LocalDate.now(clock());
    toCreate.hours = -.05;
    toCreate.summary = "blarg";
    toCreate.successes = "none";
    toCreate.concerns = "nothing";
    Response response = activities().post(entity(toCreate));
    assertEquals(400, response.getStatus());
  }

  @Test
  public void addThenGet() {
    ActivityJson toCreate = new ActivityJson();
    toCreate.name = "fdsa";
    toCreate.date = LocalDate.now(clock());
    toCreate.hours = 2.5;
    toCreate.summary = "blarg";
    toCreate.successes = "none";
    toCreate.concerns = "nothing";
    toCreate.educational = true;
    Response postResponse = activities().post(entity(toCreate));
    assertEquals(204, postResponse.getStatus());

    List<ActivitySummaryJson> summaries = activities().get(
        new GenericType<List<ActivitySummaryJson>>() {});
    assertEquals(1, summaries.size());
    assertEquals("fdsa", summaries.get(0).name);
    assertEquals(LocalDate.now(clock()), summaries.get(0).date);
    assertEquals(2.5, summaries.get(0).hours, 0.0);

    ActivityJson getResponse = activity(summaries.get(0).id).get(ActivityJson.class);
    assertEquals("fdsa", getResponse.name);
    assertNotNull(getResponse.id);
    assertEquals(LocalDate.now(clock()), getResponse.date);
    assertEquals(true, getResponse.educational);
    assertEquals(2.5, getResponse.hours, 0.0);
  }

  private Invocation.Builder activities() {
    return person().path("activities").request();
  }

  private Invocation.Builder activity(int id) {
    return person().path("activities").path(Integer.toString(id)).request();
  }
}
