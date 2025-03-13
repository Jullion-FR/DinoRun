package com.jdauvergne.dinorun.display

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.ImageView
import com.jdauvergne.dinorun.R

class FullScreenModalDialog(
    context: Context,
    private val replayAction: () -> Unit
) : Dialog(context) {

    private lateinit var replayImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        hideSystemUI()
        setContentView(R.layout.death_screen)

        window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        replayImageView = findViewById(R.id.replayImageView)

        replayImageView.setOnClickListener {
            replayAction()
            dismiss()
        }
    }

    private fun hideSystemUI() {
        window?.decorView?.let { decorView ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                decorView.windowInsetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        )
            }
        }
    }
}