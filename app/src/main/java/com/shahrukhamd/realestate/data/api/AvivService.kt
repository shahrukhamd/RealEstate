package com.shahrukhamd.realestate.data.api

import com.shahrukhamd.realestate.data.model.PropertyItem
import com.shahrukhamd.realestate.data.model.RealEstateListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AvivService {

    @GET("/listings.json")
    suspend fun getPropertyList(): Response<RealEstateListResponse>

    @GET("/listings/{id}.json")
    suspend fun getPropertyDetails(@Path("id") id: Int): Response<PropertyItem>
}