package me.sin.accountingapp.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast


/**
 * Created by Sin on 2020/7/12
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        getExtra()
        initActionBar()
        initData()
        initEvent()
    }

    abstract fun getLayoutResId(): Int
    open fun getExtra() {}
    open fun initActionBar() {}
    open fun initData() {}
    open fun initEvent() {}
    open fun release() {}

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    open fun startActivityForResult(cls: Class<*>?, bundle: Bundle?,
                                    requestCode: Int) {
        val intent = Intent()
        intent.setClass(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    open fun toast(text: String?) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

}