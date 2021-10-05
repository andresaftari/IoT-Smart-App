package org.rciot.smartapp.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.rciot.smartapp.R
import org.rciot.smartapp.ui.energy.EnergyFragment
import org.rciot.smartapp.ui.environment.EnvironmentFragment

private val TAB_TITLES = arrayOf(
    R.string.title_environment,
    R.string.title_energy,
    R.string.title_oxygen
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int) = context.resources.getString(TAB_TITLES[position])

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> EnvironmentFragment()
        1 -> EnergyFragment()
        else -> Fragment()
    }

    override fun getCount() = 3
}