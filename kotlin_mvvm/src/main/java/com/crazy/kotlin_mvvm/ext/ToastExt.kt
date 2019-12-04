package com.crazy.kotlin_mvvm.ext

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import com.crazy.kotlin_mvvm.BaseApplication

/**
 * Created by luyao
 * on 2019/5/31 16:42
 */


fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, content, duration).apply {
        setGravity(Gravity.CENTER,0,0)
        show()
    }
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}

fun Context.longToast(content: String) {
    toast(content, Toast.LENGTH_LONG)
}

fun Context.longToast(@StringRes id: Int) {
    toast(id, Toast.LENGTH_LONG)
}

fun Any.toast(context: Context, content: String, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(content, duration)
}

fun Any.toast(context: Context, @StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(id, duration)
}

fun Any.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    BaseApplication.context.toast(content, duration)
}

fun Any.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    BaseApplication.context.toast(id, duration)
}

fun Any.longToast(context: Context, content: String) {
    context.longToast(content)
}

fun Any.longToast(content: String) {
    BaseApplication.context.longToast(content)
}

fun Any.longToast(context: Context, @StringRes id: Int) {
    context.longToast(id)
}

fun Any.longToast(@StringRes id: Int) {
    BaseApplication.context.longToast(id)
}


