package com.ikakus.breadcrumbs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView


class DaysRecyclerViewAdapter(private val myDataset: List<Boolean>) :
    RecyclerView.Adapter<DaysRecyclerViewAdapter.DaysViewHolder>() {


    class DaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaysViewHolder {
        context = parent.context
        val dayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_view, parent, false) as LinearLayout
        return DaysViewHolder(dayView)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.itemView.setBackgroundDrawable(if(myDataset[position]){
            context.resources.getDrawable(R.drawable.day_checked)
        }else{
            context.resources.getDrawable(R.drawable.day_unchecked)
        })
    }

    override fun getItemCount() = myDataset.size
}