package org.rciot.smartapp.data

// Environment Tags
const val ENV_A_CHECK = "CHECK_ENVIRONMENT_A"
const val ENV_B_CHECK = "CHECK_ENVIRONMENT_B"
const val ENV_C_CHECK = "CHECK_ENVIRONMENT_C"
const val ENV_pH_CHECK_A = "CHECK_PH_A"
const val ENV_pH_CHECK_B = "CHECK_PH_B"
const val ENV_pH_CHECK_C = "CHECK_PH_C"
const val ENV_TEMP_CHECK_A = "CHECK_TEMP_A"
const val ENV_WATER_CHECK_B = "CHECK_WATER_B"
const val ENV_WATER_CHECK_C = "CHECK_WATER_C"
const val ENV_GATE_CHECK_A = "CHECK_GATE_A"
const val ENV_GATE_CHECK_B = "CHECK_GATE_B"
const val ENV_GATE_CHECK_C = "CHECK_GATE_C"
const val ENV_PPM_CHECK_A = "CHECK_PPM_A"
const val ENV_PPM_CHECK_B = "CHECK_PPM_B"
const val ENV_PPM_CHECK_C = "CHECK_PPM_C"

// API Endpoints
const val AUTH = "efc52fbb21322456:db196a65b2521708"
const val BASE_URL = "https://platform.antares.id:8443/"
const val ENV_A_LIST_ENDPOINT =
    "~/antares-cse/antares-id/WasteMonitoring01/SiteA?fu=1&ty=4&drt=2"
const val ENV_B_LIST_ENDPOINT =
    "~/antares-cse/antares-id/WasteMonitoring01/SiteB?fu=1&ty=4&drt=2"
const val ENV_C_LIST_ENDPOINT =
    "~/antares-cse/antares-id/WasteMonitoring01/SiteC?fu=1&ty=4&drt=2"