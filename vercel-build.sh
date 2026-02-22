#!/bin/sh
set -eu

J17="${JAVA_HOME_17_X64:-}"

if [ -z "$J17" ]; then
  J17="$(find /usr/lib/jvm -maxdepth 1 -type d | grep -E '17|java-1\.17|temurin-17|openjdk-17' | head -n 1 || true)"
fi

if [ -z "$J17" ] || [ ! -x "$J17/bin/java" ]; then
  echo "No valid JDK 17 found"
  find /usr/lib/jvm -maxdepth 2 -type f -name java 2>/dev/null || true
  exit 1
fi

JAVA_HOME="$J17" PATH="$JAVA_HOME/bin:$PATH" ./gradlew :composeApp:composeCompatibilityBrowserDistribution
