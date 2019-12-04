package com.crazy.kotlin_mvvm.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crazy.kotlin_mvvm.R
import com.crazy.kotlin_mvvm.databinding.ActivityBaseBinding
import com.crazy.kotlin_mvvm.databinding.LayoutToolbarBinding
import com.crazy.kotlin_mvvm.ext.loge
import com.crazy.kotlin_mvvm.utils.StatusBarUtils
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * Created by wtc on 2019/11/2
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    private val baseBinding by lazy {
        DataBindingUtil.setContentView<ActivityBaseBinding>(
            this,
            R.layout.activity_base
        )
    }
    protected val toolbarBinding: LayoutToolbarBinding? by lazy {
        if (useToolBar()) {
            DataBindingUtil.inflate<LayoutToolbarBinding>(
                LayoutInflater.from(this),
                R.layout.layout_toolbar,
                baseBinding.container,
                true
            )
        } else null
    }
    protected val binding: V by lazy {
        DataBindingUtil.inflate<V>(
            LayoutInflater.from(this), getLayoutResId(), baseBinding.container,
            true
        )
    }

    protected lateinit var viewModel: VM

    var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtils.setColor(this, resources.getColor(R.color.cl_051D3F))

        initToolBar()

        binding.lifecycleOwner = this

        initViewModel()

        //dataBing和viewModel绑定
        binding.setVariable(initVariableId(), viewModel)

        if (useEventBus()) EventBus.getDefault().register(this)

        startObserver()

        //初始化View
        initView()
        //初始化数据
        initData()
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
            if (it.isLoading) loadingDialog = ProgressDialog.show(this, "提示", it.message)
            else loadingDialog?.dismiss()
        })
    }

    open fun initToolBar() {
        if (useToolBar()) {
            setSupportActionBar(toolbarBinding?.toolbar)
            toolbarBinding?.toolbar?.setNavigationOnClickListener { finish() }
        }
    }


    /**
     * 默认不使用 EventBus
     */
    open fun useEventBus() = false

    open fun useToolBar() = true

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