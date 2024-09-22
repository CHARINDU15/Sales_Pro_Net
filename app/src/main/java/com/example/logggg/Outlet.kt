package com.example.logggg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Outlet(
    val id: String,
    val name: String,
    val category: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable
