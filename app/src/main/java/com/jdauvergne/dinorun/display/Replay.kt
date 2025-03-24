package com.jdauvergne.dinorun.display

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools

class Replay(private val context: Context, private val score: Int, private val onReplay: () -> Unit) {

    fun show() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Supprime le titre
        dialog.setContentView(R.layout.dialog_game_over)
        dialog.window?.let { Tools.hideSystemUI(window = it) }
        dialog.setCancelable(false) // Empêche la fermeture en cliquant en dehors

        val scoreText = dialog.findViewById<TextView>(R.id.tv_score)
        val replayButton = dialog.findViewById<ImageView>(R.id.btn_replay)

        scoreText.text = "Game Over !\n\nScore : $score`\n"

        replayButton.setOnClickListener {
            dialog.dismiss()
            onReplay() // Appelle la fonction pour rejouer
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) // Plein écran
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Fond transparent
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN // Cache la barre système
        dialog.show()
    }
}
