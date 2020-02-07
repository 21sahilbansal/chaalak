package com.loconav.locodriver.landing.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.loconav.locodriver.R


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private var pageViewModel: PageViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
            var index = 1
            index = it.getInt(ARG_SECTION_NUMBER)
            pageViewModel?.let {
                it.setIndex(index)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_landing, container, false)
        val textView = root.findViewById<TextView>(R.id.section_label)
        pageViewModel?.text?.observe(this, Observer { s -> textView.text = s })
        return root
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }
}