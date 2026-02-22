#!/bin/sh
set -eu

# ── JDK 17 ───────────────────────────────────────────────────────────────────
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

# ── libatomic.so.1 ───────────────────────────────────────────────────────────
# Node.js v20+ (used by Kotlin for the wasmJs target) requires libatomic.so.1.
# Vercel's Amazon Linux 2 image does not include it by default.
# Strategy:
#   1. Look for it on the system (present if GCC is installed).
#   2. If absent, download the libatomic RPM from the AL2 repo and extract it.
#   3. Cache the result in .vercel/cache/libatomic so subsequent builds reuse it.
LIBATOMIC_CACHE=".vercel/cache/libatomic"

_libatomic="$(find /usr/lib64 /usr/lib /usr/lib/gcc /usr/lib64/gcc \
  -maxdepth 8 -name 'libatomic.so.1*' 2>/dev/null | head -n 1 || true)"

# If found but only the versioned file (e.g. libatomic.so.1.2.0), copy it to a
# writable directory and create the soname symlink (.so.1) that the linker needs.
if [ -n "$_libatomic" ] && [ "$(basename "$_libatomic")" != "libatomic.so.1" ]; then
  mkdir -p "$LIBATOMIC_CACHE"
  cp "$_libatomic" "$LIBATOMIC_CACHE/"
  ln -sf "$(basename "$_libatomic")" "$LIBATOMIC_CACHE/libatomic.so.1"
  _libatomic="$LIBATOMIC_CACHE/libatomic.so.1"
fi

# Not found anywhere on the system — install/download from the Amazon Linux 2 repo.
if [ -z "$_libatomic" ] && [ ! -f "$LIBATOMIC_CACHE/libatomic.so.1" ]; then
  echo "libatomic.so.1 not found; bootstrapping..."
  mkdir -p "$LIBATOMIC_CACHE"

  # ── Layer 1: yum install (canonical approach for AL2) ───────────────────────
  if command -v yum >/dev/null 2>&1; then
    echo "Trying yum install libatomic..."
    if yum install -y libatomic 2>/dev/null; then
      _libatomic="$(find /usr/lib64 /usr/lib -maxdepth 4 -name 'libatomic.so.1*' 2>/dev/null | head -n 1 || true)"
      [ -n "$_libatomic" ] && echo "yum install succeeded: $_libatomic"
    fi
  fi

  # ── Layer 2: CDN mirror list + metadata parsing ────────────────────────────
  if [ -z "$_libatomic" ] && [ ! -f "$LIBATOMIC_CACHE/libatomic.so.1" ]; then
    echo "yum unavailable or failed; trying CDN mirror..."
    _tmp="$(mktemp -d)"
    _mirror=""

    # Fetch a working mirror URL from the official CDN mirror list.
    _mirror="$(curl -fsSL "https://cdn.amazonlinux.com/2/core/2.0/x86_64/mirror.list" 2>/dev/null \
      | head -n 1 | tr -d '[:space:]' || true)"

    if [ -n "$_mirror" ]; then
      # Parse repodata/primary.xml.gz to find the RPM href.
      _href="$(curl -fsSL "${_mirror}repodata/primary.xml.gz" | python3 - <<'PYEOF'
import sys, gzip, xml.etree.ElementTree as ET
data = sys.stdin.buffer.read()
if not data:
    sys.exit(0)
root = ET.fromstring(gzip.decompress(data))
ns = {'r': 'http://linux.duke.edu/metadata/common'}
for p in root.findall('r:package', ns):
    n = p.find('r:name', ns)
    if n is not None and n.text == 'libatomic':
        loc = p.find('r:location', ns)
        if loc is not None:
            print(loc.get('href', ''))
        break
PYEOF
      2>/dev/null || true)"

      if [ -n "$_href" ]; then
        echo "Found RPM: ${_mirror}${_href}"
        curl -fsSL "${_mirror}${_href}" -o "$_tmp/libatomic.rpm"
        rpm2cpio "$_tmp/libatomic.rpm" | (cd "$_tmp" && cpio -id 2>/dev/null) || true
        _sofile="$(find "$_tmp" -name 'libatomic.so.1*' | head -n 1 || true)"
        if [ -n "$_sofile" ]; then
          cp "$_sofile" "$LIBATOMIC_CACHE/"
          _bn="$(basename "$_sofile")"
          [ "$_bn" != "libatomic.so.1" ] && \
            ln -sf "$_bn" "$LIBATOMIC_CACHE/libatomic.so.1" || true
          echo "CDN mirror download succeeded."
        fi
      fi
    fi
    rm -rf "$_tmp"
  fi

  # ── Layer 3: Hardcoded direct RPM URL (last resort) ────────────────────────
  if [ -z "$_libatomic" ] && [ ! -f "$LIBATOMIC_CACHE/libatomic.so.1" ]; then
    echo "Mirror metadata failed; trying direct RPM URL..."
    _tmp="$(mktemp -d)"
    _direct="https://cdn.amazonlinux.com/2/core/2.0/x86_64/mirror.list"
    # Resolve a mirror and try a known RPM path pattern.
    _base="$(curl -fsSL "$_direct" 2>/dev/null | head -n 1 | tr -d '[:space:]' || true)"
    _fallback_url="${_base:-https://cdn.amazonlinux.com/2/core/latest/x86_64/}Packages/libatomic-7.3.1-17.amzn2.x86_64.rpm"

    if curl -fsSL "$_fallback_url" -o "$_tmp/libatomic.rpm" 2>/dev/null; then
      rpm2cpio "$_tmp/libatomic.rpm" | (cd "$_tmp" && cpio -id 2>/dev/null) || true
      _sofile="$(find "$_tmp" -name 'libatomic.so.1*' | head -n 1 || true)"
      if [ -n "$_sofile" ]; then
        cp "$_sofile" "$LIBATOMIC_CACHE/"
        _bn="$(basename "$_sofile")"
        [ "$_bn" != "libatomic.so.1" ] && \
          ln -sf "$_bn" "$LIBATOMIC_CACHE/libatomic.so.1" || true
        echo "Direct RPM download succeeded."
      fi
    fi
    rm -rf "$_tmp"
  fi
fi

# Pick up whatever we found or built.
[ -z "$_libatomic" ] && [ -f "$LIBATOMIC_CACHE/libatomic.so.1" ] && \
  _libatomic="$LIBATOMIC_CACHE/libatomic.so.1"

if [ -n "$_libatomic" ]; then
  export LD_LIBRARY_PATH="$(dirname "$_libatomic")${LD_LIBRARY_PATH:+:$LD_LIBRARY_PATH}"
  echo "libatomic.so.1 at: $_libatomic"
else
  echo "WARNING: libatomic.so.1 could not be located; kotlinWasmNpmInstall may fail."
fi

# ── Gradle ───────────────────────────────────────────────────────────────────
# GRADLE_USER_HOME inside .vercel/cache/ ensures Kotlin's managed Node.js
# installation is persisted between builds.
export GRADLE_USER_HOME=".vercel/cache/gradle-home"

./gradlew \
  --no-configuration-cache \
  -Pkotlin.js.nodejs.download=true \
  -Pkotlin.js.yarn=false \
  :composeApp:composeCompatibilityBrowserDistribution
