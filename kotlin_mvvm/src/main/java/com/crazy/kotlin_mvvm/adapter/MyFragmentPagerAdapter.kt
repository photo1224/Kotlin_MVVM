package com.crazy.kotlin_mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * Created by wtc on 2019/5/8
 */
class MyFragmentPagerAdapter(fm: FragmentManager, private val mFragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private lateinit var title:List<String>

    constructor(
        fm: FragmentManager,
        mFragmentList: List<Fragment>,
        title: List<String>
    ) : this(fm, mFragmentList){
        this.title = title
    }

    override fun getItem(position: Int) = mFragmentList[position]

    override fun getCount() = mFragmentList.size

    override fun getPageTitle(position: Int) = title[position]
}
