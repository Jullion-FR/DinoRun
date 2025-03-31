package com.jdauvergne.dinorun.display.dialogs

import android.content.Context
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.jdauvergne.dinorun.R

class OptionsDialog(
    context: Context
) : AbstractDialog(context, R.layout.dialog_options) {

    companion object {
        const val PREFS_NAME = "options_prefs"
        const val TOUCH_MODE_KEY = "touch_mode"
        const val SHAKE_MODE_KEY = "shake_mode"
        const val VOLUME_KEY = "volume"
    }

    override fun show() {
        val scoreText = dialog.findViewById<TextView>(R.id.dialogTextView)
        val exitButton = dialog.findViewById<Button>(R.id.exitButton)

        scoreText.text = "Options"
        exitButton.setOnClickListener { dialog.dismiss() }

        val touchModeSwitch = dialog.findViewById<SwitchCompat>(R.id.touchModeSwitch)
        val shakeModeSwitch = dialog.findViewById<SwitchCompat>(R.id.shakeModeSwitch)
        val volumeSeekBar = dialog.findViewById<SeekBar>(R.id.volumeSeekBar)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var touchEnabled = prefs.getBoolean(TOUCH_MODE_KEY, true)
        val shakeEnabled = prefs.getBoolean(SHAKE_MODE_KEY, false)
        val savedVolume = prefs.getFloat(VOLUME_KEY, 0.5f)

        if (!touchEnabled && !shakeEnabled) {
            touchEnabled = true
        }

        touchModeSwitch.isChecked = touchEnabled
        shakeModeSwitch.isChecked = shakeEnabled
        volumeSeekBar.progress = (savedVolume * 100).toInt()

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

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                prefs.edit().putFloat(VOLUME_KEY, progress / 100f).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        dialog.show()
    }
}
