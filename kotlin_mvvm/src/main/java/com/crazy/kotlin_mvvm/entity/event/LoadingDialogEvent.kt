package com.crazy.kotlin_mvvm.entity.event

/**
 * Created by wtc on 2019/11/15
 */
data class LoadingDialogEvent(var isLoading: Boolean, var message: String = "正在加载中...")