package com.crazy.kotlin_mvvm.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding

/**
 * Created by wtc on 2019/11/15
 *
 * 懒加载 结合 FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)使用
 */
abstract class LazyBaseFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseFragment<V, VM>() {

    private var mIsFirstVisible = true

    private var isViewCreated = false

    private var currentVisibleState = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isViewCreated = true
    }

    override fun onResume() {
        super.onResume()
        currentVisibleState = true
        mIsFirstVisible = isViewCreated
        if (isViewCreated) {
            onFragmentFirstVisible()
            isViewCreated = false
        }
        onFragmentResume(mIsFirstVisible)
    }

    override fun onPause() {
        super.onPause()
        if (currentVisibleState) {
            currentVisibleState = false
            onFragmentPause()
        }
    }

    override fun onStop() {
        super.onStop()
        if (currentVisibleState) {
            currentVisibleState = false
            onFragmentPause()
        }
    }


    /**
     * 第一次可见
     */
    open fun onFragmentFirstVisible() {}

    /**
     * 可见
     * @param isFirstResume 是否是第一次可见
     */
    open fun onFragmentResume(isFirstResume: Boolean) {}

    /**
     * 不可见
     */
    open fun onFragmentPause() {}


    override fun onDestroyView() {
        super.onDestroyView()

        mIsFirstVisible = true
        isViewCreated = false
        currentVisibleState = false
    }
}