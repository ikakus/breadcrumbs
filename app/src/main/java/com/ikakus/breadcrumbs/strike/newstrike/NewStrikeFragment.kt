package com.ikakus.breadcrumbs.strike.newstrike

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseFragment
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.common.StrikeRepository

val NEW_STRIKE_STARTED = "NEW_STRIKE_STARTED"

class NewStrikeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_strike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<EditText>(R.id.et_new_strike_name)
        view.findViewById<Button>(R.id.button_start)?.apply {
            this.setOnClickListener {
                handleCreation(title)
            }
        }
    }

    private fun handleCreation(
        title: EditText
    ) {
        val repo = StrikeRepository(requireContext())
        val strike = Strike(repo)
        val strikeName = title.text.toString()
        if (nameValid(strikeName)) {
            strike.initializeActive(title.text.toString())
            val intent = Intent(NEW_STRIKE_STARTED)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        } else {
            Toast.makeText(requireContext(), "Please fill the name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun nameValid(text: String): Boolean {
        return text.isNotEmpty()
    }
}
