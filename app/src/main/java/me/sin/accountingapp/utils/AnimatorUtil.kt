package me.sin.accountingapp.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by Sin on 2020/7/12
 */
object AnimatorUtil {
    fun scaleObjectAnimation(targetView: View, startSize: Float, endSize: Float, duration: Long) {
        val scaleY = ObjectAnimator.ofFloat(targetView, "scaleY", startSize, endSize)
        val scaleX = ObjectAnimator.ofFloat(targetView, "scaleX", startSize, endSize)

        val set = AnimatorSet()
        set.play(scaleY).with(scaleX)
        set.duration = duration
        set.start()
    }
}