package com.ikakus.breadcrumbs.strike.failed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.core.base.BaseFragment


class FailedFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_failed_strike, container, false)
    }

}
