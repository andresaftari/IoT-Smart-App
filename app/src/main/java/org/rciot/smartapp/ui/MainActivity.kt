package org.rciot.smartapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import org.rciot.smartapp.R
import org.rciot.smartapp.databinding.ActivityMainBinding
import org.rciot.smartapp.utils.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = getString(R.string.title_environment)

        val pagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) = when (position) {
                0 -> binding.title.text = getString(R.string.title_environment)
                1 -> binding.title.text = getString(R.string.title_energy)
                2 -> binding.title.text = getString(R.string.title_oxygen)
                else -> binding.title.text = getString(R.string.app_name)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        tabs.setupWithViewPager(viewPager)
    }
}