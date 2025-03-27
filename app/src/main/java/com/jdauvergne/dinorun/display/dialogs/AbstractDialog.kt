package com.jdauvergne.dinorun.display.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.jdauvergne.dinorun.Tools

abstract class AbstractDialog(
    protected val context:Context,
    protected val R_ID:Int
) {
    protected val dialog = Dialog(context)
    init {
        initialize()
    }

    private fun initialize(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R_ID)
        dialog.window?.let { Tools.hideSystemUI(window = it) }
        dialog.setCancelable(false)
    }

    open fun show() {
        dialog.show()
    }
}