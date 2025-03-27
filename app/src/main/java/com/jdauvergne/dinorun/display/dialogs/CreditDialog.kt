package com.jdauvergne.dinorun.display.dialogs

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.jdauvergne.dinorun.R

class CreditDialog(
    context: Context
) : AbstractDialog(context, R.layout.dialog_credits)
{
    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val replayButton = dialog.findViewById<Button>(R.id.dialogButton)

        val txt =
            "Jeu créé par\nJulien DAUVERGNE\n" +
            "Merci d'avoir joué !"
        scoreText.text = txt

        replayButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}