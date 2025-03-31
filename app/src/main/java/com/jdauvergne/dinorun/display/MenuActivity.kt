package com.jdauvergne.dinorun.display

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.display.dialogs.CreditDialog
import com.jdauvergne.dinorun.display.dialogs.OptionsDialog
import com.jdauvergne.dinorun.display.dialogs.RulesDialog
import com.jdauvergne.dinorun.music.Music
import com.jdauvergne.dinorun.music.MusicPlayer

class MenuActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var creditsButton:Button
    private lateinit var quitButton:Button

    private lateinit var optionsButton:Button
    private lateinit var rulesButton:Button
    private val musicPlayer = MusicPlayer(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Tools.hideSystemUI(window)
        setContentView(R.layout.main_menu)

        startButton = findViewById(R.id.startButton)
        creditsButton = findViewById(R.id.creditsButton)
        quitButton = findViewById(R.id.quitButton)

        optionsButton = findViewById(R.id.optionsButton)
        rulesButton = findViewById(R.id.rulesButton)


        initListeners()
        musicPlayer.music = Music.MENU1
        musicPlayer.start()
    }

    override fun onResume() {
        super.onResume()
        musicPlayer.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
    }

    private fun initListeners() {
        startButton.setOnClickListener {
            val prefs = getSharedPreferences(OptionsDialog.PREFS_NAME, Context.MODE_PRIVATE)
            val isTouchModeEnabled = prefs.getBoolean(OptionsDialog.TOUCH_MODE_KEY, true)
            val isShakeModeEnabled = prefs.getBoolean(OptionsDialog.SHAKE_MODE_KEY, false)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(OptionsDialog.TOUCH_MODE_KEY, isTouchModeEnabled)
            intent.putExtra(OptionsDialog.SHAKE_MODE_KEY, isShakeModeEnabled)
            startActivity(intent)
            this.finish()
        }

        creditsButton.setOnClickListener {
            showCredits()
        }

        quitButton.setOnClickListener {
            finish()
        }

        optionsButton.setOnClickListener {
            showOptions()
        }

        rulesButton.setOnClickListener {
            showRules()
        }
    }

    //These are recreated each time
    //todo var them to load once
    private fun showCredits() {
        CreditDialog(this).show()
    }
    private fun showOptions() {
        OptionsDialog(this).show()
    }
    private fun showRules() {
        RulesDialog(this).show()
    }
}
