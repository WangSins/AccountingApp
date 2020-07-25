package me.sin.accountingapp.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by Sin on 2020/7/12
 */
object AnimatorUtil {

    fun scaleObjectAnimation(targetView: View, startSize: Float, endSize: Float, durationParam: Long) {
        val scaleY = ObjectAnimator.ofFloat(targetView, "scaleY", startSize, endSize)
        val scaleX = ObjectAnimator.ofFloat(targetView, "scaleX", startSize, endSize)
        with(AnimatorSet()) {
            play(scaleY).with(scaleX)
            duration = durationParam
            start()
        }
    }

    fun rotationObjectAnimation(targetView: View, startAngle: Float, endAngle: Float, durationParam: Long) {
        val rotate = ObjectAnimator.ofFloat(targetView, "rotation", startAngle, endAngle)
        rotate.duration = durationParam
        rotate.start()
    }
}