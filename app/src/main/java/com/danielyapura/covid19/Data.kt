package com.danielyapura.covid19

import com.google.gson.annotations.SerializedName
import java.util.*

class Data {

    @SerializedName("Date")
    var date: Date? = null

    @SerializedName("Cases")
    var cases: Int = 0

    @SerializedName("deaths")
    var deaths: Int = 0

    @SerializedName("confirmed")
    var confirmed: Int = 0

    @SerializedName("recovered")
    var recovered: Int = 0

    @SerializedName("Active")
    var active = 0
}