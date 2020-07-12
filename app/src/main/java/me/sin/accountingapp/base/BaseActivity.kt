package me.sin.accountingapp.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Sin on 2020/7/12
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setContentView(getLayoutResId())
    }

    open fun initActionBar() {

    }

    abstract fun getLayoutResId(): Int

    override fun onStart() {
        super.onStart()
        initData()
        initEvent()
    }

    open fun initData() {

    }

    open fun initEvent() {

    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    open fun release() {

    }
}