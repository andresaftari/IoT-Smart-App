package org.rciot.smartapp.ui.energy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.rciot.smartapp.databinding.FragmentEnergyBinding

class EnergyFragment : Fragment() {
    private lateinit var binding: FragmentEnergyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnergyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}