package com.example.page.write

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.getWrite() = get {
    call.respondHtml {
        head {
            title { +"写博客" }
        }

        body {
            script(src = "/static/func.js") { }
            form(action = "/write", method = FormMethod.post) {
                onSubmit = "return checkEmpty('title','content')"

                +"标题"
                br
                textArea {
                    id = "title"
                    name = "title"
                    rows = "1"
                    cols = "100"
                }
                br
                +"内容"
                br
                textArea {
                    id = "content"
                    name = "content"
                    rows = "40"
                    cols = "100"
                }
                br

                submitInput {
                    value = "发表"
                }
            }
        }
    }
}