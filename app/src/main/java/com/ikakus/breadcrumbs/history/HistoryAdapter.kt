package com.ikakus.breadcrumbs.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.strike.common.StrikeDto
import com.ikakus.breadcrumbs.strike.common.StrikeStatus
import kotlinx.android.synthetic.main.history_item_view.view.*

class HistoryAdapter(
    private val items: List<StrikeDto>
) : RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    lateinit var context: Context
    class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        context = parent.context
        val dayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item_view, parent, false) as ConstraintLayout
        return HistoryItemViewHolder(dayView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val strike = items[position]
        holder.itemView.tv_name.text = strike.title
        holder.itemView.tv_status.text = strike.status.toString()
        if (strike.status == StrikeStatus.FAILED) {
            holder.itemView.tv_status.setTextColor(context.resources.getColor(R.color.red))
        } else {
            holder.itemView.tv_status.setTextColor(context.resources.getColor(R.color.green))
        }
    }
}
