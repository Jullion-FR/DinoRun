package com.jdauvergne.dinorun.display.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.jdauvergne.dinorun.R

class OptionsDialog(
    context: Context
) : AbstractDialog(context, R.layout.dialog_options)
{
    companion object {
        const val PREFS_NAME = "options_prefs"
        const val TOUCH_MODE_KEY = "touch_mode"
        const val SHAKE_MODE_KEY = "shake_mode"
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val exitButton = dialog.findViewById<Button>(R.id.exitButton)

        val txt = "Options"
        scoreText.text = txt

        exitButton.setOnClickListener { dialog.dismiss() }

        val touchModeSwitch = dialog.findViewById<Switch>(R.id.touchModeSwitch)
        val shakeModeSwitch = dialog.findViewById<Switch>(R.id.shakeModeSwitch)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var touchEnabled = prefs.getBoolean(TOUCH_MODE_KEY, true)
        var shakeEnabled = prefs.getBoolean(SHAKE_MODE_KEY, false)

        if (!touchEnabled && !shakeEnabled) {
            touchEnabled = true
        }

        touchModeSwitch.isChecked = touchEnabled
        shakeModeSwitch.isChecked = shakeEnabled

        touchModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && !shakeModeSwitch.isChecked) {
                touchModeSwitch.isChecked = true
            } else {
                prefs.edit().putBoolean(TOUCH_MODE_KEY, isChecked).apply()
            }
        }

        shakeModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && !touchModeSwitch.isChecked) {
                shakeModeSwitch.isChecked = true
            } else {
                prefs.edit().putBoolean(SHAKE_MODE_KEY, isChecked).apply()
            }
        }

        dialog.show()
    }
}