package org.rciot.smartapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import id.co.telkom.iot.AntaresHTTPAPI
import org.json.JSONException
import org.json.JSONObject
import org.rciot.smartapp.R
import org.rciot.smartapp.data.*
import org.rciot.smartapp.data.api.LimbahResponse
import org.rciot.smartapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var handler: Handler
    private lateinit var antaresHTTPAPI: AntaresHTTPAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = getString(R.string.title_environment)
        antaresHTTPAPI = AntaresHTTPAPI()

        displayLevelTDS()
    }

    @SuppressLint("SetTextI18n")
    private fun displayLevelTDS() = antaresHTTPAPI.apply {
        getLatestDataofDevice("efc52fbb21322456:db196a65b2521708", "WasteMonitoring01", "SiteA")
        this.addListener {
            if (it.requestCode == 0) {
                try {
//                    val formatter = SimpleDateFormat("dd MM YYYY", Locale("ID", "id"))
                    val jsonBody = JSONObject(it.body)
                    val dataDevice = jsonBody.getJSONObject("m2m:cin").getString("con")

                    val data = Gson().fromJson(dataDevice, LimbahResponse::class.java)
                    Log.d(ENV_CHECK, data.toString())

                    handler = Handler(Looper.getMainLooper())
                    handler.post {
                        // pH check
                        with(binding) {
                            Log.d(ENV_pH_CHECK, data.pH1.toString())

                            // Set pH value on screen
                            tvPhLevel.text = data.pH1.toString()

                            // Set pH level on screen
                            if (data.pH1!! >= 1.0 && data.pH1 < 7.0)
                                tvLevelAcid.text = getString(R.string.text_acid)
                            else if (data.pH1 > 7.0 && data.pH1 <= 14.0)
                                tvLevelAcid.text = getString(R.string.text_alkaline)
                            else if (data.pH1 == 7.0)
                                tvLevelAcid.text = getString(R.string.text_neutral)

                            // Set circular chart percentage on screen
                            val value = (data.pH1 / 14.0) * 100
                            pbCircularAcid.setProgress(value.toInt(), true)
                        }

                        // Water check
                        with(binding) {
                            Log.d(ENV_WATER_CHECK, data.Tank1Level.toString())
                            Log.d(ENV_GATE_CHECK, data.Gate1Level.toString())

                            // Set water & gate value on screen
                            tvCurrentWater.text = when (data.Tank1Level) {
                                0.0 -> "0 cm"
                                else -> "${data.Tank1Level} cm"
                            }
                            tvCurrentGate.text = when (data.Gate1Level) {
                                0.0 -> "0 cm"
                                else -> "${data.Gate1Level} cm"
                            }

                            // Set water pump & gate status
                            when (data.Pump1Status!!.toInt()) {
                                0 -> switchPump.isChecked = false
                                1 -> switchPump.isChecked = true
                            }

                            when (data.Gate1Status!!.toInt()) {
                                0 -> switchGate.isChecked = false
                                1 -> switchGate.isChecked = true
                            }
                        }

                        // Ppm Check
                        with(binding) {
                            Log.d(ENV_PPM_CHECK, data.TDS1.toString())

                            // Set Ppm value on screen
                            tvTds.text = "${data.TDS1} ppm"

                            // Set status
                            if (data.TDS1!! <= 200) tvCurrentStatus.apply {
                                text = "Normal"
                                setTextColor(resources.getColor(R.color.tds_normal))
                            }
                            if (data.TDS1 > 200 && data.TDS1 <= 1300) tvCurrentStatus.apply {
                                text = "Safe"
                                setTextColor(resources.getColor(R.color.tds_safe))
                            }
                            if (data.TDS1 > 1300 && data.TDS1 <= 1700) tvCurrentStatus.apply {
                                text = "Cautious"
                                setTextColor(resources.getColor(R.color.tds_cautious))
                            }
                            if (data.TDS1 > 1700) tvCurrentStatus.apply {
                                text = "Dangerous"
                                setTextColor(resources.getColor(R.color.tds_danger))
                            }

                            val percentage = (data.TDS1 / 2500) * 100
                            pbTds.setProgress(percentage.toInt(), true)
                        }

                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                }
            }
        }
    }
}