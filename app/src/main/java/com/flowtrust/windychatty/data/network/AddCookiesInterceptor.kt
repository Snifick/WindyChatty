package com.flowtrust.windychatty.data.network

import android.content.Context
import android.provider.SyncStateContract
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException

class AddCookiesInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        //add saved cookies
        val file = File(context.filesDir, "cookie.txt")
        if (!file.exists())
            file.createNewFile()

        val inputStream = file.inputStream()
        inputStream.use {
            val cookieSize = it.read()
            if (cookieSize > 0) {
                val cookie = ByteArray(cookieSize)
                it.read(cookie)
                builder.addHeader("Cookie", cookie.decodeToString())
            }
        }

        return chain.proceed(builder.build())
    }
}