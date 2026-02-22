#!/bin/sh
set -eu

# --- JDK 17 ---
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

export JAVA_HOME="$J17"
export PATH="$JAVA_HOME/bin:$PATH"

# Node.js v20+ on Amazon Linux 2 (Vercel's build OS) requires libatomic.so.1.
# The library ships with gcc but isn't on the default linker path.
# Find it and expose it so Kotlin's downloaded Node.js binary can start.
_libatomic="$(find /usr/lib64 /usr/lib /usr/lib/gcc -maxdepth 4 -name 'libatomic.so.1' 2>/dev/null | head -n 1 || true)"
if [ -n "$_libatomic" ]; then
  export LD_LIBRARY_PATH="$(dirname "$_libatomic")${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}"
fi

# Route Gradle's user home into Vercel's persistent cache so Kotlin's
# managed Node.js installation is preserved between builds.
export GRADLE_USER_HOME=".vercel/cache/gradle-home"

./gradlew \
  --no-configuration-cache \
  -Pkotlin.js.nodejs.download=true \
  -Pkotlin.js.yarn=false \
  :composeApp:composeCompatibilityBrowserDistribution
