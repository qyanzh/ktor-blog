package com.example.page.home

import com.example.Article
import com.example.Articles
import com.example.Image
import com.example.UserSession
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat
import java.util.*

fun Route.getHome() = get {

    call.respondHtml {
        val session = call.sessions.get<UserSession>()

        val params = call.parameters
        val selectedDate = params["date"]?.split("-")?.map { it.toInt() }

        val startTime = selectedDate?.let {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedDate[0])
                set(Calendar.MONTH, selectedDate[1] - 1)
                set(Calendar.DAY_OF_MONTH, selectedDate[2])
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
        val endTime = startTime?.let {
            Calendar.getInstance().apply {
                time = startTime.time
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        startTime?.let {
            exposedLogger.debug(startTime.time.toString())
        }

        head {
            title { +"博客" }
            link(rel = "stylesheet", href = "/static/styles.css")
        }

        body {
            script(src = "/static/func.js") {}

            div {
                id = "header"
                if (session != null) {
                    h4 { +"欢迎, ${session.username} " }
                    a("/write") { +"写文章" }
                    +Entities.nbsp
                    a("/manage") { +"博文管理" }
                    +Entities.nbsp
                    if (session.username == "root") {
                        a("/stat") { +"信息统计" }
                        +Entities.nbsp
                        a("/imageManage") { +"图片管理" }
                        +Entities.nbsp
                    }
                    a("/logout") { +"注销" }
                    +Entities.nbsp
                } else {
                    form(action = "/login", method = FormMethod.post) {
                        text("用户名")
                        input {
                            type = InputType.text
                            name = "username"
                        }

                        text("密码")

                        input {
                            type = InputType.password
                            name = "password"
                        }

                        input {
                            type = InputType.submit
                            value = "登录"
                        }

                        input {
                            type = InputType.button
                            value = "注册"
                            onClick = "redirect('/register')"
                        }
                    }

                }
            }


            div {
                id = "nav"
                val images = transaction {
                    Image.all().map { it.fileName }
                }
                images.forEach {
                    img {
                        alt = "图"
                        src =
                            "/img?photo=$it"

                    }
                }

            }

            div {
                id = "section"
                h4 {
                    +"博文列表"
                }
                ul {
                    val format = SimpleDateFormat("yy/MM/dd HH:mm:ss")
                    transaction {
                        val articles = if (startTime == null || endTime == null) {
                            Article.all().orderBy(Articles.time to SortOrder.DESC)
                        } else {
                            exposedLogger.debug(format.format(startTime.timeInMillis))
                            exposedLogger.debug(format.format(endTime.timeInMillis))
                            Article.find {
                                (Articles.time greater startTime.timeInMillis) and
                                        (Articles.time less endTime.timeInMillis)
                            }.orderBy(Articles.time to SortOrder.DESC)
                        }
                        for (article in articles) {
                            li {
                                a {
                                    href = "/article?id=${article.id}"
                                    +("${article.title} - ${article.author} - ${format.format(article.time)}")
                                }
                            }
                        }
                    }
                }
            }

            div {
                id = "right"
                input(type = InputType.date, name = "date_picker") {
                    id = "date_picker"
                    value = SimpleDateFormat("yyyy-MM-dd").format(startTime?.time ?: Date())
                }
                p {
                    input(type = InputType.button) {
                        value = "查看这天"
                        onClick = "selectDate()"
                    }
                    input(type = InputType.button) {
                        value = "查看全部"
                        onClick = "redirect('/')"
                    }
                }
            }

            div {
                id = "footer"
                +"Copyright 031702420"
            }

        }
    }

}
