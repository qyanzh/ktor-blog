package com.example

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.html.FlowOrPhrasingOrMetaDataContent
import kotlinx.html.body
import kotlinx.html.script
import kotlinx.html.unsafe
import java.io.InputStream
import java.io.OutputStream

fun FlowOrPhrasingOrMetaDataContent.run(js: String) = script {
    unsafe {
        raw(js)
    }
}

suspend fun ApplicationCall.alertRedirect(msg: String, path: String) = respondHtml {
    body {
        script(src = "/static/func.js") {}
        run("alertRedirect('$msg','$path')")
    }
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}