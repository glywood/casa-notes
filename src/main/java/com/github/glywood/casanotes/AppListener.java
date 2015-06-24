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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apple.eawt.AppEvent.AppReOpenedEvent;
import com.apple.eawt.AppReOpenedListener;

/**
 * A hook for Mac OS X that causes a given URI to be opened when the dock icon
 * is clicked. This uses a Mac-specific API and the class will fail to load on
 * other platforms.
 */
@SuppressWarnings("restriction")
public class AppListener implements AppReOpenedListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppListener.class);

  private final URI uri;

  public AppListener(URI uri) {
    this.uri = uri;
  }

  @Override
  public void appReOpened(AppReOpenedEvent event) {
    try {
      Desktop.getDesktop().browse(uri);
    } catch (IOException e) {
      LOGGER.error("Could not launch browser", e);
    }
  }

}
