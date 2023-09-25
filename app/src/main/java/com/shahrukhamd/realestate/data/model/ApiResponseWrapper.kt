package com.shahrukhamd.realestate.data.model

data class ApiResponseWrapper<T>(
    val responseData: T? = null,
    val code: Int,
    val message: String? = null
)