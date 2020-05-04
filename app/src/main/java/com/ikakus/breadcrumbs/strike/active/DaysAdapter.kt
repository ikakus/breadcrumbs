package com.ikakus.breadcrumbs.strike.active

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import kotlinx.android.synthetic.main.day_view.view.*


class DaysAdapter(
    private val days: List<Long>,
    var today: Boolean
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    class DaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var checkPosition: Int = 0
    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaysViewHolder {
        context = parent.context
        val dayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_view, parent, false) as LinearLayout
        return DaysViewHolder(
            dayView
        )
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {

            when {
                days[position] > 0 -> {
                    val background = context.resources.getDrawable(R.drawable.day_checked)
                    holder.itemView.day_view_layout_back.setBackgroundDrawable(background)
                    holder.itemView.day_view_layout.setPadding(20)
                }
                position == checkPosition && !today -> {
                    val background = context.resources.getDrawable(R.drawable.day_current)
                    holder.itemView.day_view_layout_back.setBackgroundDrawable(background)
                }
                else -> {
                    val background = context.resources.getDrawable(R.drawable.day_unchecked)
                    holder.itemView.day_view_layout_back.setBackgroundDrawable(background)
                }
            }


    }

    override fun getItemCount() = days.size

    fun setCheckPosition(checkPosition: Int) {
        this.checkPosition = checkPosition
    }
}