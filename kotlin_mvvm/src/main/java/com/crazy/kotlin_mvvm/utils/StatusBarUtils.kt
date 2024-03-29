package com.crazy.kotlin_mvvm.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout


object StatusBarUtils {

    private const val DEFAULT_STATUS_BAR_ALPHA = 112

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window
                .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window
                .decorView as ViewGroup
            val count = decorView.childCount
            if (count > 0 && decorView.getChildAt(count - 1) is View) {
                decorView.getChildAt(count - 1).setBackgroundColor(color)
            } else {
                val statusView = createStatusBarView(activity, color)
                decorView.addView(statusView)
            }
            setRootView(activity)
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    fun setColor(activity: Activity, color: Int, statusBarAlpha: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window
                .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = calculateStatusColor(
                color,
                statusBarAlpha
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window
                .decorView as ViewGroup
            val count = decorView.childCount
            if (count > 0 && decorView.getChildAt(count - 1) is View) {
                decorView.getChildAt(count - 1)
                    .setBackgroundColor(
                        calculateStatusColor(
                            color,
                            statusBarAlpha
                        )
                    )
            } else {
                val statusView = createStatusBarView(
                    activity,
                    color,
                    statusBarAlpha
                )
                decorView.addView(statusView)
            }
            setRootView(activity)
        }
    }

    /**
     * 设置状态栏纯色 不加半透明效果
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setColorNoTranslucent(activity: Activity, color: Int) {

        setColor(
            activity,
            color,
            0
        )
    }

    /**
     * 设置状态栏颜色(5.0以下无半透明效果,不建议使用)
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    @Deprecated("")
    fun setColorDiff(activity: Activity, color: Int) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        activity.window
            .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 生成一个状态栏大小的矩形
        val decorView = activity.window
            .decorView as ViewGroup
        val count = decorView.childCount
        if (count > 0 && decorView.getChildAt(count - 1) is View) {
            decorView.getChildAt(count - 1)
                .setBackgroundColor(color)
        } else {
            val statusView = createStatusBarView(
                activity,
                color
            )
            decorView.addView(statusView)
        }
        setRootView(activity)
    }

    /**
     * 使状态栏半透明
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     */
    @JvmOverloads
    fun setTranslucent(activity: Activity, statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        setTransparent(activity)
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 针对根布局是 CoordinatorLayout, 使状态栏半透明
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     */
    fun setTranslucentForCoordinatorLayout(activity: Activity, statusBarAlpha: Int) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar(activity)
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    fun setTransparent(activity: Activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar(activity)
        setRootView(activity)
    }

    /**
     * 使状态栏透明(5.0以上半透明效果,不建议使用)
     *
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    @Deprecated("")
    fun setTranslucentDiff(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setRootView(activity)
        }
    }

    /**
     * 为DrawerLayout 布局设置状态栏颜色,纯色
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    fun setColorNoTranslucentForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout,
        color: Int
    ) {

        setColorForDrawerLayout(
            activity,
            drawerLayout,
            color,
            0
        )
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param activity       需要设置的activity
     * @param drawerLayout   DrawerLayout
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    @JvmOverloads
    fun setColorForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout,
        color: Int,
        statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window
                .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        if (contentLayout.childCount > 0 && contentLayout.getChildAt(0) is View) {
            contentLayout.getChildAt(0)
                .setBackgroundColor(
                    calculateStatusColor(
                        color,
                        statusBarAlpha
                    )
                )
        } else {
            val statusBarView = createStatusBarView(
                activity,
                color
            )
            contentLayout.addView(
                statusBarView,
                0
            )
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                .setPadding(
                    0,
                    getStatusBarHeight(activity),
                    0,
                    0
                )
        }
        // 设置属性
        val drawer = drawerLayout.getChildAt(1) as ViewGroup
        drawerLayout.setFitsSystemWindows(false)
        contentLayout.fitsSystemWindows = false
        contentLayout.clipToPadding = true
        drawer.fitsSystemWindows = false

        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色(5.0以下无半透明效果,不建议使用)
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    @Deprecated("")
    fun setColorForDrawerLayoutDiff(activity: Activity, drawerLayout: DrawerLayout, color: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 生成一个状态栏大小的矩形
            val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
            if (contentLayout.childCount > 0 && contentLayout.getChildAt(0) is View) {
                contentLayout.getChildAt(0)
                    .setBackgroundColor(
                        calculateStatusColor(
                            color,
                            DEFAULT_STATUS_BAR_ALPHA
                        )
                    )
            } else {
                // 添加 statusBarView 到布局中
                val statusBarView = createStatusBarView(
                    activity,
                    color
                )
                contentLayout.addView(
                    statusBarView,
                    0
                )
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1)
                    .setPadding(
                        0,
                        getStatusBarHeight(activity),
                        0,
                        0
                    )
            }
            // 设置属性
            val drawer = drawerLayout.getChildAt(1) as ViewGroup
            drawerLayout.setFitsSystemWindows(false)
            contentLayout.fitsSystemWindows = false
            contentLayout.clipToPadding = true
            drawer.fitsSystemWindows = false
        }
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    @JvmOverloads
    fun setTranslucentForDrawerLayout(
        activity: Activity,
        drawerLayout: DrawerLayout,
        statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        setTransparentForDrawerLayout(
            activity,
            drawerLayout
        )
        addTranslucentView(
            activity,
            statusBarAlpha
        )
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    fun setTransparentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window
                .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        // 内容布局不是 LinearLayout 时,设置padding top
        if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                .setPadding(
                    0,
                    getStatusBarHeight(activity),
                    0,
                    0
                )
        }

        // 设置属性
        val drawer = drawerLayout.getChildAt(1) as ViewGroup
        drawerLayout.setFitsSystemWindows(false)
        contentLayout.fitsSystemWindows = false
        contentLayout.clipToPadding = true
        drawer.fitsSystemWindows = false
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明(5.0以上半透明效果,不建议使用)
     *
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    @Deprecated("")
    fun setTranslucentForDrawerLayoutDiff(activity: Activity, drawerLayout: DrawerLayout) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 设置内容布局属性
            val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
            contentLayout.fitsSystemWindows = true
            contentLayout.clipToPadding = true
            // 设置抽屉布局属性
            val vg = drawerLayout.getChildAt(1) as ViewGroup
            vg.fitsSystemWindows = false
            // 设置 DrawerLayout 属性
            drawerLayout.setFitsSystemWindows(false)
        }
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTransparentForImageView(activity: Activity, needOffsetView: View?) {

        setTranslucentForImageView(
            activity,
            0,
            needOffsetView
        )
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageView(activity: Activity, needOffsetView: View) {

        setTranslucentForImageView(
            activity,
            DEFAULT_STATUS_BAR_ALPHA,
            needOffsetView
        )
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageView(activity: Activity, statusBarAlpha: Int, needOffsetView: View?) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window
                .decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            activity.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
        }
        addTranslucentView(
            activity,
            statusBarAlpha
        )
        if (needOffsetView != null) {
            val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(
                0,
                getStatusBarHeight(activity),
                0,
                0
            )
        }
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageViewInFragment(activity: Activity, needOffsetView: View) {

        setTranslucentForImageViewInFragment(
            activity,
            DEFAULT_STATUS_BAR_ALPHA,
            needOffsetView
        )
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTransparentForImageViewInFragment(activity: Activity, needOffsetView: View) {

        setTranslucentForImageViewInFragment(
            activity,
            0,
            needOffsetView
        )
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param activity       fragment 对应的 activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageViewInFragment(
        activity: Activity,
        statusBarAlpha: Int,
        needOffsetView: View
    ) {

        setTranslucentForImageView(
            activity,
            statusBarAlpha,
            needOffsetView
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun clearPreviousSetting(activity: Activity) {

        val decorView = activity.window
            .decorView as ViewGroup
        val count = decorView.childCount
        if (count > 0 && decorView.getChildAt(count - 1) is View) {
            decorView.removeViewAt(count - 1)
            val rootView =
                (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
            rootView.setPadding(
                0,
                0,
                0,
                0
            )
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private fun addTranslucentView(activity: Activity, statusBarAlpha: Int) {

        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        if (contentView.childCount > 1) {
            contentView.getChildAt(1)
                .setBackgroundColor(
                    Color.argb(
                        statusBarAlpha,
                        0,
                        0,
                        0
                    )
                )
        } else {
            contentView.addView(
                createTranslucentStatusBarView(
                    activity,
                    statusBarAlpha
                )
            )
        }
    }

    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private fun createStatusBarView(activity: Activity, color: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.setLayoutParams(params)
        statusBarView.setBackgroundColor(color)
        return statusBarView
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private fun createStatusBarView(activity: Activity, color: Int, alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.setLayoutParams(params)
        statusBarView.setBackgroundColor(
            calculateStatusColor(
                color,
                alpha
            )
        )
        return statusBarView
    }

    /**
     * 设置根布局参数
     */
    private fun setRootView(activity: Activity) {

        val rootView =
            (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
        rootView.clipToPadding = true
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun transparentStatusBar(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window
                .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private fun createTranslucentStatusBarView(activity: Activity, alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.setLayoutParams(params)
        statusBarView.setBackgroundColor(
            Color.argb(
                alpha,
                0,
                0,
                0
            )
        )
        return statusBarView
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId = context.resources
            .getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
        return context.resources
            .getDimensionPixelSize(resourceId)
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(color: Int, alpha: Int): Int {

        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun flymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {

        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag =
                    WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags =
                    WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(
                    lp,
                    value
                )
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {

        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(
                        window,
                        darkModeFlag,
                        darkModeFlag
                    )//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(
                        window,
                        0,
                        darkModeFlag
                    )//清除黑色字体
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置状态栏字体颜色，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    fun statusBarDarkLightMode(activity: Activity, isDark: Boolean, isFullscreen: Boolean): Int {

        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDark) {
                //设置状态栏黑色字体
                if (isFullscreen) {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                //恢复状态栏白色字体
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
            result = 3
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.window, isDark)) {
                result = 1
            } else if (flymeSetStatusBarLightMode(activity.window, isDark)) {
                result = 2
            }
        }
        return result
    }


    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    fun statusBarLightMode(activity: Activity, type: Int) {

        when (type) {
            1 -> MIUISetStatusBarLightMode(
                activity.window,
                true
            )
            2 -> flymeSetStatusBarLightMode(
                activity.window,
                true
            )
            3 -> activity.window
                .decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    fun StatusBarDarkMode(activity: Activity): Int {

        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window
                .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            result = 3
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.window, false)) {
                result = 1
            } else if (flymeSetStatusBarLightMode(activity.window, false)) {
                result = 2
            }
        }
        return result
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    fun StatusBarDarkMode(activity: Activity, type: Int) {

        when (type) {
            1 -> MIUISetStatusBarLightMode(
                activity.window,
                false
            )
            2 -> flymeSetStatusBarLightMode(
                activity.window,
                false
            )
            3 -> activity.window
                .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

    }
}

