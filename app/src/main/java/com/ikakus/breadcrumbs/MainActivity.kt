package com.ikakus.breadcrumbs

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val array: MutableList<Boolean> = mutableListOf()
    private val strikeLength = 30
    private var checkPosition = 0

    init {

        for (a in 1..strikeLength) {
            array.add(false)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewManager = GridLayoutManager(this, 6)
        viewAdapter = DaysRecyclerViewAdapter(array)

        recyclerView = findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        updateCounter()

        fab.setOnClickListener { view ->
            set(view)
        }

        fab.setOnLongClickListener { view ->
            unset(view)
            true
        }
    }

    private fun updateCounter() {
        var countView = findViewById<TextView>(R.id.count).apply {
            val count = array.count { it }
            text = "$count/$strikeLength"
        }
    }

    private fun unset(view: View) {
        decrement()
        array[checkPosition] = false
        updateCounter()
        viewAdapter.notifyDataSetChanged()
    }

    private fun set(view: View) {
        array[checkPosition] = true
        increment()
        updateCounter()
        viewAdapter.notifyDataSetChanged()
    }

    private fun decrement() {
        if (checkPosition > 0) {
            checkPosition--
        }
    }

    private fun increment() {
        if (checkPosition < strikeLength) {
            checkPosition++
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
