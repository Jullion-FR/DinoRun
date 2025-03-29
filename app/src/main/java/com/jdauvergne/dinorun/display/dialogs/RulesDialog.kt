package com.jdauvergne.dinorun.display.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jdauvergne.dinorun.R

class RulesDialog(
    context: Context
) : AbstractDialog(context, R.layout.dialog_rules) {

    private val images = listOf(
        ContextCompat.getDrawable(context, R.drawable.tuto1),
        ContextCompat.getDrawable(context,R.drawable.tuto2),
        ContextCompat.getDrawable(context, R.drawable.tuto3)
    )
    private var index = 0
    private val tutoView = dialog.findViewById<View>(R.id.tutoView)

    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val exitButton = dialog.findViewById<Button>(R.id.exitButton)
        val nextButton = dialog.findViewById<Button>(R.id.nextButton)
        val previousButton = dialog.findViewById<Button>(R.id.previousButton)

        val txt ="Règles"
        scoreText.text = txt
        updateImage()

        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        nextButton.setOnClickListener {
            nextImage()
        }

        previousButton.setOnClickListener {
            previousImage()
        }

        dialog.show()
    }

    private fun updateImage() {
        tutoView.background =  images[index]
    }

    private fun nextImage() {
        index = (index + 1) % images.size
        updateImage()
    }

    private fun previousImage() {
        index = if (index - 1 < 0) images.size - 1 else index - 1
        updateImage()
    }
}