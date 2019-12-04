package com.crazy.kotlin_mvvm.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crazy.kotlin_mvvm.BR
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * Created by wtc on 2019/11/15
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    lateinit var binding: V
    lateinit var viewModel: VM

    var loadingDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        //dataBinding跟当前页面的生命周期绑定
        binding.lifecycleOwner = this

        initViewModel()

        //dataBing和viewModel绑定
        binding.setVariable(initVariableId(), viewModel)

        if (useEventBus()) EventBus.getDefault().register(this)

        startObserver()

        initView()

        initData()

        return binding.root
    }


    /**
     * 根据泛型自动创建ViewModel对象
     */
    open fun initViewModel() {
        val type = this::class.java.genericSuperclass
        val viewModelClass: Class<VM>
        if (type is ParameterizedType) {
            viewModelClass = type.actualTypeArguments[1] as Class<VM>
            viewModel = ViewModelProviders.of(this).get(viewModelClass)

            //ViewModel感知页面生命周期变化
            lifecycle.addObserver(viewModel)
        }
    }


    private fun startObserver() {
        viewModel.isLoading.observe(this, Observer {
            if (it.isLoading) loadingDialog = ProgressDialog.show(this.activity, "提示", it.message)
            else loadingDialog?.dismiss()
        })
    }


    /**
     * 默认不使用 EventBus
     */
    open fun useEventBus() = false

    abstract fun getLayoutResId(): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    abstract fun initView()

    abstract fun initData()


    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) EventBus.getDefault().unregister(this)
    }
}