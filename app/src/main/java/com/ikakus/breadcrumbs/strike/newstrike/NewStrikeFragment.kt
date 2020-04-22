package com.ikakus.breadcrumbs.strike.newstrike

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.utils.Storage

val NEW_STRIKE_STARTED = "NEW_STRIKE_STARTED"

class NewStrikeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_strike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<EditText>(R.id.title)
        view.findViewById<Button>(R.id.button_start)?.apply {
            val storage = Storage(requireContext())
            this.setOnClickListener {
                storage.setActive(true)
                storage.setTitle(title.text.toString())
                val intent = Intent(NEW_STRIKE_STARTED)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }
    }
}
