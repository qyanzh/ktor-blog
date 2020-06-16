package com.example

import com.example.page.article.getArticle
import com.example.page.comment.postComment
import com.example.page.home.getHome
import com.example.page.img.getDeleteImage
import com.example.page.img.getImageManage
import com.example.page.img.getImg
import com.example.page.img.postAddImage
import com.example.page.login.postLogin
import com.example.page.logout.getLogout
import com.example.page.manage.getDeleteArticle
import com.example.page.manage.getManage
import com.example.page.register.getRegister
import com.example.page.register.postRegister
import com.example.page.stat.getStatistic
import com.example.page.write.getWrite
import com.example.page.write.postWrite
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@kotlin.jvm.JvmOverloads
fun Application.main(testing: Boolean = false) {

    initDB()

    install(Sessions) {
        cookie<UserSession>("USER_SESSION")
    }

    install(CallLogging)

    routing {

        static("/static") {
            resources("static")
        }

        route("/") {
            getHome()
        }

        route("/register") {
            getRegister()
            postRegister()
        }

        route("/login") {
            postLogin()
        }

        route("/logout") {
            getLogout()
        }

        route("/write") {
            getWrite()
            postWrite()
        }

        route("/article") {
            getArticle()
        }

        route("/comment") {
            postComment()
        }

        route("/manage") {
            getManage()
            getDeleteArticle("/delete")

        }

        route("/stat") {
            getStatistic()
        }

        route("/imageManage") {
            getImageManage()
            postAddImage("/add")
            getDeleteImage("/delete")
        }

        route("/img") {
            getImg("")
        }
    }


}


data class UserSession(val username: String, val userId: Int)

