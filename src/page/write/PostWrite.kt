package com.example.page.write

import com.example.Article
import com.example.UserSession
import com.example.alertRedirect
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.postWrite() = post {
    val params = call.receiveParameters()
    val author = call.sessions.get<UserSession>()!!.username
    val title = params["title"]!!
    val content = params["content"]!!
    val time = System.currentTimeMillis()
    val id = transaction {
        Article.new {
            this.author = author
            this.time = time
            this.content = content
            this.title = title
            this.view = 0
            this.comment = 0
        }.id
    }
    call.alertRedirect("发表成功", "/article?id=${id}")
}