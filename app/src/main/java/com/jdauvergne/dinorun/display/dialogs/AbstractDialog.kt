package com.jdauvergne.dinorun.display.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.jdauvergne.dinorun.Tools

/**
 * An abstract class representing a custom dialog in the game.
 * This class initializes and configures a dialog with a given layout resource.
 *
 * @property context The context in which the dialog is created.
 * @property resId The resource ID of the layout to be used for the dialog.
 */
abstract class AbstractDialog(
    protected val context: Context,
    private val resId: Int
) {
    /**
     * The dialog instance that is managed by this class.
     */
    protected val dialog = Dialog(context)

    init {
        initialize()
    }

    /**
     * Initializes the dialog by setting its layout, removing the title,
     * hiding system UI, and making it non-cancelable.
     */
    private fun initialize() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(resId)
        dialog.window?.let { Tools.hideSystemUI(window = it) }
        dialog.setCancelable(false)
    }

    /**
     * Displays the dialog.
     * Can be overridden by subclasses to modify the show behavior.
     */
    open fun show() {
        dialog.show()
    }
}
