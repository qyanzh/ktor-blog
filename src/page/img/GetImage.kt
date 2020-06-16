package com.example.page.img

import io.ktor.application.call
import io.ktor.response.respondFile
import io.ktor.routing.Route
import io.ktor.routing.get
import java.io.File

fun Route.getImg(path: String) = get(path) {
    val photo = call.parameters["photo"]!!
    val file = File("D:\\images", photo)
    if (file.exists()) {
        call.respondFile(file)
    }
}