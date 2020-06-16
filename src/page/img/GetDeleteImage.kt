package com.example.page.img

import com.example.Images
import com.example.UserSession
import com.example.alertRedirect
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.getDeleteImage(path: String) = get(path) {
    val params = call.parameters
    val fileName = params["fileName"]!!
    val session = call.sessions.get<UserSession>()
    session?.let {
        transaction {
            Images.deleteWhere { Images.fileName eq fileName }
        }
        call.alertRedirect("删除成功", "/imageManage")
    }
}