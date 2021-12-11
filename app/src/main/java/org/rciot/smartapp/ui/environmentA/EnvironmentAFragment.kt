package org.rciot.smartapp.ui.environmentA

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
import org.rciot.smartapp.data.model.LimbahResponseA
import org.rciot.smartapp.databinding.FragmentEnvironmentABinding

class EnvironmentAFragment : Fragment() {
    private lateinit var binding: FragmentEnvironmentABinding

    private lateinit var handler: Handler
    private lateinit var viewModel: EnvAViewModel
    private lateinit var antaresHTTPAPI: AntaresHTTPAPI

    private var listOfTDS = arrayListOf<String>()
    private val listOfData = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(EnvAViewModel::class.java)
        binding = FragmentEnvironmentABinding.inflate(layoutInflater, container, false)
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

        xAxis.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(true)
        }

        axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 0f
            axisMaximum = 1000f
        }

        description.apply {
            isEnabled = false
            text = ""
        }

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
            getDatasetA()
            data.observe(viewLifecycleOwner) {
                listOfData.clear()
                if (listOfData.isEmpty()) it.dataList!!.forEach { item -> listOfData.add(item) }
                val tdsList = ArrayList<String>()

                for (i in 0..40) {
                    Log.d("chart_dataC", listOfData[i])

                    getDeviceData(listOfData[i])
                    deviceData.observe(viewLifecycleOwner) { device ->
                        Log.d("chart_device_datasetC", "${device.data.con}")
                        if (device.data.con!!.contains("TDS")) {

                            val jsonBody = JSONObject(device.data.con)
                            val dataTDS = jsonBody.getString("TDS")

                            tdsList.add(dataTDS)
                        }
//                        else if (!device.data.con.contains("TDS")) {
//                            val jsonBody = JSONObject(device.data.con)
//                            val dataTDS = jsonBody.getString("TDS1")
//
//                            tdsList.add(dataTDS)
//                        }
                        fetchChartData(tdsList)
                        Log.i("list_A", "$tdsList -- ${tdsList.size}")
                    }
                }
            }
        }
    }

    // Fetch Environment Data
    @SuppressLint("SetTextI18n")
    private fun displayLevelTDS() = antaresHTTPAPI.apply {
        getLatestDataofDevice("efc52fbb21322456:db196a65b2521708", "WasteMonitoring01", "SiteA")
        this.addListener {
            if (it.requestCode == 0) {
                try {
                    val jsonBody = JSONObject(it.body)
                    val dataDevice = jsonBody.getJSONObject("m2m:cin").getString("con")

                    val data = Gson().fromJson(dataDevice, LimbahResponseA::class.java)
                    Log.d(ENV_A_CHECK, data.toString())

                    handler = Handler(Looper.getMainLooper())
                    handler.post {
                        // pH check
                        with(binding) {
                            Log.d(ENV_pH_CHECK_A, data.pH.toString())

                            // Set pH value on screen
                            tvPhLevel.text = data.pH.toString()

                            // Set pH level on screen
                            if (data.pH!! >= 1.0 && data.pH < 7.0)
                                tvLevelAcid.text = getString(R.string.text_acid)
                            else if (data.pH > 7.0 && data.pH <= 14.0)
                                tvLevelAcid.text = getString(R.string.text_alkaline)
                            else if (data.pH == 7.0)
                                tvLevelAcid.text = getString(R.string.text_neutral)

                            // Set circular chart percentage on screen
                            val value = (data.pH / 14.0) * 100
                            pbCircularAcid.setProgress(value.toInt(), true)
                        }

                        // Water check
                        with(binding) {
                            Log.d(ENV_GATE_CHECK_A, data.gateLevel.toString())

                            tvCurrentWater.text = when (data.gateLevel) {
                                0.0 -> "0 cm"
                                else -> "${data.gateLevel} cm"
                            }
                        }

                        // Temp Check
                        with(binding) {
                            Log.d(ENV_GATE_CHECK_A, data.gateLevel.toString())

                            tvCurrentTemp.text = when (data.temp) {
                                0.0 -> "0°C"
                                else -> "${data.temp}°C"
                            }
                        }

                        // Ppm Check
                        with(binding) {
                            Log.d(ENV_PPM_CHECK_A, data.tds.toString())

                            // Set Ppm value on screen
                            tvTds.text = "${data.tds} ppm"

                            // Set status
                            if (data.tds!! <= 200)
                                tvCurrentStatus.apply {
                                    text = "Normal"
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.tds_normal
                                        )
                                    )
                                }
                            if (data.tds > 200 && data.tds <= 1300)
                                tvCurrentStatus.apply {
                                    text = "Safe"
                                    setTextColor(ContextCompat.getColor(context, R.color.tds_safe))
                                }
                            if (data.tds > 1300 && data.tds <= 1700)
                                tvCurrentStatus.apply {
                                    text = "Cautious"
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.tds_cautious
                                        )
                                    )
                                }
                            if (data.tds > 1700) tvCurrentStatus.apply {
                                text = "Dangerous"
                                setTextColor(ContextCompat.getColor(context, R.color.tds_danger))
                            }

                            val percentage = (data.tds / 2500) * 100
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