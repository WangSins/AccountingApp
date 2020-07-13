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
        initActionBar()
        setContentView(getLayoutResId())
        initData()
        initEvent()
    }

    open fun initActionBar() {

    }

    abstract fun getLayoutResId(): Int

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