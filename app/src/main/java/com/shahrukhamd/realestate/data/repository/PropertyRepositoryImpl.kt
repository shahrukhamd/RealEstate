package com.shahrukhamd.realestate.data.repository

import com.shahrukhamd.realestate.data.api.AvivService
import com.shahrukhamd.realestate.data.model.ApiResponseWrapper
import com.shahrukhamd.realestate.data.model.PropertyItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val avivService: AvivService
): PropertyRepository {

    override suspend fun getProperties(): Flow<ApiResponseWrapper<List<PropertyItem>>> =
        flow {
            avivService.getPropertyList().let {
                if (it.isSuccessful) {
                    emit(
                        ApiResponseWrapper(
                            it.body()?.items?.filterNotNull(),
                            it.code(),
                            it.message()
                        )
                    )
                } else {
                    emit(ApiResponseWrapper(null, it.code(), it.message()))
                }
            }
        }

    override suspend fun getPropertyDetail(id: Int): Flow<ApiResponseWrapper<PropertyItem>> =
        flow {
            avivService.getPropertyDetails(id).let {
                if (it.isSuccessful) {
                    emit(ApiResponseWrapper(it.body(), it.code(), it.message()))
                } else {
                    emit(ApiResponseWrapper(null, it.code(), it.message()))
                }
            }
        }
}