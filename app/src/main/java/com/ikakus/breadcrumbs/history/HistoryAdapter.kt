package com.ikakus.breadcrumbs.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.strike.common.StrikeDto
import kotlinx.android.synthetic.main.history_item_view.view.*

class HistoryAdapter(
    private val items: List<StrikeDto>
) : RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val dayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item_view, parent, false) as ConstraintLayout
        return HistoryItemViewHolder(dayView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.itemView.history_item_text.text = items[position].status.toString()
    }
}
