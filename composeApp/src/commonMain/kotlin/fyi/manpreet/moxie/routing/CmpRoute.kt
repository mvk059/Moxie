package fyi.manpreet.moxie.routing

data class CmpRoute(
    val screen: Screen,
    val embedMode: Boolean,
) {
    sealed interface Screen {
        data object Index : Screen
        data class Project(val slug: String) : Screen
        data class NotFound(val path: String) : Screen
    }
}

fun parseRoute(pathname: String, search: String): CmpRoute {
    val embedMode = parseEmbedMode(search)
    val path = sanitizePath(pathname)
    val parts = path.split('/').filter { it.isNotBlank() }

    val screen = when {
        parts.isEmpty() -> CmpRoute.Screen.Index
        parts.size == 2 && parts[0] == "projects" -> CmpRoute.Screen.Project(parts[1])
        else -> CmpRoute.Screen.NotFound(path)
    }

    return CmpRoute(screen = screen, embedMode = embedMode)
}

fun projectPath(slug: String): String = "/projects/$slug"

private fun parseEmbedMode(search: String): Boolean {
    val query = search.removePrefix("?")
    if (query.isBlank()) return false

    return query
        .split('&')
        .map { it.split('=', limit = 2) }
        .any { parts ->
            val key = parts.getOrNull(0)?.lowercase()
            val value = parts.getOrNull(1)?.lowercase() ?: ""
            key == "embed" && (value == "1" || value == "true" || value.isBlank())
        }
}

private fun sanitizePath(pathname: String): String {
    val path = pathname.trim()
    if (path.isBlank()) return "/"
    return if (path.startsWith('/')) path else "/$path"
}

