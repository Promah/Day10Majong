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

    private val highestScores = MutableLiveData<Int>()
    fun getHighestScores():LiveData<Int> = highestScores
    fun setHighestScores(scores: Int){
        highestScores.postValue(scores)
    }

}