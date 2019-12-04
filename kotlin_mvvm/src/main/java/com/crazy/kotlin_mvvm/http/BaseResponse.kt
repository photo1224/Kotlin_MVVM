package com.crazy.kotlin_mvvm.http

/**
 * Created by wtc on 2019/11/1
 */
data class BaseResponse<out T>(val code: Int, val message: String, val data: T)