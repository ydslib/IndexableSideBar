package com.yds.indexablesidebar.bean

data class CountryData(val data:MutableList<CountryBean>)

data class CountryBean(
    var character: String,
    val countryName:String,
    val phoneCode:String,
    val countryCode:String
)