package com.jdauvergne.dinorun.display.dialogs

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.jdauvergne.dinorun.R

class RulesDialog(
    context: Context
) : AbstractDialog(context, R.layout.dialog_rules)
{
    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val replayButton = dialog.findViewById<ImageView>(R.id.dialogButton)

        val txt = ""
        scoreText.text = txt

        replayButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}