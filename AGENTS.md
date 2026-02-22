# AGENTS.md

This repository hosts a Compose Multiplatform (CMP) app with web targets used for iframe embedding into a separate Next.js site.

## Purpose

- Serve CMP web at `https://cmp.manpreet.fyi`
- Show a project index at `/`
- Serve embeddable project pages at `/projects/:slug`
- Support iframe mode via `?embed=1`

## Current Architecture

- Web entrypoint: `composeApp/src/webMain/kotlin/fyi/manpreet/moxie/main.kt`
- Root portal app: `composeApp/src/commonMain/kotlin/fyi/manpreet/moxie/CmpPortalApp.kt`
- Route parser/model: `composeApp/src/commonMain/kotlin/fyi/manpreet/moxie/routing/CmpRoute.kt`
- Web route adapter: `composeApp/src/webMain/kotlin/fyi/manpreet/moxie/routing/WebRouteAdapter.kt`
- Project registry: `composeApp/src/commonMain/kotlin/fyi/manpreet/moxie/projects/ProjectRegistry.kt`
- Iframe resize bridge: `composeApp/src/webMain/kotlin/fyi/manpreet/moxie/embed/IframeBridge.kt`

## Project Registration Pattern

To add a new CMP web project:

1. Create a new composable under:
   `composeApp/src/commonMain/kotlin/fyi/manpreet/moxie/projects/samples/`
2. Register it in:
   `composeApp/src/commonMain/kotlin/fyi/manpreet/moxie/projects/ProjectRegistry.kt`
3. Ensure slug is URL-safe and unique.

Route generated:

- `/projects/<slug>`
- Embeddable URL: `/projects/<slug>?embed=1`

## Iframe Contract

When `embed=1` is present, the app posts resize messages to parent:

```json
{"type":"moxie-cmp:resize","slug":"<slug>","height":1234}
```

Allowed target origins are defined in:

- `composeApp/src/webMain/kotlin/fyi/manpreet/moxie/embed/IframeBridge.kt`

## Deployment (Vercel)

Vercel config is in:

- `vercel.json`

It builds and serves:

- Build command: `./gradlew :composeApp:composeCompatibilityBrowserDistribution`
- Output dir: `composeApp/build/dist/composeWebCompatibility/productionExecutable`

Compatibility distribution gives:

- WASM for modern browsers
- JS fallback for unsupported browsers

## Local Commands

- Dev wasm: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Dev js: `./gradlew :composeApp:jsBrowserDevelopmentRun`
- Prod compat build: `./gradlew :composeApp:composeCompatibilityBrowserDistribution`

## Guardrails

- Keep web routing logic centralized in `CmpRoute.kt`.
- Do not hardcode project pages outside `ProjectRegistry`.
- Keep iframe-specific behavior behind `embed=1`.
- Validate web build before merging:
  `./gradlew :composeApp:composeCompatibilityBrowserDistribution`

