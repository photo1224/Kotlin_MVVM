package com.crazy.kotlin_mvvm.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.crazy.kotlin_mvvm.R

/**
 * Created by wtc on 2019/5/11
 */
abstract class BaseQuickPageStateAdapter<T, K : BaseViewHolder> : BaseQuickAdapter<T, K> {

    private var context: Context? = null
    private lateinit var pageView: View
    private lateinit var tvTip: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var ivEmpty: ImageView

    constructor(context: Context?, data: List<T>) : super(data) {
        initPageView(context)
    }

    constructor(context: Context?, layoutResId: Int) : super(layoutResId) {
        initPageView(context)
    }

    constructor(context: Context?, layoutResId: Int, data: List<T>) : super(layoutResId, data) {
        initPageView(context)
    }

    private fun initPageView(context: Context?) {
        this.context = context
        pageView = LayoutInflater.from(context).inflate(R.layout.layout_page_state, null)
        tvTip = pageView.findViewById(R.id.tv_tip)
        progressBar = pageView.findViewById(R.id.progressbar)
        ivEmpty = pageView.findViewById(R.id.iv_empty)
    }


    fun showLoadingPage() {
        ivEmpty.visibility = View.GONE
        tvTip.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        emptyView = pageView
    }

    fun showEmptyPage() {
        ivEmpty.visibility = View.VISIBLE
        tvTip.visibility = View.VISIBLE
        tvTip.text = context?.getString(R.string.no_content)
        progressBar.visibility = View.GONE

        notifyDataSetChanged()
        emptyView = pageView
    }

    fun showErrorPage(errorMsg: String?) {
        if (!TextUtils.isEmpty(errorMsg)) tvTip.text = errorMsg
        else tvTip.setText(R.string.no_net)

        tvTip.visibility = View.VISIBLE
        ivEmpty.visibility = View.GONE
        progressBar.visibility = View.GONE

        emptyView = pageView
    }
}
