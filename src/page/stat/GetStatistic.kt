package com.example.page.stat

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
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat

fun Route.getStatistic() = get {
    call.respondHtml {
        val session = call.sessions.get<UserSession>()
        val username = session?.username!!
        if (username == "root") {

            head {
                title { +"统计信息" }
                link(rel = "stylesheet", href = "/static/styles.css")
            }

            body {
                table {
                    tr {
                        th { +"标题" }
                        th { +"作者" }
                        th { +"评论数" }
                        th { +"浏览量" }
                        th { +"发布日期" }
                    }
                    transaction {
                        val articles = Article.all().orderBy(Articles.view to SortOrder.DESC)
                        val format = SimpleDateFormat("yy/MM/dd HH:mm:ss")
                        for (article in articles) {
                            tr {
                                td {
                                    +article.title
                                }
                                td {
                                    +article.author
                                }
                                td {
                                    +article.comment.toString()
                                }
                                td {
                                    +article.view.toString()
                                }
                                td {
                                    +format.format(article.time)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            head {
                title { +"拒绝访问" }
            }
            body {
                +"非管理员"
            }
        }
    }
}