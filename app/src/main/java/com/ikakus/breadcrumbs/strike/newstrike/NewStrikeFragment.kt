package com.ikakus.breadcrumbs.strike.newstrike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ikakus.breadcrumbs.R
import com.ikakus.breadcrumbs.utils.Storage

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
        view.findViewById<Button>(R.id.button_start)?.apply {
            val storage = Storage(requireContext())
            this.setOnClickListener {
                storage.setActive(true)
            }
        }
    }
}
