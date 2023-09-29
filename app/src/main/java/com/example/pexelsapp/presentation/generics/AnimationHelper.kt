package com.example.pexelsapp.presentation.generics

import android.view.View
import android.view.ViewPropertyAnimator

class AnimationHelper<T : View>(private val view: T) {
    private var currentAnimator: ViewPropertyAnimator? = null
    fun animateScaleDownAndUp(
        scaleFactor: Float,
        duration: Long
    ) {
        currentAnimator?.cancel()

        val originalScaleX = view.scaleX
        val originalScaleY = view.scaleY
        currentAnimator = view.animate()
            .scaleX(scaleFactor)
            .scaleY(scaleFactor)
            .setDuration(duration)
            .withEndAction {
                view.animate()
                    .scaleX(originalScaleX)
                    .scaleY(originalScaleY)
                    .setDuration(duration)
                    .start()
            }
        currentAnimator?.start()
    }

    fun cancelAnimation() {
        currentAnimator?.cancel()
    }
}
