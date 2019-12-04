package com.crazy.kotlin_mvvm.utils

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.crazy.kotlin_mvvm.adapter.BaseQuickPageStateAdapter
import com.crazy.kotlin_mvvm.ext.dp2px
import com.crazy.kotlin_mvvm.ext.loge
import com.crazy.kotlin_mvvm.glide.CenterCropRoundCornerTransform

/**
 * Created by wtc on 2019/11/16
 */

@BindingAdapter(
    "imageUrl",
    "imageDrawable",
    "placeholder",
    "error",
    "roundedCorners",
    "circleCrop",
    "crossFade",
    "overrideImageWidth",
    "overrideImageHeight",
    requireAll = false
)
fun bindImage(
    imageView: ImageView,
    imageUrl: String? = null,
    imageDrawable: Drawable? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    roundedCorners: Int? = null,
    circleCrop: Boolean? = false,
    crossFade: Boolean? = false,
    overrideWidth: Int? = null,
    overrideHeight: Int? = null
) {
    if (TextUtils.isEmpty(imageUrl) && imageDrawable == null) return

    var builder = Glide.with(imageView.context)
        .load(imageUrl ?: imageDrawable)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .centerCrop()

    if (placeholder != null) {
        builder = builder.placeholder(placeholder)
    }

    if (error != null) {
        builder = builder.error(error)
    }

    val options = RequestOptions()
    if (roundedCorners != null) {
        val transform =
            options.transform(CenterCropRoundCornerTransform(imageView.context.dp2px(roundedCorners)))
    }

    if (circleCrop == true) {
        builder = builder.circleCrop()
    }

    if (crossFade == true) {
        builder = builder.transition(DrawableTransitionOptions.withCrossFade())
    }

    if (overrideWidth != null && overrideHeight != null) {
        builder = builder.run { override(overrideWidth, overrideHeight) }
    }

    builder
        .apply(options)
        .into(imageView)
}

@BindingAdapter("isRefreshing")
fun refreshData(refreshLayout: SwipeRefreshLayout, isRefreshing: Boolean) {
    refreshLayout.isRefreshing = isRefreshing
}

@BindingAdapter("data")
fun <T> notifyDataSetChanged(rv: RecyclerView, newData: List<T>?) {
    val quickAdapter = rv.adapter as? BaseQuickAdapter<T, *>
    quickAdapter?.setNewData(newData)
}


@BindingAdapter(
    "errorMessage",
    "page",
    "newData",
    requireAll = false
)
fun <T> notifyDataSetChangedWithPageState(
    rv: RecyclerView,
    errorMessage: String? = null,
    page: Int = -1,
    newData: List<T>?
) {
    val adapter = rv.adapter as? BaseQuickPageStateAdapter<T, *>

    when {
        page == 1 -> {
            if (!TextUtils.isEmpty(errorMessage)) {
                adapter?.showErrorPage(errorMessage)
                return
            }
            if (newData == null || newData.isEmpty()) adapter?.showEmptyPage()
            else adapter?.setNewData(newData)
        }

        page > 1 -> when {
            newData == null -> adapter?.loadMoreFail()
            newData.isEmpty() -> adapter?.loadMoreEnd(false)
            else -> {
                adapter?.addData(newData)
                adapter?.loadMoreComplete()
            }
        }
    }
}
