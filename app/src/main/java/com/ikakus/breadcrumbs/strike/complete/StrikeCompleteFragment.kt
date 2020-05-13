package com.ikakus.breadcrumbs.strike.complete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseFragment


class StrikeCompleteFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strike_complete, container, false)
    }

}
