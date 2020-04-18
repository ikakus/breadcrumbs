package com.ikakus.breadcrumbs.strike.active

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import kotlinx.android.synthetic.main.day_view.view.*


class DaysRecyclerViewAdapter(
    private val days: List<Boolean>,
    var today: Boolean
) :
    RecyclerView.Adapter<DaysRecyclerViewAdapter.DaysViewHolder>() {

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
        holder.itemView.day_view_layout_back.setBackgroundDrawable(
            when {
                days[position] -> {
                    context.resources.getDrawable(R.drawable.day_checked)
                }
                position == checkPosition && !today -> {
                    context.resources.getDrawable(R.drawable.day_current)
                }
                else -> {
                    context.resources.getDrawable(R.drawable.day_unchecked)
                }
            }
        )

        holder.itemView.day_view_layout_back.elevation = when {
            days[position] -> {
                10f
            }
            position == checkPosition && !today -> {
                20f
            }
            else -> {
                0f
            }
        }
    }

    override fun getItemCount() = days.size

    fun setCheckPosition(checkPosition: Int) {
        this.checkPosition = checkPosition
    }
}