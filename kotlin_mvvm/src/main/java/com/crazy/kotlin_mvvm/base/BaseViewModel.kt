package com.crazy.kotlin_mvvm.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazy.kotlin_mvvm.entity.event.LoadingDialogEvent
import com.crazy.kotlin_mvvm.entity.event.LogoutEvent
import com.crazy.kotlin_mvvm.ext.loge
import com.crazy.kotlin_mvvm.ext.toast
import com.crazy.kotlin_mvvm.http.BaseResponse
import com.google.gson.Gson
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Created by wtc on 2019/11/2
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    val isLoading by lazy { MutableLiveData<LoadingDialogEvent>() }

    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        errBlock: (suspend CoroutineScope.(Throwable) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                defaultErrorBlock(e)
                errBlock?.invoke(this, e)
            } finally {
            }
        }

    }

    suspend fun <T : Any> apiCall(
        call: suspend () -> BaseResponse<T>?,
        httpFailedBlock: (suspend (code: Int, message: String) -> Unit)? = null,
        httpErrorBlock: (suspend (Throwable) -> Unit)? = null
    ): BaseResponse<T>? {
        return try {
            call.invoke()
        } catch (e: Throwable) {
            defaultErrorBlock(e,httpFailedBlock)
            httpErrorBlock?.invoke(e)
            null
        } finally {
            isLoading.value = LoadingDialogEvent(false)
        }
    }

    /**
     * 统一处理接口返回数据
     */
    suspend fun <T> handleHttpResponse(
        baseResponse: BaseResponse<T>,
        httpSuccessBlock: suspend CoroutineScope.() -> Unit,
        httpFailedBlock: (suspend CoroutineScope.(code: Int, message: String) -> Unit)? = null
    ) {
        coroutineScope {
            if (baseResponse.code == 200) httpSuccessBlock()
            else httpFailedBlock?.invoke(this,baseResponse.code, baseResponse.message)
        }
    }


    /**
     * 默认对Error的处理
     */
    open suspend fun defaultErrorBlock(e: Throwable, httpFailedBlock: (suspend (code: Int, message: String) -> Unit)? = null) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                val response = Gson().fromJson(
                    e.response()?.errorBody()?.string(), BaseResponse::class.java
                )
                val errMsg = "${response.message} [${response.code}] "
                errMsg.loge()
                toast(errMsg)
                httpFailedBlock?.invoke(response.code,response.message)
                //token失效
                if (response.code == 401) EventBus.getDefault().post(LogoutEvent())
            }

            is ConnectException,
            is SocketException,
            is UnknownHostException,
            is InterruptedIOException -> toast("网络连接异常")

            is JsonParseException,
            is JSONException,
            is ParseException -> toast("数据解析错误")

            else -> {
                e.printStackTrace()
                toast("程序出错啦~")
            }
        }
    }

}