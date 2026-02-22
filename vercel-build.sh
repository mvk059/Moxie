#!/bin/sh
set -eu

J17="${JAVA_HOME_17_X64:-}"

if [ -z "$J17" ]; then
  J17="$(find /usr/lib/jvm -maxdepth 1 -type d | grep -E '17|java-1\.17|temurin-17|openjdk-17' | head -n 1 || true)"
fi

if [ -z "$J17" ] || [ ! -x "$J17/bin/java" ]; then
  echo "No system JDK 17 found; bootstrapping Temurin 17..."
  J17=".vercel/cache/jdk17"
  if [ ! -x "$J17/bin/java" ]; then
    mkdir -p "$J17"
    curl -fsSL "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse" \
      | tar -xz --strip-components=1 -C "$J17"
  fi
fi

# --- Node.js / npm ---
NODE_DIR=".vercel/cache/nodejs"
NODE_VERSION="22.13.1"

if ! command -v npm >/dev/null 2>&1; then
  echo "npm not found on PATH; bootstrapping Node.js ${NODE_VERSION}..."
  if [ ! -x "$NODE_DIR/bin/npm" ]; then
    mkdir -p "$NODE_DIR"
    curl -fsSL "https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.xz" \
      | tar -xJ --strip-components=1 -C "$NODE_DIR"
  fi
  export PATH="$NODE_DIR/bin:$PATH"
fi

JAVA_HOME="$J17" PATH="$JAVA_HOME/bin:$PATH" ./gradlew \
  --no-configuration-cache \
  -Pkotlin.js.nodejs.download=false \
  -Pkotlin.js.yarn=false \
  :composeApp:composeCompatibilityBrowserDistribution
