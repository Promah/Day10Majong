package com.example.day10majong


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.day10majong.logic.MajongLogic
import kotlinx.android.synthetic.main.fragment_game.view.*


class GameFragment : Fragment() {

    companion object {
        val IS_START_NEW_GAME = "isGameFragmentNidToNewGame"
    }

    private var logic: MajongLogic? = null
    private var score : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_game, container, false)
        v.recyclerView.layoutManager = GridLayoutManager(context, 4)

        val model = ViewModelProviders.of(activity!!).get(Communicator::class.java)
        logic = model.getLogic()
        v.recyclerView.adapter = MajongRecyclerAdapter(logic!!)

        //handle game won event
        logic?.isGameFinished?.observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
            if (it == true){
                model.setHighestScores(score!!)
                Toast.makeText(activity, "You have won! You`r score is $score! You clicked ${logic!!.currentClicksUsed} times!", Toast.LENGTH_SHORT).show()
                getFragmentManager()?.popBackStackImmediate()

            }
        })

        //live update score
        logic!!.getScore().observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
            score = it
            v.textViewScore.text = it.toString()
        })

        return v
    }

}
