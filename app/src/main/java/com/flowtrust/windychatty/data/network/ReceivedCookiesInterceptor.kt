package com.flowtrust.windychatty.data.network

import android.content.Context
import android.provider.SyncStateContract
import okhttp3.Interceptor
import okhttp3.Response
import java.io.File
import java.io.IOException

class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {

            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) { cookies.add(header) }

            val file = File(context.filesDir, "cookie.txt")
            if (!file.exists())
                file.createNewFile()
            file.outputStream().use { ots ->
                cookies.forEach { cookie ->
                    val text = cookie.encodeToByteArray()
                    ots.write(text.size)
                    ots.write(text)
                }
            }
        }
        return originalResponse
    }
}