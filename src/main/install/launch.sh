#!/bin/bash

DIR=$(cd "$(dirname "$0")"; pwd)

if [ -z "$JAVA_HOME" ]; then
  JAVA_HOME=`/usr/libexec/java_home -F -R -v 1.8+`
fi

if [ -z "$JAVA_HOME" -a -f "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java" ]; then
  JAVA_HOME="/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home"
fi

if [ -z "$JAVA_HOME" ]; then
  osascript -e 'open location "http://www.java.com"'
  osascript -e 'tell application "System Events" to display dialog "Please install Java"'
  exit
fi

exec "$JAVA_HOME/bin/java" -cp "$DIR/../Resources/*" com.github.glywood.casanotes.Main "$DIR/../Web/"
