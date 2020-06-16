package com.example.page.login

import com.example.*
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kotlinx.html.body
import kotlinx.html.script
import kotlinx.html.unsafe
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.postLogin() = post {
    val params = call.receiveParameters()
    val username = params["username"]!!
    val password = params["password"]!!
    val user = transaction {
        User.find { Users.username eq username }.firstOrNull()
    }
    user?.let {
        if (it.password == password) {
            call.sessions.set(UserSession(it.username, it.id.value))
            call.respondRedirect("/")
        } else {
            call.alertRedirect("密码错误","/")

        }
    } ?: run {
        call.alertRedirect("账号不存在","/")
    }
}