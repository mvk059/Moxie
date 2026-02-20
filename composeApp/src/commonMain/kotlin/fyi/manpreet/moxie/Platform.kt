package fyi.manpreet.moxie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform