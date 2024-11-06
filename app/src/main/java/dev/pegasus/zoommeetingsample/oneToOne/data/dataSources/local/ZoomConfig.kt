package dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.local

import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class ZoomConfig {

    val zoomClientId = "F0aAKy0_QO2Q1L0XEdj78w"
    val zoomClientSecret = "E4moSlqY35ATG6wVfElTQMYPDsOY36Hz"

    val redirectUri = "yourapp://callback"
    val authorizationUrl = "https://zoom.us/oauth/authorize?response_type=code&client_id=${ConstantUtils.CLIENT_ID}&redirect_uri=$redirectUri"

}