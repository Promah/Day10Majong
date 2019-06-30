package com.example.day10majong

import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_menu.view.*


import android.graphics.drawable.GradientDrawable


class MenuFragment : Fragment() {
    private var model: Communicator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        //animate menuBG
        val animationBGChangeDuration = 3000 //ms

        val animatedBG = AnimationDrawable()
        animatedBG.isOneShot = false
        animatedBG.setEnterFadeDuration(animationBGChangeDuration)
        animatedBG.setExitFadeDuration(animationBGChangeDuration)

        val gradientDrawable1 = GradientDrawable(
            GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                resources.getColor(R.color.mtrl_textinput_default_box_stroke_color),
                resources.getColor(R.color.button_material_dark)
            )
        )
        val gradientDrawable2 = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                resources.getColor(R.color.mtrl_btn_transparent_bg_color),
                resources.getColor(R.color.mtrl_textinput_default_box_stroke_color)
            )
        )
        val gradientDrawable3 = GradientDrawable(
            GradientDrawable.Orientation.BR_TL,
            intArrayOf(
                resources.getColor(R.color.button_material_dark),
                resources.getColor(R.color.mtrl_textinput_default_box_stroke_color)
            )
        )

        animatedBG.addFrame(gradientDrawable1, animationBGChangeDuration)
        animatedBG.addFrame(gradientDrawable2, animationBGChangeDuration)
        animatedBG.addFrame(gradientDrawable3, animationBGChangeDuration)

        v.background = animatedBG
        animatedBG.start()

        //animate textViews
        v.textViewStartNewGame.height += 30

        val animatorTextViewNewGame = ObjectAnimator()
        animatorTextViewNewGame.target = v.textViewStartNewGame
        animatorTextViewNewGame.setProperty(TextView.ROTATION)
        animatorTextViewNewGame.duration = 5500
        animatorTextViewNewGame.setFloatValues(0f, -4f, 4f, 0f)
        animatorTextViewNewGame.repeatCount = ObjectAnimator.INFINITE
        animatorTextViewNewGame.start()

        val animatorTextViewNewGame2 = ObjectAnimator()
        animatorTextViewNewGame2.target = v.textViewStartNewGame
        animatorTextViewNewGame2.setProperty(TextView.TRANSLATION_Y)
        animatorTextViewNewGame2.duration = 4000
        animatorTextViewNewGame2.setFloatValues(0f, -3f, 3f, 0f)
        animatorTextViewNewGame2.repeatCount = ObjectAnimator.INFINITE
        animatorTextViewNewGame2.start()

//        not working -> rewriting existing animatorTextViewNewGame
//        val animatorTextViewContinueGame = animatorTextViewNewGame.apply {this.target = v.textViewContinueGame }
        val animatorTextViewContinueGame = ObjectAnimator()
        animatorTextViewContinueGame.target = v.textViewContinueGame
        animatorTextViewContinueGame.setProperty(TextView.ROTATION)
        animatorTextViewContinueGame.duration = 4500
        animatorTextViewContinueGame.setFloatValues(0f, 5f, -5f, 0f)
        animatorTextViewContinueGame.repeatCount = ObjectAnimator.INFINITE
        animatorTextViewContinueGame.start()

        val animatorTextViewContinueGame2 = ObjectAnimator()
        animatorTextViewContinueGame2.target = v.textViewContinueGame
        animatorTextViewContinueGame2.setProperty(TextView.SCALE_X)
        animatorTextViewContinueGame2.duration = 3000
        animatorTextViewContinueGame2.setFloatValues(1f, 0.95f, 1.05f, 1f)
        animatorTextViewContinueGame2.repeatCount = ObjectAnimator.INFINITE
        animatorTextViewContinueGame2.start()


        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)
        val logic = model!!.getLogic()

        //checking is any game in progress
        logic.isGameStarted.observe(viewLifecycleOwner, Observer {
            if (it == true)
                v.textViewContinueGame.visibility = TextView.VISIBLE
            else
                v.textViewContinueGame.visibility = TextView.GONE
        })

        //set HighestScores on game finish
        model?.getHighestScores()?.observe(viewLifecycleOwner, Observer {
            if ((it ?: 0) > v.textViewHighestScore.text.toString().toInt())
                v.textViewHighestScore.text = it.toString()
        })

        v.findViewById<TextView>(R.id.textViewStartNewGame).setOnClickListener {
            model?.getNewLogic()
            findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
        }
        v.findViewById<TextView>(R.id.textViewContinueGame).setOnClickListener {
            model?.getLogic()
            findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
        }

        return v
    }
}
