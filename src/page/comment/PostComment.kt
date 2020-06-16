package com.example.page.comment

import com.example.Article
import com.example.Articles
import com.example.Comment
import com.example.alertRedirect
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.routing.Route
import io.ktor.routing.post
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.postComment() = post {
    val params = call.receiveParameters()
    val username = when (val name = params["username"]!!) {
        "" -> "匿名用户"
        else -> name
    }
    val articleId = params["articleId"]!!.toInt()
    val content = params["content"]!!
    transaction {
        Comment.new {
            this.articleId = articleId
            this.content = content
            this.username = username
            this.time = System.currentTimeMillis()
        }
        Article.find { Articles.id eq articleId }.first().comment++

    }
    call.alertRedirect("发表评论成功", "/article?id=${articleId}")
}