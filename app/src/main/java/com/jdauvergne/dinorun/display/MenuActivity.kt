package com.jdauvergne.dinorun.display

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.display.dialogs.CreditDialog

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        Tools.hideSystemUI(window)
        setContentView(R.layout.main_menu) // Assurez-vous que ce fichier est bien activity_menu.xml

        // Références des boutons
        val startButton: Button = findViewById(R.id.startButton)
        val creditsButton: Button = findViewById(R.id.creditsButton)
        val quitButton: Button = findViewById(R.id.quitButton)

        // Lancer l'activité MainActivity lorsque l'on clique sur Démarrer
        startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Afficher les crédits lorsque l'on clique sur Crédits
        creditsButton.setOnClickListener {
            showCredits()
        }

        // Quitter l'application lorsqu'on clique sur Quitter
        quitButton.setOnClickListener {
            finish() // Ferme l'activité et retourne à l'écran précédent
        }
    }

    private fun showCredits() {
        CreditDialog(this).show()
    }
}
