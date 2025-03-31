package com.jdauvergne.dinorun.display.dialogs

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.display.MainActivity
import com.jdauvergne.dinorun.display.MenuActivity


class ReplayDialog(
    private val mainActivity: MainActivity,
    private val score: Int,
    private val isHighScore:Boolean = false,
    private val onReplay: () -> Unit
) : AbstractDialog(mainActivity, R.layout.dialog_replay)
 {
    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val replayButton = dialog.findViewById<Button>(R.id.dialogReplayButton)
        val replayImageView = dialog.findViewById<ImageView>(R.id.dialogReplayImageView)
        val exitButton = dialog.findViewById<Button>(R.id.dialogExitButton)

        val score =
            if(isHighScore) "New High Score : $score !\n"
            else "Score : $score"
        val txt = "Game Over !\n" +
                score
        scoreText.text = txt

        replayButton.setOnClickListener {
            replay()
        }
        replayImageView.setOnClickListener {
            replay()
        }
        exitButton.setOnClickListener {
            exit()
        }

        dialog.show()
    }
     private fun replay(){
         dialog.dismiss()
         onReplay()
     }
     private fun exit(){
         val intent = Intent(context, MenuActivity::class.java)
         startActivity(context, intent, null)
         mainActivity.finish()
     }
}
