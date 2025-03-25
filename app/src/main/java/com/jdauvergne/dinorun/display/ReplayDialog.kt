package com.jdauvergne.dinorun.display

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools

class ReplayDialog(
    private val context: Context,
    private val score: Int,
    private val highScore: Int,
    private val onReplay: () -> Unit
) {
    @SuppressLint("SetTextI18n")
    fun show() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_game_over)
        dialog.window?.let { Tools.hideSystemUI(window = it) }
        dialog.setCancelable(false)

        val scoreText = dialog.findViewById<TextView>(R.id.tv_score)
        val replayButton = dialog.findViewById<ImageView>(R.id.btn_replay)

        scoreText.text = "Game Over !\n\nScore : $score\nHigh Score : $highScore"

        replayButton.setOnClickListener {
            dialog.dismiss()
            onReplay()
        }

        dialog.window?.let { Tools.hideSystemUI(window = it) }
        dialog.show()
    }
}
