package com.jdauvergne.dinorun.music
import android.content.Context
import android.media.MediaPlayer
import com.jdauvergne.dinorun.DinoServiceInterface
import com.jdauvergne.dinorun.display.dialogs.OptionsDialog

class MusicPlayer(private val context: Context): DinoServiceInterface {

    var music = Music.GAME1
    private var mediaPlayer: MediaPlayer? = null
     override fun start() {
        val prefs = context.getSharedPreferences(OptionsDialog.PREFS_NAME, Context.MODE_PRIVATE)
        val volume = prefs.getFloat(OptionsDialog.VOLUME_KEY, 0.5f) // Récupération du volume

        mediaPlayer = MediaPlayer.create(context, music.resId).apply {
            setVolume(volume, volume)
            start()
        }
    }

    override fun pause() {
        mediaPlayer?.apply {
            if (isPlaying) pause()
        }
    }

    override fun resume() {
        mediaPlayer?.apply {
            if (!isPlaying) start()
        }
    }

    override fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }
}
