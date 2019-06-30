package com.example.day10majong.logic

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import kotlin.random.Random

class MajongLogic(val width:Int = 4, val height:Int = 4) {

    companion object{
        private var instance : MajongLogic?= null


        fun getInstance(width:Int, height:Int): MajongLogic{
            if (instance == null){
                instance = MajongLogic(width, height)
            }
            return instance!!
        }
        fun deleteInstance(){
            instance = null
        }
    }

    private val stones = mutableListOf<Int>()
    private val stateOpen = mutableListOf<Boolean>()
    private var actionStoneIdx:Int? = null

    private val maxPossibleScore = width*height*1000
    private val minClicksUsedToHaveWon = width*height

    var currentClicksUsed = 0

    val score = MutableLiveData<Int>()
    fun getScore() : LiveData<Int>{
        return score
    }

    val isGameFinished = MutableLiveData<Boolean>()
    val isGameStarted = MutableLiveData<Boolean>()

    init {
        isGameFinished.postValue(false)

        assert(width == 4 && height == 4)

        for (i in 0 until width * height) {
            stones.add(i / 2)
            stateOpen.add(false)
        }
        shuffle()

        score.postValue(maxPossibleScore)
    }


    fun toIndex(x:Int, y:Int):Int = y * width + x

    fun getStone(x:Int, y:Int):Int = stones[toIndex(x, y)]

    fun isStoneOpen(x:Int, y:Int):Boolean = stateOpen[toIndex(x, y)]

    fun haveWon():Boolean {
        for (i in 0 until width * height) {
            if (!stateOpen[i])
                return false
        }
        isGameFinished.postValue(true)
        isGameStarted.postValue(false)
        return true
    }

//    fun reset() {
//        for (i in 0 until width * height) {
//            stateOpen[i] = false
//        }
//        actionStoneIdx = null
//    }

    fun action(x:Int, y:Int) : Pair<Int,Int>? {
        isGameStarted.postValue(true)
        currentClicksUsed++

        if (currentClicksUsed <= minClicksUsedToHaveWon)
            score.postValue(maxPossibleScore)
        else
            score.postValue(maxPossibleScore *  minClicksUsedToHaveWon / currentClicksUsed )


        var undoPair:Pair<Int, Int>? = null
        val idx = toIndex(x, y)
        assert(stateOpen[idx] == false)

        if (actionStoneIdx != null) {
            val oldStone = stones[actionStoneIdx!!]
            val newStone = stones[idx]

            // open it even if fail
            stateOpen[idx] = true
            if (newStone != oldStone) {
                // return pair for undo
                undoPair = Pair(actionStoneIdx!!, idx)
            }

            // reset action stone
            actionStoneIdx = null
        } else {
            actionStoneIdx = idx
            stateOpen[actionStoneIdx!!] = true
        }

        return undoPair
    }

    fun performUndo(undoPair: Pair<Int, Int>) {
        assert(stateOpen[undoPair.first] == false)
        assert(stateOpen[undoPair.second] == false)

        stateOpen[undoPair.first] = false
        stateOpen[undoPair.second] = false
    }

    fun shuffle() {
        val r = Random
        for (idx1 in 0 until width * height) {
            val idx2 = idx1 + r.nextInt(width * height - (idx1))
            val t = stones[idx1]
            stones[idx1] = stones[idx2]
            stones[idx2] = t
            stateOpen[idx1] = false
        }
    }
}