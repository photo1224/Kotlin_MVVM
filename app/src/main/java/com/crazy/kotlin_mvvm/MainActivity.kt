package com.crazy.kotlin_mvvm

import com.crazy.kotlin_mvvm.base.BaseActivity
import com.crazy.kotlin_mvvm.base.BaseViewModel
import com.crazy.kotlin_mvvm.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {

    override fun getLayoutResId() = R.layout.activity_main

    override fun initVariableId() = BR.viewModel

    override fun initView() {
    }

    override fun initData() {
    }

}
