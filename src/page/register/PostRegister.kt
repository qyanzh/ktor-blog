package com.example.page.register

import com.example.User
import com.example.Users
import com.example.alertRedirect
import com.example.run
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.request.receiveParameters
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.coroutines.launch
import kotlinx.html.body
import kotlinx.html.script
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.postRegister() = post {
    val params = call.receiveParameters()
    val username = params["username"]!!
    val password = params["password"]!!
    transaction {
        User.find { Users.username eq username }.firstOrNull()?.let {
            launch {
                call.alertRedirect("用户名已存在","/register")
            }
        } ?: run {
            User.new {
                this.username = username
                this.password = password
            }
            launch {
                call.alertRedirect("注册成功","/")
            }
        }
    }
}