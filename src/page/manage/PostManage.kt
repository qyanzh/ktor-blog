package com.example.page.manage

import com.example.Article
import com.example.Articles
import com.example.UserSession
import com.example.alertRedirect
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.getDeleteArticle(path: String) = get(path) {
    val params = call.parameters
    val articleId = params["id"]!!.toInt()
    val session = call.sessions.get<UserSession>()
    session?.let {
        transaction {
            val article = Article.find { Articles.id eq articleId }.first()
            if (it.username == article.author) {
                article.delete()
                launch {
                    call.alertRedirect("删除成功", "/manage")
                }
            }
        }
    }
}