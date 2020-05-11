package com.ikakus.breadcrumbs.strike.failed

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
import com.ikakus.breadcrumbs.strike.common.StrikeStatus
import com.ikakus.breadcrumbs.strike.newstrike.NEW_STRIKE_STARTED


class FailedFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_failed_strike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = Repo(requireContext())
        val strike = Strike(repo)
        view.findViewById<TextView>(R.id.tv_failed).apply {
            val strikeName = strike.getTitle()
            text = "Oh no $strikeName failed"
        }
        setupListeners(view)
    }

    private fun setupListeners(view: View) {
        val repo = Repo(requireContext())
        val strike = Strike(repo)

        view.findViewById<Button>(R.id.btn_restart)?.apply {
            setOnClickListener {
                val title =
                    strike.getHistory().firstOrNull { it.status == StrikeStatus.FAILED }?.title
                        ?: ""
                strike.initializeActive(title)
                val intent = Intent(NEW_STRIKE_STARTED)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }
        view.findViewById<Button>(R.id.btn_new)?.apply {
            setOnClickListener {
                strike.initializeNew()
                val intent = Intent(NEW_STRIKE_STARTED)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }
    }

}
