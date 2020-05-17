package com.ikakus.breadcrumbs.strike.complete

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseFragment
import com.ikakus.breadcrumbs.strike.common.Repo
import com.ikakus.breadcrumbs.strike.common.Strike
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED


class StrikeCompleteFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strike_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = Repo(requireContext())
        val strike = Strike(repo)
        val title = view.findViewById<TextView>(R.id.et_strike_name)
        title.text = strike.getCurrent().title
        view.findViewById<Button>(R.id.button_start)?.apply {
            this.setOnClickListener {
                strike.initializeNew()
                val intent = Intent(NEW_STRIKE_STARTED)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }
    }
}
