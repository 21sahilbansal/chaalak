package com.loconav.locodriver.landing.ui.main

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.loconav.locodriver.Trips.tripList.TripsFragment
import android.view.LayoutInflater
import android.widget.TextView
import com.loconav.locodriver.R
import com.loconav.locodriver.expense.ExpenseListFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class LandingTabPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TripsFragment.getInstance()
            else -> ExpenseListFragment.getInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    fun getTabView(position: Int): View {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.custom_tab_layout, null)
        val title = view.findViewById<TextView>(R.id.tab_title)
        setTabTitleStyle(title, position)
        title.text = getPageTitle(position)
        return view
    }

    private fun setTabTitleStyle(title: TextView, position: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            title.setTextAppearance(
                if (position == 0)
                    R.style.TopNavActive
                else
                    R.style.TopNav
            )
        } else {
            title.setTextAppearance(
                title.context, if (position == 0)
                    R.style.TopNavActive
                else
                    R.style.TopNav
            )
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_trip,
            R.string.tab_text_expense
        )
    }
}