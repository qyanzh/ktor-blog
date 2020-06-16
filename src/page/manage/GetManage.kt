package com.example.page.manage

import com.example.Article
import com.example.Articles
import com.example.UserSession
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.getManage() = get {

    call.respondHtml {
        val session = call.sessions.get<UserSession>()
        val username = session?.username ?: ""
        val params = call.parameters
        val q = params["q"]
        head {
            title { +"博文管理" }
        }
        body {
            +"博文管理"
            p {}
            script(src = "/static/func.js") {}
            form {
                +"文章搜索(标题/内容)"

                input(type = InputType.text) {
                    value = q ?: ""
                    name = "q"
                    id = "q"
                }
                input(type = InputType.button) {
                    value = "搜索"
                    onClick = "queryArticle('q')"
                }
            }

            ul {
                transaction {
                    val articles = if (q == null) {
                        Article.find { Articles.author eq username }
                    } else {
                        Article.find { (Articles.author eq username) and ((Articles.title like "%$q%") or (Articles.content like "%$q%")) }
                    }

                    for (article in articles) {
                        li {
                            a {
                                href = "/article?id=${article.id}"
                                +(article.title + " - " + article.author)
                            }
                            +Entities.nbsp
                            a {
                                href = "/manage/delete?id=${article.id}"
                                +("删除")
                            }
                        }
                    }
                }
            }
        }
    }
}

