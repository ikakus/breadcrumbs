package com.ikakus.breadcrumbs.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.common.StrikeStatus

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val repo = Repo(this)
        val strike = Strike(repo)
        findViewById<RecyclerView>(R.id.recycler).apply {
            adapter =
                HistoryAdapter(strike.getHistory().filter { it.status != StrikeStatus.ACTIVE })
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }
}