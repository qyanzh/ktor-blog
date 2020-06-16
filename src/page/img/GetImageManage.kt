package com.example.page.img

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
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.SimpleDateFormat

fun Route.getImageManage() = get {
    call.respondHtml {
        val session = call.sessions.get<UserSession>()
        val username = session?.username!!
        if (username == "root") {

            head {
                title { +"图片管理" }
                link(rel = "stylesheet", href = "/static/styles.css")
            }

            body {
                style {
                    unsafe {
                        raw("""
                                div {
                                    text-align: center;
                                    margin: 0 auto;
                                }
                            """.trimIndent()
                        )
                    }
                }
                div {
                    table {
                        transaction {
                            tr {
                                th {
                                    +"文件名"
                                }
                                th {
                                    +"图片"
                                }
                                th {
                                    +"操作"
                                }
                            }
                            val images = Image.all()
                            for (image in images) {
                                tr {
                                    td {
                                        + image.fileName
                                    }
                                    td {
                                        img {
                                            src = "img?photo=" + image.fileName
                                        }
                                    }
                                    td {
                                        a {
                                            href = "/imageManage/delete?fileName=${image.fileName}"
                                            +"删除"
                                        }
                                    }

                                }
                            }
                        }
                    }
                    p {
                        form(method = FormMethod.post,action = "imageManage/add",encType = FormEncType.multipartFormData) {
                            input {
                                type = InputType.text
                                name = "fileName"
                            }
                            input {
                                type = InputType.file
                                name = "image"
                            }
                            input {
                                type = InputType.submit
                                value = "上传"
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

