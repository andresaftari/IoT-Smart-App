package org.rciot.smartapp.ui.environmentC

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import id.co.telkom.iot.AntaresHTTPAPI
import org.json.JSONException
import org.json.JSONObject
import org.rciot.smartapp.R
import org.rciot.smartapp.data.*
import org.rciot.smartapp.data.model.LimbahResponseC
import org.rciot.smartapp.databinding.FragmentEnvironmentCBinding

class EnvironmentCFragment : Fragment() {
    private lateinit var binding: FragmentEnvironmentCBinding

    private lateinit var handler: Handler
    private lateinit var viewModel: EnvCViewModel
    private lateinit var antaresHTTPAPI: AntaresHTTPAPI

    private var listOfTDS = arrayListOf<String>()
    private val listOfData = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(EnvCViewModel::class.java)
        binding = FragmentEnvironmentCBinding.inflate(layoutInflater, container, false)
        antaresHTTPAPI = AntaresHTTPAPI()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOfData.clear()
        displayLevelTDS()
        setupChart()
        fetchTDSData()
    }

    private fun setupChart() = with(binding.chart) {
        setNoDataText("No TDS Data")

        xAxis.setDrawLabels(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)

        axisLeft.setDrawGridLines(false)
        axisLeft.axisMinimum = 0f
        axisLeft.axisMaximum = 1000f
        description.isEnabled = false
        description.text = ""

        animateX(1000, Easing.EaseInSine)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            setDrawInside(false)
        }
    }

    private fun fetchChartData(tdsList: ArrayList<String>) {
        val entries = ArrayList<Entry>()

        listOfTDS = tdsList

        for (i in tdsList.indices) {
            val tds = tdsList[i]
            entries.add(Entry(i.toFloat(), tds.toFloat()))
        }

        val dataset = LineDataSet(entries, getString(R.string.text_tds_level))
        Log.i("entries_c", "$entries")

        with(dataset) {
            color = ContextCompat.getColor(requireContext(), R.color.tds_safe)
            fillColor = dataset.color
            setDrawFilled(true)
            setDrawCircles(false)
            setDrawValues(false)
        }

        val data = LineData(dataset)

        binding.chart.apply {
            this.data = data
            this.invalidate()
        }
    }

    // Fetch TDS chart
    private fun fetchTDSData() {
        with(viewModel) {
            getStatus().observe(viewLifecycleOwner) { loadingIndicator(it) }
            getDatasetC()
            data.observe(viewLifecycleOwner) {
                listOfData.clear()
                if (listOfData.isEmpty()) it.dataList!!.forEach { item -> listOfData.add(item) }
                val tdsList = ArrayList<String>()

                for (i in 0..40) {
                    Log.d("chart_dataC", listOfData[i])

                    getDeviceData(listOfData[i])
                    deviceData.observe(viewLifecycleOwner) { device ->
                        Log.d("chart_device_datasetC", "${device.data.con}")
                        if (device.data.con!!.contains("TDS3")) {

                            val jsonBody = JSONObject(device.data.con)
                            val dataTDS = jsonBody.getString("TDS3")

                            tdsList.add(dataTDS)
                        }
                        fetchChartData(tdsList)
                        Log.i("list_C", "$tdsList")
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayLevelTDS() = antaresHTTPAPI.apply {
        getLatestDataofDevice("efc52fbb21322456:db196a65b2521708", "WasteMonitoring01", "SiteC")
        this.addListener {
            if (it.requestCode == 0) {
                try {
                    val jsonBody = JSONObject(it.body)
                    val dataDevice = jsonBody.getJSONObject("m2m:cin").getString("con")

                    val data = Gson().fromJson(dataDevice, LimbahResponseC::class.java)
                    Log.d(ENV_C_CHECK, data.toString())

                    handler = Handler(Looper.getMainLooper())
                    handler.post {
                        // pH check
                        with(binding) {
                            Log.d(ENV_pH_CHECK_C, data.pH3.toString())

                            // Set pH value on screen
                            tvPhLevel.text = data.pH3.toString()

                            // Set pH level on screen
                            if (data.pH3!! >= 1.0 && data.pH3 < 7.0)
                                tvLevelAcid.text = getString(R.string.text_acid)
                            else if (data.pH3 > 7.0 && data.pH3 <= 14.0)
                                tvLevelAcid.text = getString(R.string.text_alkaline)
                            else if (data.pH3 == 7.0)
                                tvLevelAcid.text = getString(R.string.text_neutral)

                            // Set circular chart percentage on screen
                            val value = (data.pH3 / 14.0) * 100
                            pbCircularAcid.setProgress(value.toInt(), true)
                        }

                        // Water check
                        with(binding) {
                            Log.d(ENV_WATER_CHECK_C, data.tank3Level.toString())
                            Log.d(ENV_GATE_CHECK_C, data.gate3Level.toString())

                            // Set water & gate value on screen
                            tvCurrentWater.text = when (data.tank3Level) {
                                0.0 -> "0 cm"
                                else -> "${data.tank3Level} cm"
                            }
                            tvCurrentGate.text = when (data.gate3Level) {
                                0.0 -> "0 cm"
                                else -> "${data.gate3Level} cm"
                            }

                            // Set water pump & gate status
                            when (data.pump3Status!!.toInt()) {
                                0 -> switchPump.isChecked = false
                                1 -> switchPump.isChecked = true
                            }

                            when (data.gate3Status!!.toInt()) {
                                0 -> switchGate.isChecked = false
                                1 -> switchGate.isChecked = true
                            }
                        }

                        // Ppm Check
                        with(binding) {
                            Log.d(ENV_PPM_CHECK_C, data.tds3.toString())

                            // Set Ppm value on screen
                            tvTds.text = "${data.tds3} ppm"

                            // Set status
                            if (data.tds3!! <= 200)
                                tvCurrentStatus.apply {
                                    text = "Normal"
                                    setTextColor(resources.getColor(R.color.tds_normal))
                                }
                            if (data.tds3 > 200 && data.tds3 <= 1300)
                                tvCurrentStatus.apply {
                                    text = "Safe"
                                    setTextColor(resources.getColor(R.color.tds_safe))
                                }
                            if (data.tds3 > 1300 && data.tds3 <= 1700)
                                tvCurrentStatus.apply {
                                    text = "Cautious"
                                    setTextColor(resources.getColor(R.color.tds_cautious))
                                }
                            if (data.tds3 > 1700) tvCurrentStatus.apply {
                                text = "Dangerous"
                                setTextColor(resources.getColor(R.color.tds_danger))
                            }

                            val percentage = (data.tds3 / 2500) * 100
                            pbTds.setProgress(percentage.toInt(), true)
                        }
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun loadingIndicator(apiStatus: ApiStatus) = with(binding) {
        when (apiStatus) {
            ApiStatus.LOADING -> pbLoading.visibility = View.VISIBLE
            ApiStatus.SUCCESS -> pbLoading.visibility = View.GONE
            ApiStatus.FAILED -> {
                pbLoading.visibility = View.GONE
                Snackbar.make(requireView(), "No data connection", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}