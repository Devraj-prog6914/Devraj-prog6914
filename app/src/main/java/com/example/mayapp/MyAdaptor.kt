package com.example.mayapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.mayapp.databinding.ItemveiwBinding // Ensure correct binding import

class MyAdaptor(private var datalist: ArrayList<datamodel>, private var context: Context) :
    RecyclerView.Adapter<MyAdaptor.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemveiwBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = datalist.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = datalist[position]

        holder.binding.Crbnd.text = data.name
        holder.binding.crsat.text = data.address

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MainActivity4::class.java)
            context.startActivity(intent)
            if (context is MainActivity4) {
                (context as MainActivity4).finish()
            }
        }
    }

    inner class MyViewHolder(val binding: ItemveiwBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val anima = AlphaAnimation(0.0f, 1.0f)
            anima.duration = 2000
            binding.root.startAnimation(anima)
        }
    }
}
