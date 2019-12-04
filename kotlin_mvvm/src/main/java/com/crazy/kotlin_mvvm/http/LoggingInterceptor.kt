package com.crazy.kotlin_mvvm.http

import android.util.Log
import com.crazy.kotlin_mvvm.BuildConfig
import com.crazy.kotlin_mvvm.ext.formatJson
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 * Created by wtc on 2019/11/1
 */
class LoggingInterceptor(var isLogEnable:Boolean = BuildConfig.DEBUG) :Interceptor{

    companion object{
        private const val TAG = "API_LOG"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (!isLogEnable) {
            return chain.proceed(request)
        }

        val charset = Charset.forName("UTF-8")
        val buffer = Buffer()
        val requestBody = request.body()

        val response = chain.proceed(request)

        val responseBody = response.body()
        val source = responseBody?.source()

        val headers = request.headers()
        val responseHeadersLength = headers.size()

        Log.d(
            TAG,
            "╔════════════════════════════════════════════════════════════════════════════════════════"
        )
        Log.d(TAG, String.format("║ 请求地址 %s", request.url()))
        Log.d(TAG, String.format("║ 请求方式 %s", request.method()))
        Log.d(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        for (i in 0 until responseHeadersLength) {
            val headerName = headers.name(i)
            val headerValue = headers.get(headerName)
            Log.d(TAG, String.format("║ 请求头: %s : %s", headerName, headerValue))
        }

        Log.d(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        if (requestBody != null) {
            requestBody.writeTo(buffer)
            Log.d(TAG, String.format("║ 请求参数 %s", buffer.readString(charset)))
            Log.d(
                TAG,
                "╟────────────────────────────────────────────────────────────────────────────────────────"
            )
        }

        if (source == null) {
            Log.d(TAG, String.format("║ 访问错误码 %s", response.code()))
        } else {
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val bufferS = source.buffer()
            val contentType = responseBody.contentType()
            if (contentType != null) {
                val json = bufferS.clone().readString(contentType.charset(charset)!!)
                Log.d(TAG, "║ 返回数据")
                val con = json.formatJson().split("\n")
                for (line in con) {
                    Log.d(TAG, "║$line")
                }
            }
        }
        Log.d(
            TAG,
            "╚════════════════════════════════════════════════════════════════════════════════════════"
        )
        return response
    }

}