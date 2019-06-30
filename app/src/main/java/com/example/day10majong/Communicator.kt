package com.example.day10majong

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.day10majong.logic.MajongLogic


class Communicator : ViewModel(){

    private var mainLogic = MajongLogic.getInstance(4,4)
    fun getLogic() = mainLogic
    fun getNewLogic(): MajongLogic {
        MajongLogic.deleteInstance()
        mainLogic = MajongLogic.getInstance(4,4)
        return mainLogic
    }

//    val _isGameStarted = MutableLiveData<Boolean>()
//    fun isGameStarted() = mainLogic?.isGameFinished
//    fun getGameState(): LiveData<Boolean> = isGameStarted
//    fun setGameState(isStarted:Boolean){
//        isGameStarted.postValue(isStarted)
//    }

//    val logic = MutableLiveData<MajongLogic>()
//    fun getLocic(w:Int, h:Int){
//        val _logic = MajongLogic.getInstance(w,h)
//        logic.postValue(_logic)
//    }
//    fun renewLocic(w:Int, h:Int){
//        MajongLogic.deleteInstance()
//        val _logic = MajongLogic.getInstance(w,h)
//        logic.postValue(_logic)
//    }

    private val highestScores = MutableLiveData<Int>()
    fun getHighestScores():LiveData<Int> = highestScores
    fun setHighestScores(scores: Int){
        highestScores.postValue(scores)
    }

}