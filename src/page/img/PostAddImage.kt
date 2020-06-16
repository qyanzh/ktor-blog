package com.example.page.img

import com.example.Image
import com.example.alertRedirect
import com.example.copyToSuspend
import io.ktor.application.call
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.routing.Route
import io.ktor.routing.post
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun Route.postAddImage(path: String) = post(path) {
    val multipart = call.receiveMultipart().readAllParts()
    val fileName = (multipart.filter { it.name == "fileName" }[0] as PartData.FormItem).value
    val filePart = (multipart.filter { it.name == "image" }[0] as PartData.FileItem)
    val ext = File(filePart.originalFileName).extension
    val filename = if (fileName.isEmpty()) {
        filePart.originalFileName
    } else {
        "${fileName}.$ext"
    }
//    val filename = filePart.originalFileName
    val file = File("D:\\images", filename)
    filePart.streamProvider().use { input ->
        file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
    }
    transaction {
        Image.new {
            this.fileName = filename!!
            this.time = System.currentTimeMillis()
        }
    }
    call.alertRedirect("添加成功", "/imageManage")
}