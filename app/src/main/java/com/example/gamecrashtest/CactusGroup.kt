package com.example.gamecrashtest

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.launch

class CactusGroup(
    private var cactusList: List<Cactus>
) {

    fun spawn(){
        for (cactus: Cactus in cactusList){
            cactus.spawn()
            for(i in 0..< cactusList.indexOf(cactus)){
                //cactusList[i] in case of different sizes in group (ex: SmlMedSml)
                cactus.spriteOffset = cactusList[i].size.width.dpToPx.toFloat()/2
            }
        }
    }
     fun startMoving(lifecycleScope: LifecycleCoroutineScope) {
        var start:Float
        var target:Float
        for (cactus: Cactus in cactusList){
            lifecycleScope.launch {
                start = cactus.x + cactus.spriteOffset
                target = cactus.spriteOffset - cactus.x

                cactus.startMoving(
                    start,
                    target
                )
            }
        }
    }
}