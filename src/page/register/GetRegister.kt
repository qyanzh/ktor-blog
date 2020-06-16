package com.example.page.register

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.getRegister() = get {

    call.respondHtml {
        head {
            title { +"注册" }
        }

        body {
            script(src = "/static/func.js") {}

            form(action = "/register", method = FormMethod.post) {
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
                    value = "注册"
                }

            }

        }
    }

}
