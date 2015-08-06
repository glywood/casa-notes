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
package com.github.glywood.casanotes;

import java.security.SecureRandom;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class LoggingExceptionMapper implements ExceptionMapper<Throwable> {
  private static final Logger logger = LoggerFactory.getLogger(LoggingExceptionMapper.class);
  private static final SecureRandom random = new SecureRandom();

  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof WebApplicationException) {
      logger.debug("Failure in resource method", exception);

      Response response = ((WebApplicationException) exception).getResponse();
      if (response.hasEntity()) {
        return response;
      } else {
        return Response.status(response.getStatusInfo()).entity("Error: " + exception.getMessage())
            .build();
      }
    } else {
      String rand = Long.toHexString(random.nextLong());
      logger.debug("Failure in resource method, id " + rand, exception);
      return Response.serverError().entity("Server error " + rand).build();
    }
  }
}
