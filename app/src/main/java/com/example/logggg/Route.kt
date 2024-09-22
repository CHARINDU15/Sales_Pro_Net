package com.example.logggg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.android.gms.maps.model.LatLng

@Parcelize
data class Route(
    val routeName: String,
    val routeDescription: String,
    val routeCoordinates: List<LatLng> = emptyList() // Default empty list if not provided
) : Parcelable {
    // Secondary constructor
    constructor(routeName: String, routeDescription: String) : this(
        routeName,
        routeDescription,
        emptyList() // Provide default value for routeCoordinates
    )
}
