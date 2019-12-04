package com.crazy.kotlin_mvvm.listener

import android.view.View

/**
 * Created by wtc on 2019/11/19
 */
interface OnMyClickListener<T> {

    fun onClick(view: View, model: T? = null)

}