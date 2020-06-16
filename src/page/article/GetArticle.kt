package com.example.page.article

import com.example.*
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat

fun Route.getArticle() = get {
    val param = call.parameters
    val id = param["id"]!!.toInt()
    val blog = transaction {
        Article.find { Articles.id eq id }.first().apply {
            view++
        }
    }


    call.respondHtml {
        head {
            title { +"博文" }
            +("标题：${blog.title}")
            br
            +("作者：${blog.author}")
            br
            +("时间：${SimpleDateFormat("yy/MM/dd HH:mm").format(blog.time)}")
        }
        body {
            p {
                +blog.content
            }
            p {
                +"评论列表"
                ul {
                    transaction {
                        val comments = Comment.find { Comments.articleId eq id }.orderBy(Comments.time to SortOrder.ASC)
                        val format = SimpleDateFormat("yy/MM/dd HH:mm:ss")
                        for (comment in comments) {
                            li {
                                p {
                                    +(comment.content + " - " + comment.username + " - " + format.format(comment.time))
                                }
                            }
                        }
                    }
                }
            }
            p {
                form(action = "/comment", method = FormMethod.post) {
                    textArea {
                        this.id = "content"
                        name = "content"
                        rows = "10"
                        cols = "100"
                    }
                    input(type = InputType.hidden) {
                        name = "username"
                        value = call.sessions.get<UserSession>()?.username ?: ""
                    }
                    input(type = InputType.hidden) {
                        name = "articleId"
                        value = id.toString()
                    }
                    br
                    input(type = InputType.submit) {
                        value = "发表评论"
                    }
                }
            }

        }
    }
}


