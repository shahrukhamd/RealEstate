package com.shahrukhamd.realestate.data.repository

import com.shahrukhamd.realestate.data.model.ApiResponseWrapper
import com.shahrukhamd.realestate.data.model.PropertyItem
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getProperties(): Flow<ApiResponseWrapper<List<PropertyItem>>>

    suspend fun getPropertyDetail(id: Int): Flow<ApiResponseWrapper<PropertyItem>>
}