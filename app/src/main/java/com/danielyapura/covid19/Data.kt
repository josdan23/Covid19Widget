package com.danielyapura.covid19

import com.google.gson.annotations.SerializedName
import java.util.*

class Data {

    @SerializedName("Date")
    var date: Date? = null

    @SerializedName("Cases")
    var cases: Int = 0
}