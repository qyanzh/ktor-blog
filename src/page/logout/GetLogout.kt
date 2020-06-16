package com.example.page.logout

import com.example.UserSession
import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.clear
import io.ktor.sessions.sessions

fun Route.getLogout() = get {
    call.sessions.clear<UserSession>()
    call.respondRedirect("/")
}