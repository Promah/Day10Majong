package com.example.day10majong


import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.day10majong.logic.MajongLogic
import kotlinx.android.synthetic.main.cell_view.view.*



class MajongRecyclerAdapter(val logic : MajongLogic) :  RecyclerView.Adapter<RecyclerItemHolder>(){

    val stoneDrawables = arrayOf(
        R.drawable.majong_icon_0,
        R.drawable.majong_icon_1,
        R.drawable.majong_icon_2,
        R.drawable.majong_icon_3,
        R.drawable.majong_icon_4,
        R.drawable.majong_icon_5,
        R.drawable.majong_icon_6,
        R.drawable.majong_icon_7
    )
    private var ignoreInput = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_view, parent, false)
        return RecyclerItemHolder(v)
    }

    override fun getItemCount(): Int {
        return logic.height * logic.width
    }

    override fun onBindViewHolder(holder: RecyclerItemHolder, index: Int) {

        val x = index % logic.width
        val y = index / logic.width

        val stone = logic.getStone(x, y)
        val open = logic.isStoneOpen(x, y)

        holder.setBackgroundRes( if (open) stoneDrawables[stone] else R.drawable.ic_launcher_background )

        holder.onBind {
            if (ignoreInput)
                return@onBind

            val index = holder.adapterPosition
            val x = index % logic.width
            val y = index / logic.width

            if (logic.isStoneOpen(x, y))
                return@onBind

            val undoPair = logic.action(x, y)
            if (undoPair != null) {
                ignoreInput = true
                Handler(Looper.getMainLooper()).postDelayed( {
                    ignoreInput = false
//                    logic.reset()
                    logic.performUndo(undoPair)
                    notifyDataSetChanged()
                }, 1000);
            }

            if (logic.haveWon()) {
//                Toast.makeText(holder.itemView.context, "You have won!", Toast.LENGTH_SHORT).show()
                logic.shuffle()
            }
            notifyDataSetChanged()
        }
    }

}
class RecyclerItemHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun setBackgroundRes(resId: Int){
        itemView.setBackgroundResource(resId)
    }

    fun onBind(onClick: (()->Unit)? ){
        itemView.cell_single.setOnClickListener {
            onClick?.invoke()
        }
    }
}