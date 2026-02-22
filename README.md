Compose Multiplatform Apps built by me

### Build Web Compatibility Bundle (Wasm + JS Fallback)

Use this for deployment so modern browsers get Wasm and unsupported browsers fall back to JS:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:composeCompatibilityBrowserDistribution
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:composeCompatibilityBrowserDistribution
  ```

Output directory:

```text
composeApp/build/dist/composeWebCompatibility/productionExecutable
```

### CMP Routes for Embedding

- `https://cmp.manpreet.fyi/` - project index
- `https://cmp.manpreet.fyi/projects/:slug` - standalone project page
- `https://cmp.manpreet.fyi/projects/:slug?embed=1` - iframe mode

Iframe mode posts resize updates to parent:

```text
{"type":"moxie-cmp:resize","slug":"<slug>","height":<number>}
```
