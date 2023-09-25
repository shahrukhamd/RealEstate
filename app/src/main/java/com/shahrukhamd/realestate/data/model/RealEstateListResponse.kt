package com.shahrukhamd.realestate.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.squareup.moshi.Json

@Parcelize
data class RealEstateListResponse(

	@Json(name="items")
	val items: List<PropertyItem?>? = null,

	@Json(name="totalCount")
	val totalCount: Int? = null

) : Parcelable
