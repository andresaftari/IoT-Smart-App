package org.rciot.smartapp.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.rciot.smartapp.R
import org.rciot.smartapp.ui.environmentB.EnviornmentBFragment
import org.rciot.smartapp.ui.environmentA.EnvironmentAFragment
import org.rciot.smartapp.ui.environmentC.EnvironmentCFragment

private val TAB_TITLES = arrayOf(
    R.string.title_env_a,
    R.string.title_env_b,
    R.string.title_limbah
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int) = context.resources.getString(TAB_TITLES[position])

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> EnvironmentAFragment()
        1 -> EnviornmentBFragment()
        2 -> EnvironmentCFragment()
        else -> Fragment()
    }

    override fun getCount() = 3
}