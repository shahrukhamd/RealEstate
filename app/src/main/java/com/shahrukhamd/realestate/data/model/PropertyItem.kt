package com.shahrukhamd.realestate.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyItem(

    @Json(name="area")
    val area: Double? = null,

    @Json(name="offerType")
    val offerType: Int? = null,

    @Json(name="city")
    val city: String? = null,

    @Json(name="price")
    val price: Double? = null,

    @Json(name="propertyType")
    val propertyType: String? = null,

    @Json(name="id")
    val id: Int? = null,

    @Json(name="url")
    val url: String? = null,

    @Json(name="professional")
    val professional: String? = null,

    @Json(name="rooms")
    val rooms: Int? = null,

    @Json(name="bedrooms")
    val bedrooms: Int? = null
) : Parcelable
